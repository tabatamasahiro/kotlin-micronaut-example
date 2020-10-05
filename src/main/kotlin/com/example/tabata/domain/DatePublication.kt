package com.example.tabata.domain

import java.time.DateTimeException
import java.time.LocalDate
import java.time.ZoneId

class DatePublication(val yyyymmdd: String) {
    companion object {
        fun valueToUpdate(yyyymmdd: String): DatePublication {
            return DatePublication(yyyymmdd)
        }

        fun isPastDate(yyyymmdd: LocalDate): Boolean {
            return now().isAfter(yyyymmdd)
        }

        fun now() = LocalDate.now(ZoneId.of("Asia/Tokyo"))
    }

    fun parseWithRelease(release: Release): Update {

        if (isAlready(release)) {
            // 出版済:出版日更新NG
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
            return Update.NG
        }
    }

    fun isAlready(release: Release): Boolean {
        return release == Release.DONE
    }

    fun undecided(): Boolean {
        return yyyymmdd.isNullOrBlank()
    }

    fun parse(): Update {
        if (isPastDate(LocalDate.parse(yyyymmdd))) {
            //昨日以前の日付へ変更する事はできないためNG
            return Update.NG
        } else {
            return Update.OK
        }
    }

}