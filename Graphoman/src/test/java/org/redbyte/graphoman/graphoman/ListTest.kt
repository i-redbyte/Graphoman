package org.redbyte.graphoman.graphoman

import org.junit.Assert.assertEquals
import org.junit.Test
import org.redbyte.graphoman.graphoman.struct.List

class ListTest {
    @Test
    fun `add value to head list`() {
        var list = List(1)
        assertEquals("[1, NIL]", list.toString())
        list = list.addToHead(2)
        assertEquals("[2, 1, NIL]", list.toString())
        list = list.addToHead(1917)
        assertEquals("[1917, 2, 1, NIL]", list.toString())
    }
}