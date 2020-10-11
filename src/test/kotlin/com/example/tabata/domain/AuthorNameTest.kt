package com.example.tabata.domain

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AuthorNameTest {

    @Test
    fun 更新() {

        var authorName = AuthorName("sasaki tarou")

        Assertions.assertEquals(authorName.parseToSave(), Update.OK)
    }

    @Test
    fun 更新NG() {

        var authorName = AuthorName.valueToUpdate("sasaki tarou-123")
        Assertions.assertEquals(authorName.parseToSave(), Update.NG)
        Assertions.assertEquals(authorName.errorMsg, authorName.ERROR_MSG_001)

    }

    @Test
    fun 更新NGByBlank() {

        var authorName = AuthorName.valueToUpdate("")
        Assertions.assertEquals(authorName.parseToSave(), Update.NG)
        Assertions.assertEquals(authorName.errorMsg, authorName.ERROR_MSG_001)

    }
}