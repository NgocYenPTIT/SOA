
package com.example.register_subject_service.config;
import com.eventstore.dbclient.ConnectionStringParsingException;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBClientSettings;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreDBConfig {

    @Value("${eventstoredb.connection.uri}")
    private String connectionString;

    @Bean
    public EventStoreDBClient eventStoreDBClient() throws ConnectionStringParsingException {
        EventStoreDBClientSettings settings = EventStoreDBConnectionString.parse(connectionString);
        return EventStoreDBClient.create(settings);
    }
}