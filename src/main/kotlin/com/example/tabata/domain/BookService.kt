package com.example.tabata.domain

import com.example.tabata.controller.BookForm
import com.example.tabata.repository.BookRepository
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

        var datePublication = DatePublication.valueToUpdate(bookForm.yyyymmdd)

        if (datePublication.parseToSave() == Update.NG) {
            // 現在日以降の日付が選択されていない
            return Pair(Update.NG, datePublication.errorMsg)
        }

        var release = BookRelease.checkToDoRelase(datePublication, bookTitle)

        bookRepository.save(Book(UUID.randomUUID(), bookForm.author_name,
                bookForm.title, release, datePublication.localDate))

        return Pair(Update.OK, "登録しました")
    }

}