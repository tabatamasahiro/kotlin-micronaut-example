package com.example.tabata.domain

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BookTitleTest {

    @Test
    fun 出版済だとタイトルを変更NG() {

        var update =
                BookTitle.valueToUpdate("シンニホン").parseWithRelease(Release.ON_SALE)

        assertEquals(update, Update.NG)
    }

    @Test
    fun 出版まだだとタイトル変更OK() {

        val newTitle = "シンニホン"

        var bookTitle = BookTitle.valueToUpdate(newTitle)

        var update = bookTitle.parseWithRelease(Release.NOT_ON_SALSE)

        assertEquals(update, Update.OK)
        assertEquals(bookTitle.title, newTitle)
    }

    @Test
    fun insert時のParse() {

        val newTitle = "シンニホン"

        var bookTitle = BookTitle.valueToUpdate(newTitle)

        assertEquals(bookTitle.parseToSave(), Update.OK)
    }
}