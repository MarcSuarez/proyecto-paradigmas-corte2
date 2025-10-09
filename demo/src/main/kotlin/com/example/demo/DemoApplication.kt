package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import io.github.cdimascio.dotenv.Dotenv

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    val dotenv = Dotenv.load()

    System.setProperty("DB_HOST", dotenv["DB_HOST"])
    System.setProperty("DB_PORT", dotenv["DB_PORT"])
    System.setProperty("DB_NAME", dotenv["DB_NAME"])
    System.setProperty("DB_USER", dotenv["DB_USER"])
    System.setProperty("DB_PASSWORD", dotenv["DB_PASSWORD"])

    org.springframework.boot.runApplication<DemoApplication>(*args)
}
