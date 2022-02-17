package org.redbyte.graphoman.graphoman.types

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ResultTest {

    @Test
    fun `Create empty result and assign a value`() {
        var r = Result<Int>()
        assertEquals(Result.Empty, r)
        r = Result(123)
        assertNotEquals(Result.Empty, r)
    }

    @Test
    fun `Get fail value`() {
        assertEquals("Failure(My message)", Result.failure<Any>("My message").toString())
    }

    @Test
    fun `Result get or else`() {
        val resultOk = Result("Kotlin")
        assertEquals("Kotlin", resultOk.getOrElse("C++"))
        val resultFail = Result.failure<Int>("Fail")
        assertEquals(1123, resultFail.getOrElse(1123))
    }

    @Test
    fun `Result get or null`() {
        val resultOk = Result("Kotlin")
        assertEquals("Kotlin", resultOk.getOrNull())
        val resultFail = Result.failure<Int>("Fail")
        assertEquals(null, resultFail.getOrNull())
    }

    @Test
    fun `Filter data`() {
        val r = Result("C++")
        assertEquals("C++", r.filter { it == "C++" }.getOrNull())
        assertNotEquals("C++", r.filter { it != "C++" }.getOrNull())
        assertEquals("Failure(Not found)", r.filter("Not found") { it == "Go" }.toString())
    }

    @Test
    fun `lift (A) - B`() {
        val r = Result.lift<Int, CharSequence> { "$it C++"}
        val result = Result(31)
        assertEquals("31 C++", r(result).getOrNull())
    }
}