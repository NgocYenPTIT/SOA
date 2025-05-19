package com.example.enrollment_background_service.service.event;

import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient;
import com.example.enrollment_background_service.model.CourseRegistrationEvent;
import com.example.enrollment_background_service.model.UpdateReadModelEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class SaveUpdateReadModelEvent {

    // Dùng để ghi sự kiện
    private final EventStoreDBClient eventStoreDBClient;

    // Dùng để quản lý persistent subscriptions
    private final EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient;

    private final ObjectMapper objectMapper;


    @Autowired
    public SaveUpdateReadModelEvent(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.objectMapper = objectMapper;
    }


/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Saves the provided CourseRegistrationEvent to the configured EventStoreDB stream.
     *
     * @param event the CourseRegistrationEvent to be saved
     * @throws Exception if there is an error during the event serialization or appending to the stream
     */

/* <<<<<<<<<<  1f2e19da-ca6d-48bf-b742-2fb9eb9729ad  >>>>>>>>>>> */
    public void call(UpdateReadModelEvent event, String stream) throws Exception {

        System.out.println("Saving event..." + event);

        EventData eventData = EventData.builderAsJson(
                UUID.randomUUID(),
                event.getEventType(),
                objectMapper.writeValueAsBytes(event)).build();

        AppendToStreamOptions options = AppendToStreamOptions.get();
        eventStoreDBClient.appendToStream(stream, options, eventData).get();

        System.out.println("Event successfully saved to stream: " + stream);
    }

}