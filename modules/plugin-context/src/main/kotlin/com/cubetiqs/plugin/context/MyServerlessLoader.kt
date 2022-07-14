package com.cubetiqs.plugin.context

object MyServerlessLoader {
    fun serve(plugin: MyServerlessPlugin, context: ServerlessContext): Any {
        return plugin.serve(context)
    }
}