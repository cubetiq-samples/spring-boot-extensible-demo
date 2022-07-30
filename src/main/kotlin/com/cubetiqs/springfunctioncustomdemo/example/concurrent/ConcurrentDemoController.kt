package com.cubetiqs.springfunctioncustomdemo.example.concurrent

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

@RestController
@RequestMapping("/concurrent")
class ConcurrentDemoController constructor(
    private val concurrentService: ConcurrentService,
) {
    companion object {
        private val lock = ReentrantLock()
    }

    @GetMapping("/executionSync")
    // @Synchronized
    fun executionSync(): Any {
        val threadName = Thread.currentThread().name

        // validate the current access with lock
        while (lock.isLocked) {
            println("Waiting in thread: $threadName")
            TimeUnit.SECONDS.sleep(2)
        }

        return try {
            lock.lock()
            println("Executing is locked on thread: $threadName")
            // do huge or long execution
            TimeUnit.SECONDS.sleep(15)
            mapOf(
                "thread" to threadName,
                "status" to "OK",
            )
        } finally {
            lock.unlock()
        }
    }

    @GetMapping("/executionAsync")
    fun executionAsync(): Any {
        val threadName = Thread.currentThread().name
        concurrentService.executionLongProcessAsync()
        return mapOf(
            "thread" to threadName,
            "status" to "OK",
        )
    }
}