package com.example.tabata.domain

class BookTitle(val title: String?) {

    var errorMsg: String = ""
    val ERROR_MSG_001: String = "この書籍は出版済のためタイトルの変更はできません"

    companion object {
        fun valueToUpdate(title: String?): BookTitle {
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
        return Update.OK
    }

    fun isAlready(release: Release): Boolean {
        return release == Release.ON_SALE
    }

}