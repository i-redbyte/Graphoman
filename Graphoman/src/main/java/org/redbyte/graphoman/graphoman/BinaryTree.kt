package org.redbyte.graphoman.graphoman

sealed class BinaryTree<out T : Comparable<@UnsafeVariance T>> {
    abstract fun isEmpty(): Boolean

    internal object Empty : BinaryTree<Nothing>() {
        override fun isEmpty(): Boolean = true
        override fun toString(): String = "E"

    }

    internal class Node<T : Comparable<T>>(
        internal val value: T,
        internal val left: BinaryTree<T>,
        internal val right: BinaryTree<T>
    ) : BinaryTree<T>() {
        override fun isEmpty(): Boolean = false
        override fun toString(): String = "(Node $left $value $right)"
    }

    operator fun plus(element: @UnsafeVariance T): BinaryTree<T> = when (this) {
        Empty -> Node(element, Empty, Empty)
        is Node -> when {
            element < this.value -> Node(this.value, left + element, right)
            element > this.value -> Node(this.value, left, right + element)
            else -> this
        }
    }

    companion object {
        operator fun <T : Comparable<T>> invoke(vararg data: T): BinaryTree<T> =
            data.foldRight(Empty) { t: T, tree: BinaryTree<T> -> tree.plus(t) }
    }
}