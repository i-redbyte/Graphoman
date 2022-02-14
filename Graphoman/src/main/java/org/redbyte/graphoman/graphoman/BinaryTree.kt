package org.redbyte.graphoman.graphoman

import kotlin.math.max

sealed class BinaryTree<out T : Comparable<@UnsafeVariance T>> {
    abstract fun isEmpty(): Boolean
    abstract fun size(): Int
    abstract fun height(): Int

    internal object Empty : BinaryTree<Nothing>() {

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"

        override fun size(): Int = 0

        override fun height(): Int = 0

    }

    internal class Node<T : Comparable<T>>(
        internal val value: T,
        internal val left: BinaryTree<T>,
        internal val right: BinaryTree<T>
    ) : BinaryTree<T>() {

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "(Node $left $value $right)"

        override fun size(): Int = 1 + left.size() + right.size()

        override fun height(): Int = 1 + max(left.height(), right.height())
    }

    operator fun plus(element: @UnsafeVariance T): BinaryTree<T> = when (this) {
        Empty -> Node(element, Empty, Empty)
        is Node -> when {
            element < this.value -> Node(this.value, left + element, right)
            element > this.value -> Node(this.value, left, right + element)
            else -> this
        }
    }

    fun contains(t: @UnsafeVariance T): Boolean = when (this) {
        is Empty -> false
        is Node -> when {
            t < value -> left.contains(t)
            t > value -> right.contains(t)
            else -> value == t
        }
    }

    companion object {
        operator fun <T : Comparable<T>> invoke(vararg data: T): BinaryTree<T> =
            data.foldRight(Empty) { t: T, tree: BinaryTree<T> -> tree.plus(t) }
    }
}