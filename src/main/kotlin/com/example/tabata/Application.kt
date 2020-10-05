package com.example.tabata

import com.example.tabata.domain.Book
import com.example.tabata.domain.Release
import com.example.tabata.repository.BookRepository
import io.micronaut.context.event.StartupEvent
import io.micronaut.core.annotation.TypeHint
import io.micronaut.runtime.Micronaut.*
import io.micronaut.runtime.event.annotation.EventListener
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.util.*
import javax.inject.Singleton

fun main(args: Array<String>) {
    build()
            .args(*args)
            .packages("com.example.tabata")
            .start()
}

@Singleton
@TypeHint(typeNames = ["org.h2.Driver", "org.h2.mvstore.db.MVTableEngine"])
class Application(
        private val bookRepository: BookRepository
) {

    @EventListener
    fun init(event: StartupEvent) {

        if (LOG.isInfoEnabled) {
            LOG.info("Populating data")
        }

        var book1 = Book(UUID.randomUUID(), "落合陽一", "日本再興戦略", Release.ON_SALE,
                LocalDate.of(2018, 1, 31))

        var book2 = Book(UUID.randomUUID(), "安宅和人", "issue driven", Release.ON_SALE,
                LocalDate.of(2010, 11, 24))

        bookRepository.saveAll(listOf(book1, book2))
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(Application::class.java)
    }
}
