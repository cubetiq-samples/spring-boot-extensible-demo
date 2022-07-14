package com.cubetiqs.plugin

import com.cubetiqs.plugin.context.MyServerlessPlugin
import com.cubetiqs.plugin.context.Pluggable
import com.cubetiqs.plugin.context.ServerlessContext

// com.cubetiqs.plugin.MyCustomPlugin2
@Pluggable(install = true)
class MyCustomPlugin2 : MyServerlessPlugin {
    override fun serve(context: ServerlessContext): Any {
        return "MyCustomPlugin-2"
    }

    override fun getId(): String {
        return "my-plugin-2"
    }
}