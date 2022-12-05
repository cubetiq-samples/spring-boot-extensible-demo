package com.cubetiqs.plugin.context

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface ServerlessContext {
    fun getRequest(): HttpServletRequest
    fun getResponse(): HttpServletResponse

    companion object {
        fun create(request: HttpServletRequest, response: HttpServletResponse): ServerlessContext {
            return object : ServerlessContext {
                override fun getRequest(): HttpServletRequest {
                    return request
                }

                override fun getResponse(): HttpServletResponse {
                    return response
                }
            }
        }
    }
}