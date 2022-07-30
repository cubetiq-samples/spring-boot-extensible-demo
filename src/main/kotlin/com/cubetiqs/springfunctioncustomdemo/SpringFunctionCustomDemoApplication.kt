package com.cubetiqs.springfunctioncustomdemo

import com.cubetiqs.plugin.context.MyServerlessFactory
import com.cubetiqs.plugin.context.MyServerlessLoader
import com.cubetiqs.plugin.context.spring.SpringServerlessContext
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@SpringBootApplication(
    scanBasePackages = ["com.cubetiqs.springfunctioncustomdemo"]
)
class SpringFunctionCustomDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringFunctionCustomDemoApplication>(*args)
}

@RestController
@RequestMapping("/plugin")
class ServerlessServerController @Autowired constructor(
    private val applicationContext: ApplicationContext,
) {
    @RequestMapping("/{plugin}")
    fun serve(
        @PathVariable plugin: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        val body = MyServerlessLoader.serve(
            MyServerlessFactory.load(plugin),
            MySpringPluginContext(request = request, response = response, context = applicationContext)
        )
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

class MySpringPluginContext(
    private val response: HttpServletResponse,
    private val request: HttpServletRequest,
    private val context: ApplicationContext,
) : SpringServerlessContext {
    override fun getContext(): ApplicationContext {
        return context
    }

    override fun getRequest(): HttpServletRequest {
        return request
    }

    override fun getResponse(): HttpServletResponse {
        return response
    }
}