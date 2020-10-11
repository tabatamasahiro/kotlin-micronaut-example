package com.example.tabata.domain

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BookTitleTest {

    @Test
    fun 出版まだ_タイトルを変更ByBlank() {

        var bookTitle = BookTitle.valueToUpdate("")

        var update = bookTitle.parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(update, Update.OK)
    }


    @Test
    fun 出版済だとタイトルを変更NG() {

        var bookTitle = BookTitle.valueToUpdate("シンニホン")

        var update = bookTitle.parseWithRelease(Release.ON_SALE)

        assertEquals(update, Update.NG)
        assertEquals(bookTitle.errorMsg, bookTitle.ERROR_MSG_001)
    }

    @Test
    fun 更新時_出版まだだとタイトル変更OK() {

        val newTitle = "shin nihon"

        var bookTitle = BookTitle.valueToUpdate(newTitle)

        var update = bookTitle.parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(update, Update.OK)
        assertEquals(bookTitle.title, newTitle)
    }


    @Test
    fun insert時のParse() {

        val newTitle = "Java"
        var bookTitle = BookTitle.valueToUpdate(newTitle)

        assertEquals(bookTitle.parseToSave(), Update.OK)
    }

    @Test
    fun insert時のParse2() {

        val newTitle = "Java2"
        var bookTitle = BookTitle.valueToUpdate(newTitle)

        assertEquals(bookTitle.parseToSave(), Update.OK)
    }

    @Test
    fun insert時のParse3() {

        val newTitle = "Java 2"
        var bookTitle = BookTitle.valueToUpdate(newTitle)

        assertEquals(bookTitle.parseToSave(), Update.OK)
    }

    @Test
    fun insert時のParseByBlank() {

        val newTitle = ""
        var bookTitle = BookTitle.valueToUpdate(newTitle)

        assertEquals(bookTitle.parseToSave(), Update.OK)
    }

}