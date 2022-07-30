package com.cubetiqs.springfunctioncustomdemo.example.scheduler

import org.springframework.web.bind.annotation.*
import java.util.Date

@RestController
@RequestMapping("/scheduler")
class SchedulerDemoController constructor(
    private val jobManagerService: JobManagerService,
    private val taskApiServiceFactory: TaskApiServiceFactory,
) {
    @GetMapping
    fun getAll(): Any {
        return jobManagerService.getAll()
    }

    @GetMapping("/newDate")
    fun getNewDate(
        @RequestParam(name = "addSeconds", defaultValue = "0") addSeconds: Long,
    ): Any {
        val now = Date().time
        return Date(now + (addSeconds * 1000)).time
    }

    @GetMapping("/execute/{jobKey}")
    fun executeByJobKey(
        @PathVariable jobKey: JobKey,
    ): Any {
        jobManagerService.execute(jobKey)
        return mapOf(
            "status" to "OK",
            "operation" to "EXECUTE",
            "jobKey" to jobKey,
        )
    }

    @GetMapping("/remove/{jobKey}")
    fun removeByJobKey(
        @PathVariable jobKey: JobKey,
    ): Any {
        jobManagerService.removeThenCancel(jobKey)
        return mapOf(
            "status" to "OK",
            "operation" to "REMOVE",
            "jobKey" to jobKey,
        )
    }

    @PostMapping
    fun create(
        @RequestBody request: JobRequest,
    ): Any {
        jobManagerService.schedule(taskApiServiceFactory.fromJobRequest(request))
        return request
    }
}