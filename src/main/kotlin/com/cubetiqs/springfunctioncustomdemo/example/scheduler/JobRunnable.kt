package com.cubetiqs.springfunctioncustomdemo.example.scheduler

@FunctionalInterface
fun interface JobRunnable : Runnable {
    fun executeInternal()

    override fun run() {
        // start duration
        executeInternal()
        // end duration
    }
}