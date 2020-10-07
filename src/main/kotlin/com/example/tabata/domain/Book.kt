package com.example.tabata.domain

import io.micronaut.data.annotation.AutoPopulated
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
data class Book(
        @Id
        @AutoPopulated
        var isbn: UUID?,
        val authorName: String,
        val title: String?,
        val release: Release = Release.NOT_ON_SALSE,
        val salesDate: LocalDate?
) {

}