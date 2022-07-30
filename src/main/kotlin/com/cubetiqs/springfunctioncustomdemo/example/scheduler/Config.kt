package com.cubetiqs.springfunctioncustomdemo.example.scheduler

import org.springframework.boot.task.TaskSchedulerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
@EnableAsync
class Config {
    @Bean
    fun taskScheduler(): TaskScheduler {
        return TaskSchedulerBuilder()
            .threadNamePrefix("tasksched_")
            .build()
    }
}