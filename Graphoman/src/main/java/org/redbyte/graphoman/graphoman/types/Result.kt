package org.redbyte.graphoman.graphoman.types

import java.io.Serializable

sealed class Result<out T> : Serializable {

    abstract fun <B> map(f: (T) -> B): Result<B>

    abstract fun <B> flatMap(f: (T) -> Result<B>): Result<B>

    fun getOrElse(defaultValue: @UnsafeVariance T): T = when (this) {
        is Success -> this.value
        is Failure -> defaultValue
    }

    fun orElse(defaultValue: () -> Result<@UnsafeVariance T>): Result<T> = when (this) {
        is Success -> this
        is Failure -> try {
            defaultValue()
        } catch (e: Exception) {
            failure(e)
        }
    }

    class Failure<out T>(internal val exception: Exception) : Result<T>() {

        override fun toString(): String = "Failure(${exception.message})"

        override fun <R> map(f: (T) -> R): Result<R> = Failure(exception)

        override fun <R> flatMap(f: (T) -> Result<R>): Result<R> = Failure(exception)
    }

    class Success<out T>(internal val value: T) : Result<T>() {
        override fun toString(): String = "Failure($value)"

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

        fun getValue() = value
    }

    companion object {
        operator fun <T> invoke(a: T? = null): Result<T> =
            when (a) {
                null -> Failure(NullPointerException())
                else -> Success(a)
            }

        fun <T> failure(message: String): Result<T> = Failure(IllegalStateException(message))
        fun <T> failure(exception: Exception): Result<T> = Failure(exception)
    }
}