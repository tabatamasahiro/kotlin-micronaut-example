package com.example.tabata.repository

import com.example.tabata.domain.Book
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

@MicronautTest
class BookRepositoryTest(private val bookRepository: BookRepository) {

    @Test
    fun 著者名で検索() {

        var books = bookRepository.findByAuthorNameOrderByDatePublication("安宅和人")
        Assertions.assertEquals(1, books.size)
        var book = books.get(0)

        Assertions.assertEquals("安宅和人", book.authorName)
        Assertions.assertEquals("issue driven", book.title)
        Assertions.assertEquals(Book.Release.DONE, book.release)
        Assertions.assertEquals(
                LocalDate.of(2010, 11, 24),
                book.datePublication)

    }


    fun findBy尾田栄一郎(): Book {
        var books = bookRepository.findByAuthorNameOrderByDatePublication("尾田栄一郎")
        Assertions.assertEquals(1, books.size)
        return books.get(0)
    }

    @Test
    fun insert_タイトルだけ未定() {

        var datePublication = LocalDate.of(2010, 11, 24)

        var newBook = Book(UUID.randomUUID(), "尾田栄一郎", null,
                Book.Release.NOT_DONE, datePublication)

        bookRepository.save(newBook)

        val book = findBy尾田栄一郎()

        println(book)

        Assertions.assertEquals(newBook.authorName, book.authorName)
        Assertions.assertNull(book.title)
        Assertions.assertEquals(newBook.release, book.release)
        Assertions.assertEquals(newBook.datePublication, datePublication)
    }

    @Test
    fun insert_出版日だけ未定() {

        var newBook = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース110巻",
                Book.Release.NOT_DONE, null)

        bookRepository.save(newBook)

        val book = findBy尾田栄一郎()

        Assertions.assertEquals(newBook.authorName, book.authorName)
        Assertions.assertEquals(newBook.title, book.title)
        Assertions.assertEquals(newBook.release, book.release)
        Assertions.assertNull(book.datePublication)
    }

    @Test
    fun 出版日未登録から登録へ変更() {

        insert_出版日だけ未定()

        val book = findBy尾田栄一郎()

        var yyyyMMdd = LocalDate.of(2024, 11, 29)
        bookRepository.update(book.isbn, yyyyMMdd)

        val updateBook = findBy尾田栄一郎()

        Assertions.assertEquals(updateBook.authorName, book.authorName)
        Assertions.assertEquals(updateBook.title, book.title)
        Assertions.assertEquals(updateBook.release, book.release)
        Assertions.assertEquals(yyyyMMdd.toString(), updateBook.datePublication.toString())
    }

    @Test
    fun タイトル変更() {
        insert_出版日だけ未定()

        val book = findBy尾田栄一郎()

        val newTitle = "ワンピース100巻"

        bookRepository.update(book.isbn, newTitle)

        val updateBook = findBy尾田栄一郎()

        println(updateBook)

        Assertions.assertEquals(updateBook.authorName, book.authorName)
        Assertions.assertEquals(updateBook.title, newTitle)
        Assertions.assertEquals(updateBook.release, book.release)
        Assertions.assertNull(updateBook.datePublication)
    }

    @Test
    fun 著者に紐づく書籍をすべて取得() {
        var op1 = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース1巻",
                Book.Release.DONE, LocalDate.of(1997, 12, 29))
        var op20 = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース20巻",
                Book.Release.DONE, LocalDate.of(2001, 9, 9))
        var op35 = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース35巻",
                Book.Release.DONE, LocalDate.of(2004, 11, 9))

        bookRepository.saveAll(listOf(op1, op20, op35))

        var books = bookRepository.findByAuthorNameOrderByDatePublication("尾田栄一郎")
        Assertions.assertEquals(3, books.size)

        var booksOrigin = listOf<Book>(op1, op20, op35).sortedBy { book -> book.datePublication }

        var index = 0;

        for (book in books) {
//            println(book)
//            println(booksOrigin.get(index))
            Assertions.assertEquals(booksOrigin.get(index).authorName, book.authorName)
            Assertions.assertEquals(booksOrigin.get(index).title, book.title)
            Assertions.assertEquals(booksOrigin.get(index).release, book.release)
            Assertions.assertEquals(booksOrigin.get(index).datePublication, book.datePublication)
            ++index
        }
    }

    @Test
    fun deleteById() {

        著者に紐づく書籍をすべて取得()
        bookRepository.deleteByAuthorName("尾田栄一郎")

        var books = bookRepository.findByAuthorNameOrderByDatePublication("尾田栄一郎")
        Assertions.assertEquals(0, books.size)

        for (book in bookRepository.findAll()) {
            println(book)
        }
    }
}