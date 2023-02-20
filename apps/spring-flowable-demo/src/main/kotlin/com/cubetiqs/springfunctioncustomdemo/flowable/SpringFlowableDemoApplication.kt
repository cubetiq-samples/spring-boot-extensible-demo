package com.cubetiqs.springfunctioncustomdemo.flowable

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = ["com.cubetiqs.springfunctioncustomdemo.flowable"],
    proxyBeanMethods = false,
)
class SpringFlowableDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringFlowableDemoApplication>(*args)
}