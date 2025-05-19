package com.example.course_background_service.service.event;

import com.eventstore.dbclient.*;
import com.example.course_background_service.model.CourseRegistrationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class SaveRegistrationEvent {

    // Dùng để ghi sự kiện
    private final EventStoreDBClient eventStoreDBClient;

    // Dùng để quản lý persistent subscriptions
    private final EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient;

    private final ObjectMapper objectMapper;


    @Autowired
    public SaveRegistrationEvent(
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
    public void call(CourseRegistrationEvent event,String stream) throws Exception {

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