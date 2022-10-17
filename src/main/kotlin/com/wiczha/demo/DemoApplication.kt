package com.wiczha.demo

import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

@SpringBootApplication
open class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("api/")
class QuizResource {
    @GetMapping("items/")
    fun getItems(): MutableList<UserItem> = runBlocking  {
        // @PathVariable state: String
        val retrieveItems = RetrieveItems()
        val name = "player"
        val list: MutableList<UserItem> = retrieveItems.getItemsDataSQL()
        return@runBlocking list
    }

}