package com.cubetiqs.plugin.context.spring

import com.cubetiqs.plugin.context.ServerlessContext
import org.springframework.context.ApplicationContext

interface SpringServerlessContext : ServerlessContext {
    fun getContext(): ApplicationContext
}