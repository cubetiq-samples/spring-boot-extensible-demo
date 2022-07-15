# Spring Function Custom Demo

Includes:
- Serverless Function

[ServerlessContext Interface:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/plugin-context/src/main/kotlin/com/cubetiqs/plugin/context/ServerlessContext.kt)
```kotlin
package com.cubetiqs.plugin.context

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface ServerlessContext {
    fun getRequest(): HttpServletRequest
    fun getResponse(): HttpServletResponse
}
```

[PluginEntry Interface:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/plugin-context/src/main/kotlin/com/cubetiqs/plugin/context/PluginEntry.kt)
```kotlin
package com.cubetiqs.plugin.context

interface PluginEntry {
    fun getId(): String
}
```

[MyServerlessPlugin Inteface:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/plugin-context/src/main/kotlin/com/cubetiqs/plugin/context/MyServerlessPlugin.kt)
```kotlin
package com.cubetiqs.plugin.context

interface MyServerlessPlugin : PluginEntry {
    fun serve(context: ServerlessContext): Any
}
```

[MyServerlessLoader Object:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/plugin-context/src/main/kotlin/com/cubetiqs/plugin/context/MyServerlessLoader.kt)
```kotlin
package com.cubetiqs.plugin.context

object MyServerlessLoader {
    fun serve(plugin: MyServerlessPlugin, context: ServerlessContext): Any {
        return plugin.serve(context)
    }
}
```

[MyServerlessFactory Object:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/plugin-context/src/main/kotlin/com/cubetiqs/plugin/context/MyServerlessFactory.kt)
```kotlin
package com.cubetiqs.plugin.context

import org.reflections.Reflections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

object MyServerlessFactory {
    private val plugins: ConcurrentMap<String, MyServerlessPlugin> = ConcurrentHashMap()

    // Exact plugin
    fun install(plugin: MyServerlessPlugin) {
        plugins[plugin.getId()] = plugin
        println("Plugin: ${plugin.getId()} installed!")
    }

    // Invoke by className with full package and main plugin class
    fun install(className: String) {
        val clazz = Class.forName(className)
        install(clazz)
    }

    fun install(clazz: Class<*>) {
        val plugin = clazz.getDeclaredConstructor().newInstance() as? MyServerlessPlugin
        plugin ?: throw IllegalArgumentException("Install failed!")
        install(plugin)
    }

    fun install(classes: Set<Class<*>>) {
        classes.map { install(it) }
    }

    @kotlin.jvm.Throws(IllegalArgumentException::class)
    fun load(plugin: String): MyServerlessPlugin {
        return plugins[plugin] ?: throw IllegalArgumentException("Cannot find plugin: $plugin")
    }

    fun scanPackages(packageName: String): Set<Class<*>> {
        val reflections = Reflections(packageName)
        return reflections.getTypesAnnotatedWith(Pluggable(install = true))
    }
}
```

[Pluggable Annotation:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/plugin-context/src/main/kotlin/com/cubetiqs/plugin/context/Pluggable.kt)
```kotlin
package com.cubetiqs.plugin.context

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class Pluggable(
    val install: Boolean = true,
)
```

[MyCustomPlugin1 Class:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/my-plugin-1/src/main/kotlin/com/cubetiqs/plugin/MyCustomPlugin1.kt)
```kotlin
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
```

[MyCustomPlugin2 Class:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/my-plugin-2/src/main/kotlin/com/cubetiqs/plugin/MyCustomPlugin2.kt)
```kotlin
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
```

[MyCustomPlugin3 Class:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/my-plugin-3/src/main/kotlin/com/cubetiqs/plugin/MyCustomPlugin3.kt)
```kotlin
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
```

[SpringServerlessContext Interface:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/modules/spring-plugin-context/src/main/kotlin/com/cubetiqs/plugin/context/spring/SpringServerlessContext.kt)
```kotlin
package com.cubetiqs.plugin.context.spring

import com.cubetiqs.plugin.context.ServerlessContext
import org.springframework.context.ApplicationContext

interface SpringServerlessContext : ServerlessContext {
    fun getContext(): ApplicationContext
}
```

[SpringFunctionCustomDemoApplication:](https://git.cubetiqs.com/cubetiq/spring-function-custom-demo/src/branch/main/src/main/kotlin/com/cubetiqs/springfunctioncustomdemo/SpringFunctionCustomDemoApplication.kt)
```kotlin
package com.cubetiqs.springfunctioncustomdemo

import com.cubetiqs.plugin.context.MyServerlessFactory
import com.cubetiqs.plugin.context.MyServerlessLoader
import com.cubetiqs.plugin.context.ServerlessContext
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

@SpringBootApplication
class SpringFunctionCustomDemoApplication

fun main(args: Array<String>) {
    runApplication<SpringFunctionCustomDemoApplication>(*args)
}

@RestController
class ServerlessServerController @Autowired constructor(
    private val applicationContext: ApplicationContext,
) {
    @RequestMapping("/{plugin}")
    fun serve(
        @PathVariable plugin: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {
        val body =
            MyServerlessLoader.serve(
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
```

# Example with curl

End Point: http://localhost:8080/my-plugin-1
```shell
curl http://localhost:8080/my-plugin-1 | json_pp
```

End Point: http://localhost:8080/install/com.cubetiq.plugin.MyCustomPlugin1
```shell
curl http://localhost:8080/install/com.cubetiq.plugin.MyCustomPlugin1 | json_pp
```

End Point: http://localhost:8080/scan/com.cubetiq.plugin
```shell
curl http://localhost:8080/scan/com.cubetiq.plugin | json_pp
```

# Development
```shell
git clone https://git.cubetiqs.com/cubetiq/spring-function-custom-demo.git
```

### Contributors
- Sambo Chea <sombochea@cubetiqs.com>