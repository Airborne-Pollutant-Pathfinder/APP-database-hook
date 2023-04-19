# APP-database-hook

This database hook is responsible for fetching data from 3rd party APIs. It also provides a webhook for an application
to attach listeners to, so they can receive updates when new data is available. It uses Hibernate and Spark.

## Setup

1. The database must first be loaded before running the database hook. More instructions can be found 
   [here](https://github.com/Airborne-Pollutant-Pathfinder/APP-database).
2. Add the `APP_WEBHOOK_SECRET` environment variable. IntelliJ has native support for this if you edit the configuration
   settings. More detailed information on how to add environment variables to your IntelliJ run configurations can be 
   found 
   [here](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).

| Variable           | Example value                      | Description                                                             |
|--------------------|------------------------------------|-------------------------------------------------------------------------|
| APP_WEBHOOK_SECRET | 8jH1ctth4BQCPYRpp42DR6uvzzGa015N   | The secret of the webhook that is made to connect to the database hook. |

## Webhook

The APP-database-hook comes with a webhook to listen for when sensor data has been updated. The webhook
can be subscribed to for listeners to know when data has been updated. The webhook is located at
`/webhook`. The webhook is a POST request that requires a secret to be sent in the request header at parameter 
`X-Webhook-Secret`. The secret is the same as the `APP_WEBHOOK_SECRET` environment variable.

Below is an example of a webhook listener in Java:

```java
public class WebhookListener {
    private static final String WEBHOOK_URL = "https://my-webhook-url.com/webhook";
    private static final String WEBHOOK_SECRET = "my-webhook-secret";

    public void startListening() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(WEBHOOK_URL)
                .header("X-Webhook-Secret", WEBHOOK_SECRET)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle error
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle webhook response
                String responseBody = response.body().string();
                // Do something with the response body
            }
        });
    }
}
```