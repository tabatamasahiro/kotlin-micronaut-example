package com.example.tabata.controller

import com.example.tabata.domain.BookService
import com.example.tabata.domain.Release
import com.example.tabata.domain.Update
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView

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

        var books = bookService.searchAndUpdateReleaseStatus(author_name, title)

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

        var optionalBook = bookService.findOne(isbn)

        var resutlMap = mutableMapOf<String, String?>()
        resutlMap.putAll(updateMap)

        if (optionalBook.isPresent) {
            resutlMap.put("isbn", optionalBook.get().isbn.toString())
            resutlMap.put("author_name", optionalBook.get().authorName)
            resutlMap.put("title", optionalBook.get().title)
            resutlMap.put("yyyymmdd", optionalBook.get().salesDate.toString())
            resutlMap.put("release", optionalBook.get().release.name)
        } else {
            resutlMap.put("msg", "想定のエラー")
        }

        modelAndView.setModel(resutlMap)

        return modelAndView
    }

    @Post(value = "/update", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun updateExecute(isbn: String, release: Release, @RequestBean bookForm: BookForm): ModelAndView<Any> {

        println("------------------------------------------------")
        println("isbn: ${isbn}, release: ${release.name}")
        println(bookForm)
        println("------------------------------------------------")

        var updateAndMsg = bookService.updateTitleAndSalseDate(isbn, release, bookForm)

        var resultMap = mutableMapOf<String, String?>("msg" to updateAndMsg.second)

        resultMap.putAll(updateMap)

        if (updateAndMsg.first == Update.NG) {
            resultMap.put("isbn", isbn)
            resultMap.put("title", bookForm.title)
            resultMap.put("author_name", bookForm.author_name)
            resultMap.put("yyyymmdd", bookForm.yyyymmdd)
            resultMap.put("release", release.name)
        }

        return ModelAndView("book", resultMap)
    }

}