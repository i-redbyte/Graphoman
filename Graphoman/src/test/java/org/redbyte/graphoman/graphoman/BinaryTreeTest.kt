package org.redbyte.graphoman.graphoman

import org.junit.Assert.assertEquals
import org.junit.Test

class BinaryTreeTest {

    @Test
    fun addElement() {
        var tree = BinaryTree<Int>()+2
        assertEquals("Node(left = E value = 2 right = E)", tree.toString())
    }
}