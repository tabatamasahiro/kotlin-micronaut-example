package com.example.tabata

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
    build()
            .args(*args)
            .packages("com.example.tabata")
            .start()
}

