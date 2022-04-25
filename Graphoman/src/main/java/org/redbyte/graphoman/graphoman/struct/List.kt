package org.redbyte.graphoman.graphoman.struct

sealed class List<out A> {
    abstract fun isEmpty(): Boolean

    abstract fun init(): List<A>

    abstract fun <B> foldLeft(
        identity: B,
        p: (B) -> Boolean,
        f: (B) -> (A) -> B): B

    abstract fun <B> foldLeft(
        identity: B, zero: B,
        f: (B) -> (A) -> B): Pair<B, List<A>>

    internal object Nil : List<Nothing>() {
        override fun toString(): String = "[NIL]"

        override fun isEmpty(): Boolean = true

        override fun <B> foldLeft(identity: B, p: (B) -> Boolean, f: (B) -> (Nothing) -> B): B = identity

        override fun <B> foldLeft(identity: B, zero: B, f: (B) -> (Nothing) -> B):
                Pair<B, List<Nothing>> = Pair(identity, Nil)

        override fun init(): List<Nothing> = throw IllegalStateException("You can't call init on an empty list")
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

        override fun <B> foldLeft(identity: B, zero: B, f: (B) -> (A) -> B): Pair<B, List<A>> {
            tailrec fun <B> foldLeft(acc: B, zero: B, list: List<A>, f: (B) -> (A) -> B): Pair<B, List<A>> = when (list) {
                Nil -> Pair(acc, list)
                is Exist ->
                    if (acc == zero)
                        Pair(acc, list)
                    else
                        foldLeft(f(acc)(list.head), zero, list.tail, f)
            }
            return foldLeft(identity, zero, this, f)
        }

        private tailrec fun toString(s: String, list: List<A>): String = when (list) {
            Nil -> s
            is Exist -> toString("$s${list.head}, ", list.tail)
        }

        override fun init(): List<A> = reverse().drop(1).reverse()
    }

    fun addToHead(a: @UnsafeVariance A): List<A> = Exist(a, this)

    fun <B> foldLeft(identity: B, f: (B) -> (A) -> B): B = foldLeft(identity, this, f)

    operator fun plus(a: @UnsafeVariance A): List<A> = addToHead(a)

    fun reverse(): List<A> = foldLeft(Nil as List<A>) { acc -> { acc.addToHead(it) } }

    fun drop(n: Int): List<A> = drop(this, n)

    companion object {
        operator fun <A> invoke(vararg values: A): List<A> =
            values.foldRight(Nil) { a: A, list: List<A> -> Exist(a, list) }

        fun <A> addToHead(a: A, list: List<A>): List<A> = Exist(a, list)

        tailrec fun <A, B> foldLeft(acc: B, list: List<A>, f: (B) -> (A) -> B): B =
            when (list) {
                Nil -> acc
                is Exist -> foldLeft(f(acc)(list.head), list.tail, f)
            }

        tailrec fun <A> drop(list: List<A>, n: Int): List<A> = when (list) {
            Nil -> list
            is Exist -> if (n <= 0) list else drop(list.tail, n - 1)
        }
    }
}