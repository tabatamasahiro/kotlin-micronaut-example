package com.example.tabata.domain

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DatePublicationTest {

    @Test
    fun 現在は過去ではない() {

        var now = DatePublication.now()

        println(now)

        assertFalse(DatePublication.isPastDate(now))
    }

    @Test
    fun 現在minus1は過去() {

        var now = DatePublication.now()
        var minus1Days = now.minusDays(1)

        println(minus1Days)

        assertTrue(DatePublication.isPastDate(minus1Days))
    }

    @Test
    fun 現在Plus1は過去日ではない() {

        var now = DatePublication.now()
        var plusDays1 = now.plusDays(1)

        println(plusDays1)

        assertFalse(DatePublication.isPastDate(plusDays1))
    }


    @Test
    fun 未出版で出版日は現在日の場合UpdateOK() {

        var now = DatePublication.now().toString()

        var parseByRelease = DatePublication.valueToUpdate(now).parseWithRelease(Book.Release.NOT_DONE)

        assertEquals(parseByRelease, DatePublication.Update.OK)
    }

    @Test
    fun 未出版で出版日は現在日以降の場合UpdateOK() {

        var plus1Days = DatePublication.now().plusDays(1).toString()

        var parseByRelease =
                DatePublication.valueToUpdate(plus1Days).parseWithRelease(Book.Release.NOT_DONE)

        assertEquals(parseByRelease, DatePublication.Update.OK)
    }


    @Test
    fun 未出版で出版日は過去日の場合UpdateNG() {

        var minusDay1 = DatePublication.now().minusDays(1)

        var parseByRelease =
                DatePublication.valueToUpdate(minusDay1.toString()).parseWithRelease(Book.Release.NOT_DONE)

        assertEquals(parseByRelease, DatePublication.Update.NG)
    }

    @Test
    fun 出版済の場合はUpdateNG() {
        var now = DatePublication.now().toString()

        var parseByRelease = DatePublication.valueToUpdate(now).parseWithRelease(Book.Release.DONE)

        assertEquals(parseByRelease, DatePublication.Update.NG)
    }
}