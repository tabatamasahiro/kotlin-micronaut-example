package com.example.tabata.domain

class BookTitle(val title: String) {
    companion object {
        fun valueToUpdate(title: String): BookTitle {
            return BookTitle(title)
        }
    }

    fun parseWithRelease(release: Book.Release): Update {
        if (isAlready(release)) {
            return Update.NG
        }
        return Update.OK
    }

    fun isAlready(release: Book.Release): Boolean {
        return release == Book.Release.DONE
    }

}