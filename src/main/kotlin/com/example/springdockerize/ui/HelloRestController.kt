package com.example.springdockerize.ui

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 *
 * @author gm-jeong
 * @since 2022/04/20
 * */
@RestController
@RequestMapping("/hello")
class HelloRestController {

    @GetMapping
    fun hello(): String {
        return "hello spring with docker"
    }
}