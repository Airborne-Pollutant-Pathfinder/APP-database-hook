# APP-database-hook

This database hook is responsible for fetching data from 3rd party APIs. It also provides an SSE for an application
to attach listeners to, so they can receive updates when new data is available. It uses Hibernate and Spring WebFlux.

## Setup

The database must first be loaded before running the database hook. More instructions can be found
[here](https://github.com/Airborne-Pollutant-Pathfinder/APP-database).

## Server-Sent Events (SSE)

The APP-database-hook comes with a Server-Sent Event stream to listen for when sensor data has been updated. The SSE
can be subscribed to for listeners to know when data has been updated. The SSE is located at
`/sse`, with an endpoint to connect to after (i.e. `/sse/captured-pollutant`).

Below is an example of an SSE listener in Java using Spring WebFlux:

```java
WebClient client = WebClient.create("http://localhost:8080/sse");
ParameterizedTypeReference<ServerSentEvent<CapturedPollutantUpdate>> type = new ParameterizedTypeReference<>() {};

Flux<ServerSentEvent<CapturedPollutantUpdate>> eventStream = client.get()
    .uri("/captured-pollutant")
    .retrieve()
    .bodyToFlux(type);

eventStream.subscribe(
    sse -> System.out.println("Received: " + sse.data()),
    err -> System.err.println("Error: " + err),
    () -> System.out.println("Done")
);
```