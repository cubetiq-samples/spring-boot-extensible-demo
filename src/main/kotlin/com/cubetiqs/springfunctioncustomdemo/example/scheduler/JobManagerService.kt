package com.cubetiqs.springfunctioncustomdemo.example.scheduler

import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ScheduledFuture

@Service
class JobManagerService constructor(
    private val taskScheduler: TaskScheduler,
) {
    companion object {
        private val jobs: ConcurrentMap<JobKey, JobDetails> = ConcurrentHashMap()
    }

    // Local executors' scheduler (in memory)
    private val localSchedulers: ConcurrentMap<JobKey, ScheduledFuture<*>> = ConcurrentHashMap()

    fun getAll(): Collection<JobDetails> {
        return jobs.values
    }

    fun schedule(details: JobDetails) {
        jobs[details.key] = details

        // If allow local executor
        if (details.localScheduler == true) {
            localSchedulers[details.key] = when (details) {
                is JobStartDateDetails -> taskScheduler.schedule(details.task, details.startDate)
                else -> throw IllegalArgumentException("Not support details!")
            }
        }
    }

    fun execute(key: JobKey) {
        jobs[key]?.task?.run()
    }

    fun cancel(key: JobKey) {
        localSchedulers[key]?.cancel(true)
    }

    fun removeThenCancel(key: JobKey) {
        localSchedulers.remove(key)?.cancel(true)
    }
}