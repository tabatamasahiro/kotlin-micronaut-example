package com.example.tabata.domain

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SalesDateTest {

    @Test
    fun 現在は過去ではない() {

        var now = SalesDate.now()

        println(now)

        assertFalse(SalesDate.isPastDate(now))
    }

    @Test
    fun 現在minus1は過去() {

        var now = SalesDate.now()
        var minus1Days = now.minusDays(1)

        println(minus1Days)

        assertTrue(SalesDate.isPastDate(minus1Days))
    }

    @Test
    fun 現在Plus1は過去日ではない() {

        var now = SalesDate.now()
        var plusDays1 = now.plusDays(1)

        println(plusDays1)

        assertFalse(SalesDate.isPastDate(plusDays1))
    }


    @Test
    fun 未出版で出版日は現在日の場合UpdateOK() {

        var now = SalesDate.now().toString()

        var parseByRelease = SalesDate.valueToUpdate(now).parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(parseByRelease, Update.OK)
    }

    @Test
    fun 未出版で出版日は現在日以降の場合UpdateOK() {

        var plus1Days = SalesDate.now().plusDays(1).toString()

        var parseByRelease =
                SalesDate.valueToUpdate(plus1Days).parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(parseByRelease, Update.OK)
    }


    @Test
    fun 未出版で出版日は過去日の場合UpdateNG() {

        var minusDay1 = SalesDate.now().minusDays(1)

        var salesDate = SalesDate.valueToUpdate(minusDay1.toString())

        var parseByRelease = salesDate.parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(parseByRelease, Update.NG)
        assertEquals(salesDate.errorMsg, salesDate.ERROR_MSG_003)
    }

    @Test
    fun 出版済の場合はUpdateNG() {
        var now = SalesDate.now().toString()

        var salesDate = SalesDate.valueToUpdate(now)
        var parseByRelease = salesDate.parseWithRelease(Release.ON_SALE)

        assertEquals(parseByRelease, Update.NG)
        assertEquals(salesDate.errorMsg, salesDate.ERROR_MSG_001)
    }

    @Test
    fun 出版日の書式エラー() {
        var now = SalesDate.now().toString()

        var salesDate = SalesDate.valueToUpdate("2020-02-30")
        var parseByRelease = salesDate.parseToSave()

        assertEquals(parseByRelease, Update.NG)
        assertEquals(salesDate.errorMsg, salesDate.ERROR_MSG_002)
    }

    @Test
    fun insert時の解析() {
        var now = SalesDate.now().toString()
        assertEquals(SalesDate.valueToUpdate(now.toString()).parseToSave(), Update.OK)
    }
}