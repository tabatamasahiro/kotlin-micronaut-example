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

        var parseByRelease = DatePublication.valueToUpdate(now).parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(parseByRelease, Update.OK)
    }

    @Test
    fun 未出版で出版日は現在日以降の場合UpdateOK() {

        var plus1Days = DatePublication.now().plusDays(1).toString()

        var parseByRelease =
                DatePublication.valueToUpdate(plus1Days).parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(parseByRelease, Update.OK)
    }


    @Test
    fun 未出版で出版日は過去日の場合UpdateNG() {

        var minusDay1 = DatePublication.now().minusDays(1)

        var datePublication = DatePublication.valueToUpdate(minusDay1.toString())

        var parseByRelease = datePublication.parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(parseByRelease, Update.NG)
        assertEquals(datePublication.errorMsg, datePublication.ERROR_MSG_003)
    }

    @Test
    fun 出版済の場合はUpdateNG() {
        var now = DatePublication.now().toString()

        var datePublication = DatePublication.valueToUpdate(now)
        var parseByRelease = datePublication.parseWithRelease(Release.ON_SALE)

        assertEquals(parseByRelease, Update.NG)
        assertEquals(datePublication.errorMsg, datePublication.ERROR_MSG_001)
    }

    @Test
    fun 出版日の書式エラー() {
        var now = DatePublication.now().toString()

        var datePublication = DatePublication.valueToUpdate("2020-02-30")
        var parseByRelease = datePublication.parseToSave()

        assertEquals(parseByRelease, Update.NG)
        assertEquals(datePublication.errorMsg, datePublication.ERROR_MSG_002)
    }

    @Test
    fun insert時の解析() {
        var now = DatePublication.now().toString()
        assertEquals(DatePublication.valueToUpdate(now.toString()).parseToSave(), Update.OK)
    }
}