package com.example.tabata.domain

class BookTitle(val title: String) {
    companion object {
        fun valueToUpdate(title: String): BookTitle {
            return BookTitle(title)
        }
    }

    fun parseWithRelease(release: Release): Update {
        if (isAlready(release)) {
            return Update.NG
        }
        return Update.OK
    }

    fun isAlready(release: Release): Boolean {
        return release == Release.DONE
    }

}