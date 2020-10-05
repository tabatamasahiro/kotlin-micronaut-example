package com.example.tabata.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BookReleaseTest {

    @Test
    fun リリースする() {

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

}