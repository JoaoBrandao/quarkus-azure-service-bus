package com.jb.demo.boundary;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EventConsumer {

  private static final Logger LOG = Logger.getLogger(EventConsumer.class);
  private final ServiceBusProcessorClient consumer;


  public EventConsumer(
      @ConfigProperty(name = "azure.service-bus.connection-string") String connectionString,
      @ConfigProperty(name = "azure.service-bus.topic") String topicName,
      @ConfigProperty(name = "azure.service-bus.topic.subscription") String subscription) {


    consumer = new ServiceBusClientBuilder()
        .connectionString(connectionString)
        .processor()
        .topicName(topicName)
        .subscriptionName(subscription)
        .processMessage(this::processEvent)
        .processError(this::processError)
        .buildProcessorClient();
  }

  /**
   * According to lifecycle of application, this method will be invoked soon as application starts.
   *
   * @param ev contains context of application
   */
  void start(@Observes StartupEvent ev) {
    consumer.start();
  }

  /**
   * According to lifecycle of application, this method will be invoked when as application shutting down.
   *
   * @param ev contains context of application
   */
  void onStop(@Observes ShutdownEvent ev) {
    consumer.close();

  }

  /**
   * Method receives event from Azure Service Bus and process it
   *
   * @param context contains message data such as properties and message
   */
  private void processEvent(final ServiceBusReceivedMessageContext context) {
    final ServiceBusReceivedMessage messageContext = context.getMessage();
    LOG.infov("Processing Event data #{0}, Contents: {1}", messageContext.getSequenceNumber(), messageContext.getBody());

  }


  /**
   * According to Azure, there are some use cases where the service bus SDK can throw an exception when trying to
   * consume that event from Service Bus. In order to process those exceptions we must provide a method that will
   * receive that Context.
   *
   * @param context
   */
  private void processError(final ServiceBusErrorContext context) {
    LOG.error("Error when processing event data: " + context.getException(), context.getException());
  }

}
