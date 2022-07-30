package com.cubetiqs.springfunctioncustomdemo.example.concurrent

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class ConcurrentService {
    @Async
    fun executionLongProcessAsync() {
        println("Your job is executing its work....")
        TimeUnit.SECONDS.sleep(10)
        println("Your job executed!!!")
    }
}