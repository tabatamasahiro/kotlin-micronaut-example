package com.example.tabata.domain

import com.example.tabata.repository.BookRepository
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@MicronautTest
internal class BookServiceTest(val bookRepository: BookRepository) {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    fun 著者書籍を3件INSERT(): List<Book> {
        val op1 = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース1巻",
                Release.ON_SALE, LocalDate.of(1997, 12, 29))
        val op20 = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース20巻",
                Release.ON_SALE, LocalDate.of(2001, 9, 9))
        val op35 = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース35巻",
                Release.ON_SALE, LocalDate.of(2004, 11, 9))

        val newBooks = listOf(op1, op20, op35)

        bookRepository.saveAll(newBooks)

        return newBooks
    }

    @Test
    fun searchAndUpdateReleaseStatus() {

        著者書籍を3件INSERT()

        var newBook = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース97巻",
                Release.NOT_ON_SALSE, LocalDate.of(2020, 9, 16))
        bookRepository.save(newBook)

        var bookService = BookService(bookRepository)

        var books = bookService.searchAndUpdateReleaseStatus("", "ワンピース")

        var findFirst = books.stream().filter { book -> book.title.equals(newBook.title) }.findFirst()

        if (findFirst.isPresent) {
            var book = findFirst.get()
            println(book)
            Assertions.assertEquals(book.isbn.toString(), newBook.isbn.toString())
            Assertions.assertEquals(book.authorName, newBook.authorName)
            Assertions.assertEquals(book.title, newBook.title)
            Assertions.assertEquals(book.salesDate, newBook.salesDate)
            Assertions.assertEquals(book.release, Release.ON_SALE)
        } else {
            Assertions.assertTrue(false)
        }
    }
}