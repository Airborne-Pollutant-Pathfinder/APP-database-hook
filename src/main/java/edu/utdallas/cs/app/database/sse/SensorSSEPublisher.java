package edu.utdallas.cs.app.database.sse;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
public class SensorSSEPublisher implements SSEPublisher<SensorUpdate> {
    private final List<Consumer<SensorUpdate>> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void subscribe(Consumer<SensorUpdate> listener) {
        listeners.add(listener);
    }

    @Override
    public void publish(SensorUpdate update) {
        listeners.forEach(listener -> listener.accept(update));
    }
}
