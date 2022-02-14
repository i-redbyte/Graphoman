package org.redbyte.graphoman.graphoman

import org.junit.Assert.*
import org.junit.Test

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
        assertEquals(8, testTree.size())
    }

    @Test
    fun `height three`() {
        assertEquals(3, testTree.height())
    }

}