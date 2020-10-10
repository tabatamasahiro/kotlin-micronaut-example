package com.example.tabata.controller

import com.example.tabata.domain.BookService
import com.example.tabata.domain.BookTitle
import com.example.tabata.domain.Release
import com.example.tabata.domain.Update
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView
import io.micronaut.views.View

@Controller("/book")
class BookController(val bookService: BookService) {

    val createMap = hashMapOf<String, String>("crud" to "/create", "page_sub_title" to "新刊登録")
    val updateMap = hashMapOf<String, String>("crud" to "/update", "page_sub_title" to "書籍情報の変更")

    /**
     * ブラウザへ返却したhome.htmlがレンダリングされた
     */
    @Get(value = "/", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun createStart() = ModelAndView("book", createMap)

    @Post("/create", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun createExecute(@RequestBean bookForm: BookForm): ModelAndView<Any> {

        println(bookForm)

        var updateAndMsg = bookService.saveNewBook(bookForm)

        var resultMap = hashMapOf<String, String?>()

        if (updateAndMsg.first == Update.NG) {
            println("UpdateNG!!!${bookForm}")
            resultMap.put("author_name", bookForm.author_name)
            resultMap.put("title", bookForm.title)
            resultMap.put("yyyymmdd", bookForm.yyyymmdd)
        }
        resultMap.put("msg", updateAndMsg.second)
        resultMap.putAll(createMap)

        return ModelAndView("book", resultMap)
    }

    @Get(value = "/search", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun searchStart() = ModelAndView("search", "")

    @Post("/search", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun searchExecute(author_name: String, title: String): ModelAndView<Any> {
        println("author_name=${author_name}, title=${title}")

        var books = bookService.search(author_name, title)

        books.forEach { book ->
            println(book)
        }

        var modelAndView = ModelAndView<Any>()
        modelAndView.setView("search")
        modelAndView.setModel(mapOf("books" to books, "author_name" to author_name, "title" to title))

        return modelAndView
    }

    @Get(value = "/update", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun updateStart(@QueryValue isbn: String): ModelAndView<Any> {
        println("isbn=${isbn}")

        var modelAndView = ModelAndView<Any>()

        modelAndView.setView("book")

        var book = bookService.findOne(isbn)

        var mapOf = mutableMapOf<String, String?>(
                "isbn" to book.isbn.toString(),
                "author_name" to book.authorName,
                "title" to book.title,
                "yyyymmdd" to book.salesDate.toString(),
                "release" to book.release.name)

        mapOf.putAll(updateMap)

        modelAndView.setModel(mapOf)

        return modelAndView
    }

    @Post(value = "/update", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun updateExecute(isbn: String, title: String, yyyymmdd: String, release: Release): ModelAndView<Any> {

        println("isbn: ${isbn}, title: ${title}, yyyymmdd: ${yyyymmdd}, release: ${release.name}")

//        BookTitle.valueToUpdate(title).parseWithRelease()

        return ModelAndView("book", "")
    }

}