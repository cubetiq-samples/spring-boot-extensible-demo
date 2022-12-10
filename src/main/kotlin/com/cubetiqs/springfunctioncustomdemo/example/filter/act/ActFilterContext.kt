package com.cubetiqs.springfunctioncustomdemo.example.filter.act

interface ActFilterContext {
    fun getInput(): Input
    fun <T> getOutput(): Output<T>

    fun accept(output: Any?) {
        getOutput<Any>().accept(output)
    }

    interface Input {
        fun getBody(): Any?
        fun getHeaders(): Map<String, String>
        fun getParams(): Map<String, String>
    }

    interface Output<T> {
        fun accept(output: T?)
    }
}