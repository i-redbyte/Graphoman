package org.redbyte.graphoman.graphoman.struct

sealed class List<out A> {
    abstract fun isEmpty(): Boolean

    abstract fun <B> foldLeft(
        identity: B,
        p: (B) -> Boolean,
        f: (B) -> (A) -> B
    ): B

    internal object Nil : List<Nothing>() {
        override fun toString(): String = "[NIL]"

        override fun isEmpty(): Boolean = true

        override fun <B> foldLeft(identity: B, p: (B) -> Boolean, f: (B) -> (Nothing) -> B): B = identity
    }

    internal class Exist<out A>( // TODO: Red_byte 17.04.2022 rename class?
        internal val head: A,
        internal val tail: List<A>
    ) : List<A>() {

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "[${toString("", this)}NIL]"

        override fun <B> foldLeft(identity: B, p: (B) -> Boolean, f: (B) -> (A) -> B): B {
            fun foldLeft(acc: B, list: List<A>): B = when (list) {
                Nil -> acc
                is Exist ->
                    if (p(acc))
                        acc
                    else
                        foldLeft(f(acc)(list.head), list.tail)
            }
            return foldLeft(identity, this)
        }

        private tailrec fun toString(s: String, list: List<A>): String = when (list) {
            Nil -> s
            is Exist -> toString("$s${list.head}, ", list.tail)
        }

    }

    fun addToHead(a: @UnsafeVariance A): List<A> = Exist(a, this)

    operator fun plus(a: @UnsafeVariance A): List<A> = addToHead(a)

    companion object {
        operator fun <A> invoke(vararg values: A): List<A> =
            values.foldRight(Nil) { a: A, list: List<A> -> Exist(a, list) }

        fun <A> addToHead(a: A, list: List<A>): List<A> = Exist(a, list)

        tailrec fun <A, B> foldLeft(acc: B, list: List<A>, f: (B) -> (A) -> B): B =
            when (list) {
                Nil -> acc
                is Exist -> foldLeft(f(acc)(list.head), list.tail, f)
            }
    }
}