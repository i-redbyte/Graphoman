package org.redbyte.graphoman.graphoman.struct.graph.tree

import org.redbyte.graphoman.graphoman.types.Result
import kotlin.math.max

sealed class BinaryTree<out T : Comparable<@UnsafeVariance T>> {

    abstract val size: Int
    abstract val height: Int
    abstract fun isEmpty(): Boolean
    abstract fun max(): Result<T>
    abstract fun min(): Result<T>
    abstract fun merge(tree: BinaryTree<@UnsafeVariance T>): BinaryTree<T>

    abstract fun <A> foldLeft(
        identity: A,
        f: (A) -> (T) -> A,
        g: (A) -> (A) -> A
    ): A

    abstract fun <A> foldRight(
        identity: A,
        f: (T) -> (A) -> A,
        g: (A) -> (A) -> A
    ): A

    abstract fun <A> foldInOrder(identity: A, f: (A) -> (T) -> (A) -> A): A

    abstract fun <A> foldPreOrder(identity: A, f: (T) -> (A) -> (A) -> A): A

    abstract fun <A> foldPostOrder(identity: A, f: (A) -> (A) -> (T) -> A): A

    internal object Empty : BinaryTree<Nothing>() {
        override val size: Int = 0

        override val height: Int = -1

        override fun isEmpty(): Boolean = true

        override fun toString(): String = "E"

        override fun max(): Result<Nothing> = Result.empty()

        override fun min(): Result<Nothing> = Result.empty()

        override fun merge(tree: BinaryTree<Nothing>): BinaryTree<Nothing> = tree

        override fun <A> foldLeft(identity: A, f: (A) -> (Nothing) -> A, g: (A) -> (A) -> A): A = identity

        override fun <A> foldRight(identity: A, f: (Nothing) -> (A) -> A, g: (A) -> (A) -> A): A = identity

        override fun <A> foldInOrder(identity: A, f: (A) -> (Nothing) -> (A) -> A): A = identity

        override fun <A> foldPreOrder(identity: A, f: (Nothing) -> (A) -> (A) -> A): A = identity

        override fun <A> foldPostOrder(identity: A, f: (A) -> (A) -> (Nothing) -> A): A = identity

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

        override fun merge(tree: BinaryTree<@UnsafeVariance T>): BinaryTree<T> = when (tree) {
            Empty -> this
            is Node -> when {
                tree.value > this.value ->
                    Node(value, left, right.merge(Node(tree.value, Empty, tree.right)))
                        .merge(tree.left)
                tree.value < this.value ->
                    Node(value, left.merge(Node(tree.value, tree.left, Empty)), right)
                        .merge(tree.right)
                else -> Node(tree.value, left.merge(tree.left), right.merge(tree.right))
            }
        }

        override fun <A> foldLeft(identity: A, f: (A) -> (T) -> A, g: (A) -> (A) -> A): A =
            g(right.foldLeft(identity, f, g))(
                f(left.foldLeft(identity, f, g))
                    (this.value)
            )

        override fun <A> foldRight(identity: A, f: (T) -> (A) -> A, g: (A) -> (A) -> A): A =
            g(f(this.value)(left.foldRight(identity, f, g)))(right.foldRight(identity, f, g))

        override fun <A> foldInOrder(identity: A, f: (A) -> (T) -> (A) -> A): A =
            f(left.foldInOrder(identity, f))(value)(right.foldInOrder(identity, f))

        override fun <A> foldPreOrder(identity: A, f: (T) -> (A) -> (A) -> A): A =
            f(value)(left.foldPreOrder(identity, f))(right.foldPreOrder(identity, f))

        override fun <A> foldPostOrder(identity: A, f: (A) -> (A) -> (T) -> A): A =
            f(left.foldPostOrder(identity, f))(right.foldPostOrder(identity, f))(value)
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

    fun removeTree(tree: BinaryTree<@UnsafeVariance T>): BinaryTree<T> = when (this) {
        Empty -> tree
        is Node -> when (tree) {
            Empty -> this
            is Node -> when {
                tree.value < value -> Node(value, left.removeTree(tree), right)
                else -> Node(value, left, right.removeTree(tree))
            }
        }
    }

    fun printTree(level: Int = 0) {
        fun foo(tree: BinaryTree<@UnsafeVariance T>, level: Int) {
            if (tree !is Empty && tree is Node) {
                foo(tree.left, level + 1)
                println(" ".repeat(4 * level) + "-> ${tree.value}")
                foo(tree.right, level + 1)
            }
        }
        foo(this, level)
    }

    fun remove(element: @UnsafeVariance T): BinaryTree<T> = when (this) {
        Empty -> this
        is Node -> when {
            element < this.value -> Node(this.value, left.remove(element), right)
            element > this.value -> Node(this.value, left, right.remove(element))
            else -> left.removeTree(right)
        }
    }

    companion object {
        operator fun <T : Comparable<T>> invoke(vararg data: T): BinaryTree<T> =
            data.foldRight(Empty) { t: T, tree: BinaryTree<T> -> tree.plus(t) }
    }
}