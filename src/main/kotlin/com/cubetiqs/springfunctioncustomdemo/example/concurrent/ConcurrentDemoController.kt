package com.cubetiqs.springfunctioncustomdemo.example.concurrent

import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Async
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import java.util.function.Consumer
import javax.servlet.http.HttpServletResponse

@Component
class MessageCreatedEventPublisher(private val executor: Executor) : ApplicationListener<MessageCreatedEvent>,
    Consumer<FluxSink<MessageCreatedEvent>> {
    private val queue: BlockingQueue<MessageCreatedEvent> = LinkedBlockingQueue()

    override fun onApplicationEvent(event: MessageCreatedEvent) {
        this.queue.offer(event)
    }

    override fun accept(sink: FluxSink<MessageCreatedEvent>) {
        executor.execute {
            while (true) {
                try {
                    val event = queue.take()
                    sink.next(event)
                } catch (e: InterruptedException) {
                    ReflectionUtils.rethrowException(e)
                }
            }
        }
    }
}

data class MessageCreatedEvent(val message: String) : ApplicationEvent(message)

@RestController
@RequestMapping("/concurrent")
class ConcurrentDemoController constructor(
    private val concurrentService: ConcurrentService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val eventPublisher: MessageCreatedEventPublisher,
) {
    private val events: Flux<MessageCreatedEvent>

    init {
        events = Flux.create(eventPublisher)
    }

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

    @GetMapping("/getAuth")
    fun setAuthentication(
        @RequestParam(value = "name", defaultValue = "admin") name: String,
        @RequestParam(value = "counter", defaultValue = "1") counter: String,
        response: HttpServletResponse,
    ) {
        println("Counter request: $counter")
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(name, null, listOf())
        val atomic = AtomicInteger(0)
        while (true) {
            println("[$counter] Thread: ${Thread.currentThread().name} and Auth: ${SecurityContextHolder.getContext().authentication?.name}")
            if (atomic.incrementAndGet() > 5) {
                println("[$counter] died!")
                return
            }
            TimeUnit.SECONDS.sleep(2)
        }
    }

    @GetMapping("/getAuthFlux", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun setAuthentication(
        @RequestParam(value = "name", defaultValue = "admin") name: String,
        @RequestParam(value = "counter", defaultValue = "1") counter: String,
    ): Flux<Any> {
        println("Counter request: $counter")
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(name, null, listOf())

        MyValue.a = 2
        return Flux.generate { sink ->
            TimeUnit.SECONDS.sleep(5)
            sink.next("[$counter] Thread: ${Thread.currentThread().name} and Auth: ${SecurityContextHolder.getContext().authentication?.name}")
        }
    }

    private val executor = Executors.newFixedThreadPool(1000)

    @GetMapping("/getNonBlocking")
    fun getNonBlocking(
        @RequestParam(value = "name", defaultValue = "admin") name: String,
        @RequestParam(value = "counter", defaultValue = "1") counter: String,
    ): CompletableFuture<String> {
        println("[${Thread.currentThread().name}] Counter request: $counter")
        SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(name, null, listOf())
        return CompletableFuture.supplyAsync(
            {
                TimeUnit.SECONDS.sleep(5)
                "[$counter] Thread: ${Thread.currentThread().name} and Auth: ${SecurityContextHolder.getContext().authentication?.name}"
            }, executor
        )
    }

    @Async
    fun runAsync(counter: String): Flux<Any> {
        TimeUnit.SECONDS.sleep(5)
        return Flux.just("[$counter] Thread: ${Thread.currentThread().name} and Auth: ${SecurityContextHolder.getContext().authentication?.name}")
    }
}

object MyValue {
    var a = 0
}