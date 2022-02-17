package org.redbyte.graphoman.graphoman

import org.junit.Assert.*
import org.junit.Test
import org.redbyte.graphoman.graphoman.struct.graph.tree.BinaryTree

class BinaryTreeTest {
    var testTree = BinaryTree(50)

    init {
        testTree += 45
        testTree += 65
        testTree += 15
        testTree += 75
        testTree += 25
        testTree += 85
        testTree += 0
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
        println()
        println(testTree)
        println()
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
        println("tree1 =$testTree")
        testTree = testTree.remove(85)
        assertEquals(7, testTree.size)
        assertEquals(
            "(Node (Node (Node (Node E 0 E) 15 (Node E 25 E)) 45 E) 50 (Node E 65 (Node E 75 E)))",
            testTree.toString()
        )
        println("tree2 =$testTree")
    }

    @Test
    fun `remove value from tree (left)`() {
        println("tree1 =$testTree")
        testTree = testTree.remove(0)
        assertEquals(7, testTree.size)
        assertEquals(
            "(Node (Node (Node E 15 (Node E 25 E)) 45 E) 50 (Node E 65 (Node E 75 (Node E 85 E))))",
            testTree.toString()
        )
        println("tree2 =$testTree")
    }

}