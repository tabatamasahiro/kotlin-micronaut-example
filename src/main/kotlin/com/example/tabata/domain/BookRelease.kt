package com.example.tabata.domain

/**
 * Title と salesDate が SET された状態で DONE になる事ができる
 */
class BookRelease() {
    companion object {
        fun checkToDoRelase(salesDate: SalesDate,
                            bookTitle: BookTitle): Release {

            if (salesDate.yyyymmdd.isNullOrBlank()) {
                return Release.NOT_ON_SALSE
            }

            if (bookTitle.title.isNullOrBlank()) {
                return Release.NOT_ON_SALSE
            }

            return Release.ON_SALE
        }
    }

}