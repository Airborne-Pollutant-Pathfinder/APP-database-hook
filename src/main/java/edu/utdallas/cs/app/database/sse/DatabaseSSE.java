package edu.utdallas.cs.app.database.sse;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/sse")
public class DatabaseSSE {
    private final SensorSSEPublisher publisher;

    public DatabaseSSE(SensorSSEPublisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping(path = "/captured-pollutant", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> capturedPollutant() {
        return Flux.create(sink -> publisher.subscribe(sink::next))
                .map(result -> ServerSentEvent.builder()
                        .data(result)
                        .event("captured")
                        .build());
    }
}
