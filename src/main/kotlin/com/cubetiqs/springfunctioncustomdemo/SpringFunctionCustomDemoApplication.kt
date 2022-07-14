package com.cubetiqs.springfunctioncustomdemo

import com.cubetiqs.plugin.context.MyServerlessFactory
import com.cubetiqs.plugin.context.MyServerlessLoader
import com.cubetiqs.plugin.context.ServerlessContext
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@SpringBootApplication
class SpringFunctionCustomDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringFunctionCustomDemoApplication>(*args)
}

@RestController
class ServerlessServerController {
    @RequestMapping("/{plugin}")
    fun serve(
        @PathVariable plugin: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        val body =
            MyServerlessLoader.serve(MyServerlessFactory.load(plugin), ServerlessContext.create(request, response))
        jacksonObjectMapper().writeValue(response.outputStream, body)
    }

    @RequestMapping("/install/{className}")
    fun install(
        @PathVariable className: String
    ): Any {
        MyServerlessFactory.install(className)
        return "Succeed!"
    }

    @RequestMapping("/scan/{packageName}")
    fun scan(
        @PathVariable packageName: String
    ): Any {
        val classes = MyServerlessFactory.scanPackages(packageName)
        MyServerlessFactory.install(classes)
        return classes.map { it.canonicalName }
    }
}