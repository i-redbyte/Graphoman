package org.redbyte.graphoman.graphoman.types

import java.io.Serializable

sealed class Result<out T> : Serializable {

    abstract fun <B> map(f: (T) -> B): Result<B>
    abstract fun <B> flatMap(f: (T) -> Result<B>): Result<B>

    internal class Failure<out T>(private val exception: Exception) : Result<T>() {

        override fun <R> map(f: (T) -> R): Result<R> = Failure(exception)

        override fun <R> flatMap(f: (T) -> Result<R>): Result<R> = Failure(exception)

        override fun toString(): String = "Failure(${exception.message})"
    }

    internal class Success<out T>(internal val value: T) : Result<T>() {

        override fun <R> map(f: (T) -> R): Result<R> {
            return try {
                Success(f(value))
            } catch (e: Exception) {
                Failure(RuntimeException(e))
            }
        }

        override fun <B> flatMap(f: (T) -> Result<B>): Result<B> {
            return try {
                f(value)
            } catch (e: Exception) {
                Failure(RuntimeException(e))
            }
        }

        override fun toString(): String = "Success($value)"
    }

    internal object Empty : Result<Nothing>() {
        override fun <B> map(f: (Nothing) -> B): Result<B> = Empty
        override fun <B> flatMap(f: (Nothing) -> Result<B>): Result<B> = Empty
        override fun toString(): String = "Empty"
    }

    fun getOrElse(defaultValue: @UnsafeVariance T): T = when (this) {
        is Success -> this.value
        else -> defaultValue
    }

    fun getOrNull(): T? = when (this) {
        is Success -> this.value
        else -> null
    }

    fun orElse(defaultValue: () -> Result<@UnsafeVariance T>): Result<T> =
        when (this) {
            is Success -> this
            else -> try {
                defaultValue()
            } catch (e: Exception) {
                failure(RuntimeException(e))
            }
        }

    fun <K, V> Map<K, V>.getResult(key: K) = when {
        this.containsKey(key) -> Result(this[key])
        else -> Empty
    }

    fun filter(predicate: (T) -> Boolean): Result<T> = flatMap {
        return@flatMap if (predicate(it)) this else Empty
    }

    fun filter(message: String, predicate: (T) -> Boolean): Result<T> = flatMap {
        return@flatMap if (predicate(it)) this else failure(message)
    }

    fun <T, B> lift(f: (T) -> B): (Result<T>) -> Result<B> = { it.map(f) }

    companion object {

        operator fun <T> invoke(a: T? = null): Result<T> = when (a) {
            null -> Failure(NullPointerException())
            else -> Success(a)
        }

        operator fun <T> invoke(t: T? = null, message: String): Result<T> = when (t) {
            null -> Failure(NullPointerException(message))
            else -> Success(t)
        }

        operator fun <T> invoke(): Result<T> = Empty


        fun <T> failure(message: String): Result<T> = Failure(IllegalStateException(message))
        fun <T> failure(exception: RuntimeException): Result<T> = Failure(exception)
        fun <T> empty(): Result<T> = Empty

    }

}