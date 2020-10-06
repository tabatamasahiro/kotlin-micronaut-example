package com.example.tabata.controller

import com.example.tabata.domain.BookService
import com.example.tabata.domain.Update
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView

@Controller("/book")
class BookController(val bookService: BookService) {

    /**
     * ブラウザへ返却したhome.htmlがレンダリングされた
     */
    @Get(value = "/", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun home() = ModelAndView("new_book", "")

    @Post("/new", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun findTest(@RequestBean bookForm: BookForm): ModelAndView<Any> {

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

        return ModelAndView("new_book", resultMap)
    }

}