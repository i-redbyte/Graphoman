package org.redbyte.graphoman.graphoman.struct.graph.tree

import org.redbyte.graphoman.graphoman.types.Result
import kotlin.math.max

sealed class BinaryTree<out T : Comparable<@UnsafeVariance T>> {

    abstract val size: Int
    abstract val height: Int
    abstract fun isEmpty(): Boolean
    abstract fun max(): Result<T>
    abstract fun min(): Result<T>

    internal object Empty : BinaryTree<Nothing>() {
        override val size: Int = 0

        override val height: Int = -1

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"

        override fun max(): Result<Nothing> = Result.empty()

        override fun min(): Result<Nothing> = Result.empty()

    }

    internal class Node<T : Comparable<T>>(
        internal val value: T,
        internal val left: BinaryTree<T>,
        internal val right: BinaryTree<T>
    ) : BinaryTree<T>() {

        override val size: Int = 1 + left.size + right.size

        override val height: Int = 1 + max(left.height, right.height)

        override fun isEmpty(): Boolean = false

        override fun toString(): String = "(Node $left $value $right)"

        override fun max(): Result<T> = right.max().orElse { Result(value) }

        override fun min(): Result<T> = left.min().orElse { Result(value) }
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