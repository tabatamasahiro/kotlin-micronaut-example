package com.example.tabata.controller

import io.micronaut.core.annotation.Introspected

@Introspected
data class BookForm(
        var author_name: String,
        var title: String?,
        var yyyymmdd: String?
)