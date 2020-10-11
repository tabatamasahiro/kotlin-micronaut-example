package com.example.tabata.controller

import com.example.tabata.Application
import com.example.tabata.domain.BookService
import com.example.tabata.domain.Release
import com.example.tabata.domain.Update
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.MediaType.CHARSET_PARAMETER
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView
import io.micronaut.views.View
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.nio.charset.Charset.defaultCharset
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper
import kotlin.math.log

@Controller("/book")
class BookController(val bookService: BookService) {
    companion object {
        private val logger = LoggerFactory.getLogger(BookController::class.java)
    }

    val createMap = mutableMapOf<String, String>("crud" to "/create", "page_sub_title" to "新刊登録")
    val updateMap = mutableMapOf<String, String>("crud" to "/update", "page_sub_title" to "書籍情報の変更")

    /**
     * ブラウザへ返却したhome.htmlがレンダリングされた
     */
    @Get(value = "/", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun indexStart() = ModelAndView("/index", "")

    @Get(value = "/create", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun createStart() = ModelAndView("/book", createMap)

    @Post("/create", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun createExecute(@RequestBean bookForm: BookForm): ModelAndView<Any> {

        logger.info(bookForm.toString())

        var updateAndMsg = bookService.saveNewBook(bookForm)

        var resultMap = mutableMapOf<String, String?>()

        logger.info("first=${updateAndMsg.first}, second=${updateAndMsg.second}")

        resultMap.put("msg", updateAndMsg.second)

        if (updateAndMsg.first == Update.NG) {
            resultMap.put("author_name", bookForm.author_name)
            resultMap.put("title", bookForm.title)
            resultMap.put("yyyymmdd", bookForm.yyyymmdd)
            resultMap.putAll(createMap)
            return ModelAndView("book", resultMap)
        }

        return ModelAndView("index", resultMap)
    }

    @Get(value = "/search", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun searchStart() = ModelAndView("search", "")

    @Post("/search", consumes = [MediaType.APPLICATION_FORM_URLENCODED],
            produces = ["text/html;charset=UTF-8;"])
    fun searchExecute(author_name: String, title: String): ModelAndView<Any> {

        logger.info("author_name=${author_name}, title=${title}")

        var books = bookService.searchAndUpdateReleaseStatus(author_name, title)

        var modelAndView = ModelAndView<Any>()
        modelAndView.setView("search")
        modelAndView.setModel(mapOf("books" to books, "author_name" to author_name, "title" to title))

        return modelAndView
    }

    @Get(value = "/update", consumes = [MediaType.APPLICATION_FORM_URLENCODED], produces = ["text/html"])
    fun updateStart(@QueryValue isbn: String): ModelAndView<Any> {

        logger.info("isbn=${isbn}")

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

        logger.info("isbn=${isbn}, release=${release}, ${bookForm}")

        var updateAndMsg = bookService.updateTitleAndSalseDate(isbn, release, bookForm)

        logger.info("first=${updateAndMsg.first}, second=${updateAndMsg.second}")

        var resultMap = mutableMapOf<String, String?>("msg" to updateAndMsg.second)

        if (updateAndMsg.first == Update.NG) {
            logger.debug("book page")
            resultMap.putAll(updateMap)
            resultMap.put("isbn", isbn)
            resultMap.put("title", bookForm.title)
            resultMap.put("author_name", bookForm.author_name)
            resultMap.put("yyyymmdd", bookForm.yyyymmdd)
            resultMap.put("release", release.name)
            return ModelAndView("book", resultMap)
        }

        logger.debug("search page")
        return ModelAndView("search", resultMap)
    }

//    fun utf8tosjis(value: String): String {
//
//        var strSjis = value.toByteArray(charset("UTF-8"))
//
//        var string = String(strSjis, charset("UTF-8"))
//
//        logger.info("string=${string}")
//
//        for (i in 0..strSjis.size - 1) {
//            var c: Byte = strSjis[i]
//            var s = "%X".format(c)
//            logger.info("Sample:${s}")
//        }
//
//        return ""
//    }

}