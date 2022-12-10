package com.cubetiqs.springfunctioncustomdemo.example.filter.rule

interface RuleFilterContext {
    fun getInput(): Input

    interface Input {
        fun getBody(): Any?
        fun getHeaders(): Map<String, String?> {
            return emptyMap()
        }

        fun getParams(): Map<String, Any?> {
            return emptyMap()
        }
    }
}

@FunctionalInterface
fun interface RuleFilter {
    fun pass(
        context: RuleFilterContext,
    ): Boolean
}