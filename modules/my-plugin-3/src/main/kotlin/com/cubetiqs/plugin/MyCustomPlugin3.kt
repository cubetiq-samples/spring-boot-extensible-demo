package com.cubetiqs.plugin

import com.cubetiqs.plugin.context.MyServerlessPlugin
import com.cubetiqs.plugin.context.Pluggable
import com.cubetiqs.plugin.context.ServerlessContext

// com.cubetiqs.plugin.MyCustomPlugin3
@Pluggable(install = true)
class MyCustomPlugin3 : MyServerlessPlugin {
    override fun serve(context: ServerlessContext): Any {
        return "MyCustomPlugin-3"
    }

    override fun getId(): String {
        return "my-plugin-3"
    }
}