package com.example.tabata.domain

import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId

class DatePublication(val yyyymmdd: String?) {

    var localDate: LocalDate? = null
    var errorMsg: String = ""
    val ERROR_MSG_001: String = "この書籍は出版済のため出版日は変更できません"
    val ERROR_MSG_002: String = "出版日の入力に誤りがあります"
    val ERROR_MSG_003: String = "出版日は過去日付に変更する事はできません"

    companion object {
        fun valueToUpdate(yyyymmdd: String?): DatePublication {
            return DatePublication(yyyymmdd)
        }

        fun isPastDate(yyyymmdd: LocalDate?): Boolean {
            return now().isAfter(yyyymmdd)
        }

        fun now() = LocalDate.now(ZoneId.of("Asia/Tokyo"))
    }


    fun parseToSave(): Update {
        return parseWithRelease(Release.NOT_ON_SALSE)
    }

    fun parseWithRelease(release: Release): Update {

        if (isAlready(release)) {
            // 出版済:出版日更新NG
            errorMsg = ERROR_MSG_001
            return Update.NG
        }

        // 出版まだ
        if (undecided()) {
            // 出版日:未定
            return Update.OK
        }

        try {
            return parse()
        } catch (e: DateTimeException) {
            errorMsg = ERROR_MSG_002
            return Update.NG
        }
    }

    fun isAlready(release: Release): Boolean {
        return release == Release.ON_SALE
    }

    fun undecided(): Boolean {
        return yyyymmdd.isNullOrBlank()
    }


    fun parse(): Update {

        this.localDate = LocalDate.parse(yyyymmdd)

        if (isPastDate(localDate)) {
            //昨日以前の日付へ変更する事はできないためNG
            errorMsg = ERROR_MSG_003
            return Update.NG
        } else {
            return Update.OK
        }
    }

}