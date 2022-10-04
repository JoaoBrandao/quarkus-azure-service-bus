# Quarkus Application - Consuming events from Service Bus

The provided application shows a basic example how to consume events from Azure Service Bus using 
Quarkus application.
This example relies on is a simple String message from Service Bus and it will log the event ID and 
message data.



## How to consume from your own Azure Service Bus

Go to <strong>application properties</strong> and add the respective configurations of your Service Bus.

```
azure.service-bus.connection-string=<<<SERVICE_BUS_CONNECTION_STRING>>>
azure.service-bus.topic=<<SERVICE_BUS_TOPIC_NAME>>
azure.service-bus.topic.subscription=<<SERVICE_BUS_SUBSCRIPTION_NAME>>
```


## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
mvn quarkus:dev
```
