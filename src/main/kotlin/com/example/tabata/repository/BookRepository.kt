package com.example.tabata.repository

import com.example.tabata.domain.Book
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*

@JdbcRepository(dialect = Dialect.H2)
interface BookRepository : CrudRepository<Book, UUID> {

    override fun findAll(): List<Book>

    fun findByAuthorName(authorName: String): Optional<Book>

}