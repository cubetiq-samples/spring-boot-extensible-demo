package com.cubetiqs.springfunctioncustomdemo.redis

import com.cubetiqs.springfunctioncustomdemo.redis.websocket.ReactiveWebSocketHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import reactor.core.publisher.Flux
import java.time.Duration

@SpringBootApplication(
    scanBasePackages = ["com.cubetiqs.springfunctioncustomdemo.redis"]
)
class SpringRedisDemoApplication @Autowired constructor(
    private val webSocketHandler: ReactiveWebSocketHandler,
) {
    @Bean
    fun webSocketHandlerMapping(): HandlerMapping {
        val map = mutableMapOf<String, WebSocketHandler>()
        map["/ws"] = webSocketHandler
        val handlerMapping = SimpleUrlHandlerMapping()
        handlerMapping.order = 1
        handlerMapping.urlMap = map
        return handlerMapping
    }
}

fun main(args: Array<String>) {
    runApplication<SpringRedisDemoApplication>(*args)
}

@RestController
class HelloController {
    @GetMapping("/hello")
    fun hello() = "Hello World"
}

@RestController
class KeyValueStoreController(
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
) {
    @GetMapping("/set")
    fun set(key: String, value: String) = redisTemplate.opsForValue().set(key, value)

    @GetMapping("/get")
    fun get(key: String) = redisTemplate.opsForValue().get(key)

    // Get all keys
    @GetMapping("/keys")
    fun keys() = redisTemplate.keys("*").collectList()

    // Publish a message to channel
    @GetMapping("/publish")
    fun publish(channel: String, message: String) = redisTemplate.convertAndSend(channel, message)

    // Generate by N times
    @GetMapping("/generate")
    fun generate(channel: String, n: Int) = Flux.range(1, n)
        .map { "Message $it" }
        .flatMap { redisTemplate.convertAndSend(channel, it) }
        .collectList()

    // Subscribe to channel
    @GetMapping("/subscribe")
    fun subscribe(channel: String) = redisTemplate.listenToChannel(channel).doOnCancel {
        println("Cancelled")
    }

    // Cancellable endpoint
    @GetMapping("/test-cancel")
    fun testCancel(
        request: ServerHttpRequest,
    ) = Flux.interval(Duration.ofSeconds(1))
        .map { "Message $it\n" }
        .doOnSubscribe {
            println("Subscribed from client id: ${request.id} with thread: ${Thread.currentThread().name}")
        }
        .doOnCancel {
            println("Cancelled from client id: ${request.id} with thread: ${Thread.currentThread().name}")
        }
}

@RestController
class WebSocketController @Autowired constructor(
    private val webSocketHandler: ReactiveWebSocketHandler,
) {
    @GetMapping("/ws/send")
    fun sendToWs(message: String): Flux<String> {
        // Send to all websocket clients
        return Flux.fromIterable(webSocketHandler.sessions.values)
            .flatMap { it.send(Flux.just(WebSocketMessage())) }
            .thenMany(Flux.just("Sent to all clients"))
    }
}