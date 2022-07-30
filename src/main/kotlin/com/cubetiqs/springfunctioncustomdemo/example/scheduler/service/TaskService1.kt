package com.cubetiqs.springfunctioncustomdemo.example.scheduler.service

import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@Service
class TaskService1 {
    fun doTask1() {
        println("[TaskService1] Task 1 is working...")
    }

    fun doTask2() {
        println("[TaskService1] Task 2 is working...")

        val maxInterval = AtomicInteger(30)
        while (maxInterval.getAndDecrement() > 0) {
            println("Working interval at: ${maxInterval.get()}")
            TimeUnit.SECONDS.sleep(1)
        }

        println("[TaskService1] Task 2 is completed!")
    }
}