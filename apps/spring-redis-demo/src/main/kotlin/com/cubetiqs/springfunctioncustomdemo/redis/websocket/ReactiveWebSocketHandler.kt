package com.cubetiqs.springfunctioncustomdemo.redis.websocket

import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@Component
class ReactiveWebSocketHandler : WebSocketHandler {
    val sessions = mutableMapOf<String, WebSocketSession>()

    override fun handle(session: WebSocketSession): Mono<Void> {
        return session.send(Flux.interval(Duration.ofSeconds(1)).map { session.textMessage("Message $it") })
            .and(session.receive()
                .map { msg -> msg.payloadAsText }
                .doOnSubscribe {
                    println("Subscribed by client id: ${session.id}")
                    sessions[session.id] = session
                }
                .doOnComplete {
                    println("Completed by client id: ${session.id}")
                    sessions.remove(session.id)
                }
                .log()
            )
    }
}