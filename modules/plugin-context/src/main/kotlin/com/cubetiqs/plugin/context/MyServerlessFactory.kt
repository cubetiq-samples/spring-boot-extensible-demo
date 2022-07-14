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