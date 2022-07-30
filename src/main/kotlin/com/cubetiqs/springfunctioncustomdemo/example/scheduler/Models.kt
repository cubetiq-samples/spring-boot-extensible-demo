package com.cubetiqs.springfunctioncustomdemo.example.scheduler

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class JobKey @JsonCreator constructor(@JsonProperty val key: String) : java.io.Serializable {
    companion object {
        fun newId(id: String? = null): JobKey {
            return JobKey(id ?: UUID.randomUUID().toString())
        }
    }
}

open class JobDetails(
    var key: JobKey,
    @JsonIgnore var task: JobRunnable,
    // if true, add to local executor scheduler
    var localScheduler: Boolean? = null,
) : java.io.Serializable

class JobStartDateDetails(key: JobKey, task: JobRunnable, localScheduler: Boolean?, val startDate: Date) :
    JobDetails(key, task, localScheduler)

data class JobFunction @JsonCreator constructor(@JsonProperty val function: String) {
    init {
        if (!TaskApiServiceFactory.isAllow(function)) {
            throw IllegalArgumentException("Function not found!")
        }
    }
}

data class JobRequest(
    @JsonProperty val key: JobKey? = null,
    @JsonProperty val function: JobFunction,
    @JsonProperty val startDate: Long? = null,
    // if true, add to local executor scheduler
    @JsonProperty var localScheduler: Boolean? = null,
) : java.io.Serializable