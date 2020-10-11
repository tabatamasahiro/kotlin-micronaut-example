package com.example.tabata.domain

import com.example.tabata.controller.BookForm
import com.example.tabata.repository.BookRepository
import org.slf4j.LoggerFactory
import java.lang.RuntimeException
import java.util.*
import javax.inject.Singleton

@Singleton
class BookService(val bookRepository: BookRepository) {
    companion object {
        private val logger = LoggerFactory.getLogger(BookService::class.java)
    }

    val MSG_INSERT_SUCCESS = "登録しました"
    val MSG_UPDATE_SUCCESS = "更新しました"
    val MSG_DELETE_SUCCESS = "削除しました"
    val MSG_INSERT_ALREADY = "この著者はすでに登録されています"
    val MSG_DELETE_ALREADY = "この著者はすでに削除されています"

    fun saveNewBook(bookForm: BookForm): Pair<Update, String> {

        logger.info("${bookForm}")

        var authorName = AuthorName.valueToUpdate(bookForm.author_name)

        if (authorName.parseToSave() == Update.NG) {
            return Pair(Update.NG, authorName.errorMsg)
        }

        var bookTitle = BookTitle.valueToUpdate(bookForm.title)

        if (bookTitle.parseToSave() == Update.NG) {
            // 初回登録において想定外ルート
            return Pair(Update.NG, bookTitle.errorMsg)
        }

        var salesDate = SalesDate.valueToUpdate(bookForm.yyyymmdd)

        if (salesDate.parseToSave() == Update.NG) {
            // 現在日以降の日付が選択されていない
            return Pair(Update.NG, salesDate.errorMsg)
        }

        var release = BookRelease.checkToDoRelase(salesDate, bookTitle)

        if (bookRepository.existsByAuthorNameAndTitle(bookForm.author_name, bookForm.title)) {
            return Pair(Update.NG, MSG_INSERT_ALREADY)
        }

//        if (bookRepository.existsByAuthorNameAndTitleAndSalesDate(
//                        bookForm.author_name, bookForm.title, salesDate.valueForDatabse())) {
//            return Pair(Update.NG, "この著者はすでに登録されています(2)")
//        }

        bookRepository.save(Book(UUID.randomUUID(), bookForm.author_name,
                bookForm.title, release, salesDate.localDate))

        return Pair(Update.OK, MSG_INSERT_SUCCESS)
    }


    fun searchAndUpdateReleaseStatus(author_name: String, title: String): List<Book> {

        var books = search(author_name, title)

        var booksReleaseUpdate = mutableListOf<Book>()

        for (book in books) {
            booksReleaseUpdate.add(checkAndUpdateReleaseStatus(book))
        }

        return booksReleaseUpdate
    }

    fun checkAndUpdateReleaseStatus(book: Book): Book {

        println(book)

        if (book.release == Release.ON_SALE) {
            return book
        }

        if (book.title.isNullOrEmpty()) {
            return book
        }

        if (book.salesDate == null) {
            return book
        }

        if (SalesDate.isPastDate(book.salesDate)) {
            println("update:${book}")
            bookRepository.update(book.isbn, Release.ON_SALE)
            return bookRepository.findById(book.isbn).get()
        }

        return book
    }

    fun search(author_name: String, title: String): List<Book> {
        if (author_name.isNotBlank() && title.isNullOrBlank()) {
            println("001")
            return bookRepository.findByAuthorNameLike("%${author_name}%")
        }

        if (author_name.isNullOrBlank() && title.isNotBlank()) {
            println("002")
            return bookRepository.findByTitleLike("%${title}%")
        }

        println("003")
        return bookRepository.findByAuthorNameLikeAndTitleLike(
                "%${author_name}%", "%${title}%")
    }

    fun findOne(isbn: String): Optional<Book> {
        return bookRepository.findById(UUID.fromString(isbn))
    }

    fun updateTitleAndSalseDate(isbn: String, release: Release, bookForm: BookForm): Pair<Update, String> {

        var bookTitle = BookTitle.valueToUpdate(bookForm.title)
        if (bookTitle.parseWithRelease(release) == Update.NG) {
            return Pair(Update.NG, bookTitle.errorMsg)
        }

        var salesDate = SalesDate.valueToUpdate(bookForm.yyyymmdd)
        if (salesDate.parseWithRelease(release) == Update.NG) {
            return Pair(Update.NG, salesDate.errorMsg)
        }

        bookRepository.update(UUID.fromString(isbn), bookTitle.title, salesDate.localDate)
        return Pair(Update.OK, MSG_UPDATE_SUCCESS)
    }

    fun deleteById(isbn: UUID): Pair<Update, BookAndMsg> {

        if (bookRepository.existsById(isbn)) {
            var book = bookRepository.findById(isbn)
            bookRepository.deleteById(isbn)
            return Pair(Update.OK, BookAndMsg(book, MSG_DELETE_SUCCESS))
        }
        return Pair(Update.NG, BookAndMsg(Optional.empty(), MSG_DELETE_ALREADY + "(${isbn.toString()})"))
    }
}