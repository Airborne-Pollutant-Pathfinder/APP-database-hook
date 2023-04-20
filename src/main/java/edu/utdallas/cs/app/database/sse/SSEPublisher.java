package edu.utdallas.cs.app.database.sse;

import java.util.function.Consumer;

public interface SSEPublisher<T> {
    void subscribe(Consumer<T> listener);

    void publish(T result);
}
