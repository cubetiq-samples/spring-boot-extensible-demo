package com.cubetiqs.plugin.context

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class Pluggable(
    val install: Boolean = true,
)