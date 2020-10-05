package com.example.tabata.domain

class BookTitle(val title: String) {
    companion object {
        fun valueToUpdate(title: String): BookTitle {
            return BookTitle(title)
        }
    }

    fun parseToSave(): Update {
        return parseWithRelease(Release.NOT_ON_SALSE)
    }

    fun parseWithRelease(release: Release): Update {
        if (isAlready(release)) {
            return Update.NG
        }
        return Update.OK
    }

    fun isAlready(release: Release): Boolean {
        return release == Release.ON_SALE
    }

}