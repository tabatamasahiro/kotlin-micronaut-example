package com.example.tabata.repository

import com.example.tabata.domain.Book
import com.example.tabata.domain.Release
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*

@MicronautTest
class BookRepositoryTest(private val bookRepository: BookRepository) {

    @Test
    fun 著者名で検索() {

        var books = bookRepository.findByAuthorNameOrderBySalesDate("安宅和人")
        Assertions.assertEquals(1, books.size)
        var book = books.get(0)

        Assertions.assertEquals("安宅和人", book.authorName)
        Assertions.assertEquals("issue driven", book.title)
        Assertions.assertEquals(Release.ON_SALE, book.release)
        Assertions.assertEquals(
                LocalDate.of(2010, 11, 24),
                book.salesDate)

    }


    fun findBy尾田栄一郎(): Book {
        var books = bookRepository.findByAuthorNameOrderBySalesDate("尾田栄一郎")
        Assertions.assertEquals(1, books.size)
        return books.get(0)
    }

    @Test
    fun insert_タイトルだけ未定() {

        var salesDate = LocalDate.of(2010, 11, 24)

        var newBook = Book(UUID.randomUUID(), "尾田栄一郎", null,
                Release.NOT_ON_SALSE, salesDate)

        bookRepository.save(newBook)

        val book = findBy尾田栄一郎()

        println(book)

        Assertions.assertEquals(newBook.authorName, book.authorName)
        Assertions.assertNull(book.title)
        Assertions.assertEquals(newBook.release, book.release)
        Assertions.assertEquals(newBook.salesDate, salesDate)
    }

    @Test
    fun insert_出版日だけ未定() {

        var newBook = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース110巻",
                Release.NOT_ON_SALSE, null)

        bookRepository.save(newBook)

        val book = findBy尾田栄一郎()

        Assertions.assertEquals(newBook.authorName, book.authorName)
        Assertions.assertEquals(newBook.title, book.title)
        Assertions.assertEquals(newBook.release, book.release)
        Assertions.assertNull(book.salesDate)
    }

    @Test
    fun 出版日未登録から登録へ変更() {

        insert_出版日だけ未定()

        val book = findBy尾田栄一郎()

        var salesDate = LocalDate.of(2024, 11, 29)
        bookRepository.update(book.isbn, salesDate)

        val updateBook = findBy尾田栄一郎()

        Assertions.assertEquals(updateBook.authorName, book.authorName)
        Assertions.assertEquals(updateBook.title, book.title)
        Assertions.assertEquals(updateBook.release, book.release)
        Assertions.assertEquals(salesDate.toString(), updateBook.salesDate.toString())
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
        Assertions.assertNull(updateBook.salesDate)
    }

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
    fun 著者に紐づく書籍をすべて取得() {

        val newBooks = 著者書籍を3件INSERT()

        val books = bookRepository.findByAuthorNameOrderBySalesDate("尾田栄一郎")
        Assertions.assertEquals(3, books.size)

        val booksOrigin = newBooks.sortedBy { book -> book.salesDate }

        var index = 0;

        for (book in books) {
//            println(book)
//            println(booksOrigin.get(index))
            Assertions.assertEquals(booksOrigin.get(index).authorName, book.authorName)
            Assertions.assertEquals(booksOrigin.get(index).title, book.title)
            Assertions.assertEquals(booksOrigin.get(index).release, book.release)
            Assertions.assertEquals(booksOrigin.get(index).salesDate, book.salesDate)
            ++index
        }
    }

    @Test
    fun deleteById() {

        著者に紐づく書籍をすべて取得()
        bookRepository.deleteByAuthorName("尾田栄一郎")

        var books = bookRepository.findByTitleLike("尾田栄一郎")
        Assertions.assertEquals(0, books.size)

        for (book in bookRepository.findAll()) {
            println(book)
        }
    }

    @Test
    fun タイトルによる書籍検索_結果1件() {

        著者書籍を3件INSERT()

        var books = bookRepository.findByTitleLike("ワンピース20巻")

        Assertions.assertEquals(books.size, 1)
        Assertions.assertEquals(books.get(0).authorName, "尾田栄一郎")
        Assertions.assertEquals(books.get(0).title, "ワンピース20巻")
        Assertions.assertEquals(books.get(0).salesDate, LocalDate.of(2001, 9, 9))

    }

    @Test
    fun タイトルによる書籍検索_前方一致_結果3件() {

        var newBooks = 著者書籍を3件INSERT()

//        var books = bookRepository.findByTitle("ワンピース%")
        var books = bookRepository.findByTitleLike("ワンピース%")

        Assertions.assertEquals(books.size, 3)

        newBooks.sortedBy { book -> book.salesDate }
        books.sortedBy { book -> book.salesDate }

        var index = 0;

        for (book in books) {
            Assertions.assertEquals(newBooks.get(index).authorName, book.authorName)
            Assertions.assertEquals(newBooks.get(index).title, book.title)
            Assertions.assertEquals(newBooks.get(index).release, book.release)
            Assertions.assertEquals(newBooks.get(index).salesDate, book.salesDate)
            ++index
        }
    }


    @Test
    fun タイトルによる書籍検索_後方一致_結果3件() {

        var newBooks = 著者書籍を3件INSERT()

        var books = bookRepository.findByTitleLike("%巻")

        Assertions.assertEquals(books.size, 3)

        newBooks.sortedBy { book -> book.salesDate }
        books.sortedBy { book -> book.salesDate }

        var index = 0;

        for (book in books) {
            Assertions.assertEquals(newBooks.get(index).authorName, book.authorName)
            Assertions.assertEquals(newBooks.get(index).title, book.title)
            Assertions.assertEquals(newBooks.get(index).release, book.release)
            Assertions.assertEquals(newBooks.get(index).salesDate, book.salesDate)
            ++index
        }
    }

    @Test
    fun タイトルによる書籍検索_あいまい_結果2件() {

        var newBooks = 著者書籍を3件INSERT()

        val op22 = Book(UUID.randomUUID(), "尾田栄一郎", "ワンピース22巻",
                Release.ON_SALE, LocalDate.of(2002, 2, 9))

        bookRepository.save(op22)

        var books = bookRepository.findByTitleLike("%ピース2%")

        Assertions.assertEquals(books.size, 2)

        books.sortedBy { book -> book.salesDate }

        Assertions.assertEquals(books.get(0).authorName, "尾田栄一郎")
        Assertions.assertEquals(books.get(0).title, "ワンピース20巻")
        Assertions.assertEquals(books.get(0).release, Release.ON_SALE)
        Assertions.assertEquals(books.get(0).salesDate, LocalDate.of(2001, 9, 9))

        Assertions.assertEquals(books.get(1).authorName, "尾田栄一郎")
        Assertions.assertEquals(books.get(1).title, "ワンピース22巻")
        Assertions.assertEquals(books.get(1).release, Release.ON_SALE)
        Assertions.assertEquals(books.get(1).salesDate, LocalDate.of(2002, 2, 9))

    }

    @Test
    fun 著者名であいまい検索By田栄() {

        var newBooks = 著者書籍を3件INSERT().sortedBy { book -> book.salesDate }

        val tk = Book(UUID.randomUUID(), "田中角栄", "日本列島改造論",
                Release.ON_SALE, LocalDate.of(1972, 6, 20))

        bookRepository.save(tk)

        val authName = "田栄"

        var books = bookRepository.findByAuthorNameLike("%${authName}%")
                .sortedBy { book -> book.salesDate }

        var index = 0;

        Assertions.assertEquals(books.size, 3)

        for (book in books) {
//            println(book)
//            println(newBooks.get(index))
            Assertions.assertEquals(newBooks.get(index).authorName, book.authorName)
            Assertions.assertEquals(newBooks.get(index).title, book.title)
            Assertions.assertEquals(newBooks.get(index).release, book.release)
            Assertions.assertEquals(newBooks.get(index).salesDate, book.salesDate)
            ++index
        }
    }


    @Test
    fun 著者名であいまい検索By田() {

        val tk = Book(UUID.randomUUID(), "田中角栄", "日本列島改造論",
                Release.ON_SALE, LocalDate.of(1972, 6, 20))
        bookRepository.save(tk)

        var newBooks = 著者書籍を3件INSERT().toMutableList().plus(tk)
                .toList().sortedBy { book -> book.salesDate }

        val authName = "田"

        var books = bookRepository.findByAuthorNameLike("%${authName}%")
                .sortedBy { book -> book.salesDate }

        var index = 0;

        Assertions.assertEquals(books.size, 4)

        for (book in books) {
            println(book)
            println(newBooks.get(index))
            Assertions.assertEquals(newBooks.get(index).authorName, book.authorName)
            Assertions.assertEquals(newBooks.get(index).title, book.title)
            Assertions.assertEquals(newBooks.get(index).release, book.release)
            Assertions.assertEquals(newBooks.get(index).salesDate, book.salesDate)
            ++index
        }
    }

    @Test
    fun 著者名とタイトルであいまい検索() {

        val op22 = Book(UUID.randomUUID(), "田畑祐宏", "ピーマンの肉詰め",
                Release.ON_SALE, LocalDate.of(2029, 2, 9))
        bookRepository.save(op22)

        var newBooks = 著者書籍を3件INSERT().toMutableList().plus(op22)
                .toList().sortedBy { book -> book.salesDate }

        var authName = "田"
        var title = "ピー"

        var books = bookRepository.findByAuthorNameLikeAndTitleLike(
                "%${authName}%", "%${title}%").sortedBy { book -> book.salesDate }

        var index = 0

        Assertions.assertEquals(books.size, 4)

        for (book in books) {
//            println(book)
//            println(newBooks.get(index))
//            println("---------------------------")
            Assertions.assertEquals(newBooks.get(index).authorName, book.authorName)
            Assertions.assertEquals(newBooks.get(index).title, book.title)
            Assertions.assertEquals(newBooks.get(index).release, book.release)
            Assertions.assertEquals(newBooks.get(index).salesDate, book.salesDate)
            ++index
        }

    }
}