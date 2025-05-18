package com.example.course_service.service.event;

import com.eventstore.dbclient.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CreatePersistentSubscription {

    // Dùng để ghi sự kiện
    private final EventStoreDBClient eventStoreDBClient;

    // Dùng để quản lý persistent subscriptions
    private final EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient;

    private final ObjectMapper objectMapper;

    @Autowired
    public CreatePersistentSubscription(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.objectMapper = objectMapper;
    }

    public void call(String streamName, String groupName) {
        try {

            // Tạo persistent subscription thông qua client chuyên biệt
            persistentSubscriptionsClient.createToStream(streamName, groupName);
            System.out.println("Created persistent subscription for stream: " + streamName + ", group: " + groupName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}