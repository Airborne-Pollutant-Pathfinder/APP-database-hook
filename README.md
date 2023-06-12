# APP-database-hook

This database hook is responsible for fetching data from 3rd party APIs. It also provides an SSE for an application
to attach listeners to, so they can receive updates when new data is available. It uses Hibernate and Spring WebFlux.

## Video

Click below to see our video for the app.

[![Watch the video](https://i.imgur.com/OBm9FRB.png)](https://youtu.be/z_J9tR2n-vY)

## Setup

1. The database must first be loaded before running the database hook. More instructions can be found
[here](https://github.com/Airborne-Pollutant-Pathfinder/APP-database).
2. Add the `MINTS_BUCKET`, `MINTS_ORG`, `MINTS_TOKEN`, `MINTS_URL`, and `OPENWEATHER_TOKEN` environment variables. IntelliJ has native 
   support for this if you edit the configuration settings. More detailed information on how to add environment
   variables to your IntelliJ run configurations can be found
   [here](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).

## Server-Sent Events (SSE)

The APP-database-hook comes with a Server-Sent Event stream to listen for when sensor data has been updated. The SSE
can be subscribed to for listeners to know when data has been updated. The SSE is located at
`/sse`, with an endpoint to connect to after (i.e. `/sse/sensor`). The port is at port `8081`.

Below is an example of an SSE listener in Java using Spring WebFlux:

```java
WebClient client = WebClient.create("http://localhost:8081/sse");
ParameterizedTypeReference<ServerSentEvent<SensorUpdate>> type = new ParameterizedTypeReference<>() {};

Flux<ServerSentEvent<SensorUpdate>> eventStream = client.get()
    .uri("/sensor")
    .retrieve()
    .bodyToFlux(type);

eventStream.subscribe(
    sse -> System.out.println("Received: " + sse.data()),
    err -> System.err.println("Error: " + err),
    () -> System.out.println("Done")
);
```

## Problem Troubleshooting

**Q: For some weird reason, the program just pauses execution on a line in FetchDataTask.**

A: There is a glitch currently where FetchDataTask does not output errors. So, if you are running the program and it
pauses on a line in FetchDataTask, it is most likely because there is an error. To fix this, you can add a breakpoint
on the line that it is pausing on and run the program in debug mode. This will allow you to see the line that is pausing.
Then, you can move the code to Main.java to see the error.

It's quite a bad solution really, so it might be worth looking into why errors don't output. Perhaps it's because of
ScheduledExecutorService?
