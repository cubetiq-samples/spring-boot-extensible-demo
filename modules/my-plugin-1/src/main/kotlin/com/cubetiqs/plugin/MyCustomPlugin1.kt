package com.cubetiqs.plugin

import com.cubetiqs.plugin.context.MyServerlessPlugin
import com.cubetiqs.plugin.context.Pluggable
import com.cubetiqs.plugin.context.ServerlessContext

// com.cubetiqs.plugin.MyCustomPlugin1
@Pluggable
class MyCustomPlugin1 : MyServerlessPlugin {
    override fun serve(context: ServerlessContext): Any {
        return "MyCustomPlugin-1"
    }

    override fun getId(): String {
        return "my-plugin-1"
    }
}