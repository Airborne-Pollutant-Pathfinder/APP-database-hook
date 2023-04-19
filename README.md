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

