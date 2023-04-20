package edu.utdallas.cs.app.database.sse;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
public class CapturedPollutantSSEPublisher implements SSEPublisher<CapturedPollutantUpdate> {
    private final List<Consumer<CapturedPollutantUpdate>> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void subscribe(Consumer<CapturedPollutantUpdate> listener) {
        listeners.add(listener);
    }

    @Override
    public void publish(CapturedPollutantUpdate liveScore) {
        listeners.forEach(listener -> listener.accept(liveScore));
    }
}
