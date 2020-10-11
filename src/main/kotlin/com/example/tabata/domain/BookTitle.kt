package com.example.tabata.domain

class BookTitle(val title: String) {

    var errorMsg: String = ""
    val ERROR_MSG_001: String = "この書籍は出版済のためタイトルの変更はできません"
    val ERROR_MSG_002: String = "著者名は英数字のみです"

    companion object {
        fun valueToUpdate(title: String?): BookTitle {
            if (title.isNullOrEmpty()) {
                return BookTitle("")
            }
            return BookTitle(title)
        }
    }

    fun parseToSave(): Update {
        return parseWithRelease(Release.NOT_ON_SALSE)
    }

    fun parseWithRelease(release: Release): Update {
        if (isAlready(release)) {
            errorMsg = ERROR_MSG_001
            return Update.NG
        }

        if (title.isNullOrBlank()) {
            return Update.OK
        }

        return verifyFormat()
    }

    fun isAlready(release: Release): Boolean {
        return release == Release.ON_SALE
    }

    fun verifyFormat(): Update {
        println("ok")
        var regex = Regex("^[0-9a-zA-Z ]+$")

        if (regex.containsMatchIn(title)) {
            return Update.OK
        } else {
            errorMsg = ERROR_MSG_002
            return Update.NG
        }

    }

}