package com.cubetiqs.plugin.context

interface MyServerlessPlugin : PluginEntry {
    fun serve(context: ServerlessContext): Any
}