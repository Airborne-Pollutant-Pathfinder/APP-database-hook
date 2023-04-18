# APP-database-hook

This database hook is responsible for fetching data from 3rd party APIs. It also provides a webhook for an application
to attach listeners to so they can receive updates when new data is available. It uses jOOQ and Spark.

## Setup

1. The database must first be loaded before running the database hook. More instructions can be found 
   [here](https://github.com/Airborne-Pollutant-Pathfinder/APP-database).
2. Add the `APP_DB_URL`, `APP_DB_USERNAME`, and `APP_DB_PASSWORD` environment variables. IntelliJ has native support for 
   this if you edit the configuration settings. More detailed information on how to add environment variables to your 
   IntelliJ run configurations can be found 
   [here](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).

| Variable        | Example value                                             | Description                                    |
|-----------------|-----------------------------------------------------------|------------------------------------------------|
| APP_DB_URL      | `jdbc:mysql://localhost:3306/AirbornePollutantPathfinder` | The URL that the database can be connected at. |
| APP_DB_USERNAME | root                                                      | The username for the MySQL instance.           |
| APP_DB_PASSWORD | password                                                  | The password for the MySQL instance.           |

