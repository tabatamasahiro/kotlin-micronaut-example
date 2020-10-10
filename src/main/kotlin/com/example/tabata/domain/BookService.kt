package com.example.tabata.domain

import com.example.tabata.controller.BookForm
import com.example.tabata.repository.BookRepository
import java.time.LocalDate
import java.util.*
import javax.inject.Singleton

@Singleton
class BookService(val bookRepository: BookRepository) {

    fun saveNewBook(bookForm: BookForm): Pair<Update, String> {

        print(bookForm)

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
            return Pair(Update.NG, "この著者はすでに登録されています")
        }

//        if (bookRepository.existsByAuthorNameAndTitleAndSalesDate(
//                        bookForm.author_name, bookForm.title, salesDate.valueForDatabse())) {
//            return Pair(Update.NG, "この著者はすでに登録されています(2)")
//        }

        bookRepository.save(Book(UUID.randomUUID(), bookForm.author_name,
                bookForm.title, release, salesDate.localDate))

        return Pair(Update.OK, "登録しました")
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

    fun findOne(isbn: String): Book {
        return bookRepository.findById(UUID.fromString(isbn)).orElse(null)
    }

}