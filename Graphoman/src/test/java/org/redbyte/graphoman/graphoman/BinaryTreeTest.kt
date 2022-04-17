package org.redbyte.graphoman.graphoman

import org.junit.Assert.*
import org.junit.Test
import org.redbyte.graphoman.graphoman.struct.graph.tree.BinaryTree

class BinaryTreeTest {
    var testTree = BinaryTree(50)
    var treeA = BinaryTree(3)
    var treeB = BinaryTree(3)

    init {
        treeA += 3
        treeA += 2
        treeA += 1
        treeB += 5
        treeB += 4
        treeB += 6
        testTree += 45
        testTree += 65
        testTree += 15
        testTree += 75
        testTree += 25
        testTree += 85
        testTree += 0
    }

    @Test
    fun `print tree`() {
        BinaryTree(-1, 2, 1).printTree()
        println("==========")
        BinaryTree(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1).printTree()
        println("==========")
        BinaryTree(6, 5, 3, 2, 1).printTree()
        println("==========")
        BinaryTree(100, 50, 70, 40).printTree()
        println("==========")
        BinaryTree("C++", "Java", "C", "Kotlin", "Go", "Assembler", "Rust", "Python", "Haskell").printTree()
        println("==========")
        testTree.printTree(3)
    }

    @Test
    fun `add element`() {
        var tree = BinaryTree(2)
        assertEquals("(Node E 2 E)", tree.toString())
        tree += 1
        tree += 3
        assertEquals("(Node (Node E 1 E) 2 (Node E 3 E))", tree.toString())
    }

    @Test
    fun `contains element`() {
        assertTrue(testTree.contains(45))
        assertTrue(testTree.contains(85))
        assertTrue(testTree.contains(0))
        assertTrue(testTree.contains(50))
        assertFalse(testTree.contains(10))
        assertFalse(testTree.contains(55))
        assertFalse(testTree.contains(20))
        assertFalse(testTree.contains(40))
    }

    @Test
    fun `count element in three`() {
        assertEquals(8, testTree.size)
    }

    @Test
    fun `height three`() {
        assertEquals(3, testTree.height)
    }

    @Test
    fun `get min value`() {
        assertEquals(0, testTree.min().getOrElse(Int.MAX_VALUE))
    }

    @Test
    fun `get max value`() {
        assertEquals(85, testTree.max().getOrNull())
    }

    @Test
    fun `remove value from tree (right)`() {
        testTree = testTree.remove(85)
        assertEquals(7, testTree.size)
        assertEquals(
            "(Node (Node (Node (Node E 0 E) 15 (Node E 25 E)) 45 E) 50 (Node E 65 (Node E 75 E)))",
            testTree.toString()
        )
    }

    @Test
    fun `remove value from tree (left)`() {
        testTree = testTree.remove(0)
        assertEquals(7, testTree.size)
        assertEquals(
            "(Node (Node (Node E 15 (Node E 25 E)) 45 E) 50 (Node E 65 (Node E 75 (Node E 85 E))))",
            testTree.toString()
        )
    }

    @Test
    fun `merge trees`() {
        val merge = treeA.merge(treeB)
        assertEquals(merge.contains(6), true)
        assertEquals(merge.contains(1), true)
        assertEquals(merge.contains(88), false)
        assertEquals(treeA.merge(treeB).toString(), treeB.merge(treeA).toString())
        assertEquals(BinaryTree.Empty.merge(BinaryTree.Empty), BinaryTree.Empty)
        assertEquals(treeB.toString(), treeB.merge(BinaryTree.Empty).toString())
    }

    @Test
    fun `left fold tree`() {
        val result = BinaryTree(4, 2, 6, 1, 3, 5, 7)
            .foldLeft(
                listOf(),
                { list: List<Int> ->
                    { value: Int ->
                        list + value
                    }
                })
            { x -> { y -> x + y } }.reversed()
        assertEquals(result, listOf(7, 5, 3, 1, 2, 4, 6))
        assertNotEquals(result, listOf(1, 2, 3, 4, 5, 6, 7))
        val emptyResult = BinaryTree.Empty.foldLeft(listOf(),
            { list: List<Int> ->
                { value: Int ->
                    list + value
                }
            })
        { x -> { y -> x + y } }
        assertNotEquals(emptyResult, listOf(1, 2, 3, 4, 5, 6, 7))
        assertEquals(emptyResult, emptyList<Int>())
    }

    @Test
    fun `right fold tree`() {
        val result = BinaryTree(4, 2, 6, 1, 3, 5, 7)
            .foldRight(
                listOf(),
                { value: Int ->
                    { list: List<Int> ->
                        list + value
                    }
                })
            { x -> { y -> x + y } }.reversed()
        assertEquals(listOf(7, 6, 5, 4, 3, 2, 1), result)
        val emptyResult = BinaryTree.Empty.foldRight(listOf(),
            { value: Int ->
                { list: List<Int> ->
                    list + value
                }
            })
        { x -> { y -> x + y } }
        assertNotEquals(emptyResult, listOf(1, 2, 3, 4, 5, 6, 7))
        assertEquals(emptyResult, emptyList<Int>())
    }
}