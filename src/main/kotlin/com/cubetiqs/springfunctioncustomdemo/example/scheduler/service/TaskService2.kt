package com.cubetiqs.springfunctioncustomdemo.example.scheduler.service

import org.springframework.stereotype.Service

@Service
class TaskService2 {
    fun doTask1() {
        println("[TaskService2] Task 1 is working...")
    }

    fun doTask2() {
        println("[TaskService2] Task 2 is working...")
    }
}