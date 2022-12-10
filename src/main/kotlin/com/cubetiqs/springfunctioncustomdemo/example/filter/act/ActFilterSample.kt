package com.cubetiqs.springfunctioncustomdemo.example.filter.act

class ActFilterSample : ActFilter {
    override fun filter(context: ActFilterContext) {
        val body = context.getInput().getBody()
        val output = mapOf(
            "body" to body,
            "headers" to context.getInput().getHeaders(),
            "params" to context.getInput().getParams(),
        )

        context.accept(output)
    }
}

class ActFilterDemo {
    fun demo() {
        val filter = ActFilterSample()
        val context = object : ActFilterContext {
            override fun getInput(): ActFilterContext.Input {
                return object : ActFilterContext.Input {
                    override fun getBody(): Any {
                        return "Hello World!"
                    }

                    override fun getHeaders(): Map<String, String> {
                        return emptyMap()
                    }

                    override fun getParams(): Map<String, String> {
                        return emptyMap()
                    }
                }
            }

            override fun <T> getOutput(): ActFilterContext.Output<T> {
                return object : ActFilterContext.Output<T> {
                    override fun accept(output: T?) {
                        println("Output: $output")
                    }
                }
            }
        }

        filter.filter(context)
    }
}