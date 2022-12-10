package com.cubetiqs.springfunctioncustomdemo

import com.cubetiqs.springfunctioncustomdemo.example.filter.act.ActFilterDemo
import com.cubetiqs.springfunctioncustomdemo.example.filter.rule.RuleFilterDemo
import org.junit.jupiter.api.Test

class DemoTests {
    @Test
    fun test() {
        val actFilter = ActFilterDemo()
        actFilter.demo()
    }

    @Test
    fun test2() {
        val ruleFilter = RuleFilterDemo()
        ruleFilter.demo()
    }
}