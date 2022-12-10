package com.cubetiqs.springfunctioncustomdemo.example.filter.rule

class RuleFilterSample : RuleFilter {
    override fun pass(context: RuleFilterContext): Boolean {
        val input = context.getInput()
        val body = input.getBody()
        return body != null
    }
}

class RuleFilterDemo {
    fun demo() {
        val filter = RuleFilterSample()
        val context = object : RuleFilterContext {
            override fun getInput(): RuleFilterContext.Input {
                return object : RuleFilterContext.Input {
                    override fun getBody(): Any? {
                        return null
                    }
                }
            }
        }

        val pass = filter.pass(context)
        println("Pass: $pass")
    }
}