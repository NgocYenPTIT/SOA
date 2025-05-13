package com.example.register_subject_service.config;

import com.eventstore.dbclient.ConnectionStringParsingException;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreDBConfig {

    @Value("${eventstoredb.connection.uri}")
    private String connectionString;

    @Bean
    public EventStoreDBClientSettings eventStoreDBClientSettings() throws ConnectionStringParsingException {
        return EventStoreDBConnectionString.parse(connectionString);
    }

    @Bean
    public EventStoreDBClient eventStoreDBClient(EventStoreDBClientSettings settings) {
        return EventStoreDBClient.create(settings);
    }

    @Bean
    public EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient(EventStoreDBClientSettings settings) {
        return EventStoreDBPersistentSubscriptionsClient.create(settings);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}