package org.redbyte.graphoman.graphoman

import org.junit.Assert.assertEquals
import org.junit.Test

class BinaryTreeTest {

    @Test
    fun `add element`() {
        var tree = BinaryTree<Int>(2)
        println(tree)
        assertEquals("(Node E 2 E)", tree.toString())
        tree+=1
        tree+=3
        assertEquals("(Node (Node E 1 E) 2 (Node E 3 E))", tree.toString())
    }
}