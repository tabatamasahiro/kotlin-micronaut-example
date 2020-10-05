package com.example.tabata.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BookReleaseTest {

    @Test
    fun 出版日あり_タイトル決定_販売OK() {

        var now = DatePublication.now()
        var datePublication = DatePublication.valueToUpdate(now.toString())
        var updateDateP = datePublication.parseWithRelease(Release.NOT_ON_SALSE)
        assertEquals(updateDateP, Update.OK)

        var bookTitle = BookTitle.valueToUpdate("シンニホン")
        var upadteBookTitle = bookTitle.parseWithRelease(Release.NOT_ON_SALSE)
        assertEquals(upadteBookTitle, Update.OK)

        var release = BookRelease.checkToDoRelase(datePublication, bookTitle)
        assertEquals(release, Release.ON_SALE)
    }

    @Test
    fun 販売NG_出版日未定の場合() {

        var datePublication = DatePublication.valueToUpdate("")
        assertEquals(datePublication.parseToSave(), Update.OK)

        var bookTitle = BookTitle.valueToUpdate("シンニホン")
        var upadteBookTitle = bookTitle.parseWithRelease(Release.NOT_ON_SALSE)
        assertEquals(upadteBookTitle, Update.OK)

        var release = BookRelease.checkToDoRelase(datePublication, bookTitle)

        assertEquals(release, Release.NOT_ON_SALSE)
    }

    @Test
    fun 販売NG_タイトル未定の場合() {

        var plus2day = DatePublication.now().plusDays(2).toString()
        var datePublication = DatePublication.valueToUpdate(plus2day)
        assertEquals(datePublication.parseToSave(), Update.OK)

        var bookTitle = BookTitle.valueToUpdate("")
        var upadteBookTitle = bookTitle.parseToSave()
        assertEquals(upadteBookTitle, Update.OK)

        var release = BookRelease.checkToDoRelase(datePublication, bookTitle)

        assertEquals(release, Release.NOT_ON_SALSE)
    }
}