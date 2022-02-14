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
        override fun toString(): String = "Node(left = $left value = $value right = $right)"
    }

    operator fun plus(element: @UnsafeVariance T): BinaryTree<T> = when (this) {
        Empty -> Node(element, Empty, Empty)
        is Node -> when {
            element < this.value -> Node(this.value, left + element, right)
            element > this.value -> Node(this.value, left, right + element)
            else -> this//Node(element, this.left, this.right)
        }
    }

    companion object {
        operator fun <T : Comparable<T>> invoke(): BinaryTree<T> = Empty
    }
}