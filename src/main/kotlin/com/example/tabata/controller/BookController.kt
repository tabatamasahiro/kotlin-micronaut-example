package com.example.tabata.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView
import io.micronaut.views.View

@Controller("/book")
class BookController {

    //@ResponseBodyみたいにブラウザへHTMLが返却された。
//    @Get("/")
//    @View("home")
//    fun home() = HttpResponse.ok<String>()

    /**
     * ブラウザへ返却したhome.htmlがレンダリングされた
     */
    @Get(value = "/", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun home() = ModelAndView("home", "")

    @Post("/new", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    @View("home")
    fun findTest(@RequestBean bookForm: BookForm): BookForm {
        //author_name=yamada+tarou&title=hello+kononitiwa&date_publication=2020%2F10%2F13&%E9%80%81%E4%BF%A1=%E9%80%81%E4%BF%A1
        println(bookForm)
        bookForm.author_name = "yamada"
        return bookForm

    }

}