package com.example.tabata.domain

/**
 * Title と DatePublication が SET された状態で DONE になる事ができる
 */
class BookRelease() {
    companion object {
        fun checkToDoRelase(datePublication: DatePublication,
                            bookTitle: BookTitle): Release {

            if (datePublication.yyyymmdd.isNullOrBlank()) {
                return Release.NOT_ON_SALSE
            }

            if (bookTitle.title.isNullOrBlank()) {
                return Release.NOT_ON_SALSE
            }

            return Release.ON_SALE
        }
    }

}