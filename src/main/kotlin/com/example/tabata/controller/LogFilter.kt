package com.example.tabata.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.util.*

@Filter("/**")
class LogFilter : HttpServerFilter {
    companion object {
        private val logger = LoggerFactory.getLogger(LogFilter::class.java)
    }

    override fun doFilter(request: HttpRequest<*>?, chain: ServerFilterChain?): Publisher<MutableHttpResponse<*>> {

        MDC.put("url", request?.uri.toString())
        MDC.put("method", request?.methodName)
        logger.info("start")
        return chain!!.proceed(request)


//        try {
//            MDC.put("common", UUID.randomUUID().toString())
//            logger.info("start")
//            return chain!!.proceed(request)
//        } catch (e: Exception) {
//            logger.error(e.toString(), e)
//        } finally {
//            logger.info("end")
//            MDC.remove("common")
//        }
    }

}