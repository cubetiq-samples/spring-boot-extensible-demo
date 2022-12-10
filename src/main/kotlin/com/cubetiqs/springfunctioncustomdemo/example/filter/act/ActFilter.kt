package com.cubetiqs.springfunctioncustomdemo.example.filter.act

@FunctionalInterface
fun interface ActFilter {
    fun filter(
        context: ActFilterContext,
    )
}