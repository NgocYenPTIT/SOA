package com.example.course_background_service.service.event;

import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;
import com.example.course_background_service.model.CommitEvent;
import com.example.course_background_service.model.RollBackEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class SaveRollbackEvent {

    // Dùng để ghi sự kiện
    private final EventStoreDBClient eventStoreDBClient;

    // Dùng để quản lý persistent subscriptions
    private final EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient;

    private final ObjectMapper objectMapper;


    @Autowired
    public SaveRollbackEvent(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.objectMapper = objectMapper;
    }



    public void call(RollBackEvent event, String stream) throws Exception {

        System.out.println("Saving event..." + event + " to stream: " + stream);

        EventData eventData = EventData.builderAsJson(
                UUID.randomUUID(),
                event.getEventType(),
                objectMapper.writeValueAsBytes(event)).build();

        AppendToStreamOptions options = AppendToStreamOptions.get();
        eventStoreDBClient.appendToStream(stream, options, eventData).get();

        System.out.println("Event successfully saved to stream: " + stream);
    }

}