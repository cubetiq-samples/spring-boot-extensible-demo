package com.cubetiqs.springfunctioncustomdemo.example.scheduler

import com.cubetiqs.springfunctioncustomdemo.example.scheduler.service.TaskService1
import com.cubetiqs.springfunctioncustomdemo.example.scheduler.service.TaskService2
import org.springframework.stereotype.Component
import java.util.*

@Component
class TaskApiServiceFactory constructor(
    private val taskService1: TaskService1,
    private val taskService2: TaskService2,
) {
    companion object {
        private val functions = listOf("service_1:task1", "service_1:task2", "service_2:task1", "service_2:task2")
        fun isAllow(function: String) = functions.any { it == function }
    }

    fun execute(function: String) {
        when (function) {
            "service_1:task1" -> taskService1.doTask1()
            "service_1:task2" -> taskService1.doTask2()
            "service_2:task1" -> taskService2.doTask1()
            "service_2:task2" -> taskService2.doTask2()
            else -> println("No service task found")
        }
    }

    fun fromJobRequest(request: JobRequest): JobDetails {
        val task = JobRunnable { execute(request.function.function) }

        return if (request.startDate != null) {
            JobStartDateDetails(
                request.key ?: JobKey.newId(),
                task,
                startDate = Date(request.startDate),
                localScheduler = request.localScheduler,
            )
        } else {
            JobDetails(request.key ?: JobKey.newId(), task, localScheduler = request.localScheduler)
        }
    }
}