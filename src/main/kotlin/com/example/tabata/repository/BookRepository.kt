package com.example.tabata.repository

import com.example.tabata.domain.Book
import io.micronaut.data.annotation.Id
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.time.LocalDate
import java.util.*

@JdbcRepository(dialect = Dialect.H2)
interface BookRepository : CrudRepository<Book, UUID> {

    override fun findAll(): List<Book>

    fun deleteByAuthorName(authorName: String)

    fun findByAuthorNameOrderByDatePublication(authorName: String): List<Book>

    fun findByTitleLike(title: String): List<Book>

//    fun findByTitle(title: String): List<Book>

    fun update(@Id isbn: UUID?, datePublication: LocalDate)

    fun update(@Id isbn: UUID?, title: String)

}