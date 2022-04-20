package com.example.springdockerize

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringDockerizeApplication

fun main(args: Array<String>) {
    runApplication<SpringDockerizeApplication>(*args)
}
