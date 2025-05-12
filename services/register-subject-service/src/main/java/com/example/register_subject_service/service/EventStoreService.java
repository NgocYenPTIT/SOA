package com.example.register_subject_service.service;

import com.eventstore.dbclient.*;
import com.example.register_subject_service.model.CourseRegistrationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class EventStoreService {

    private final EventStoreDBClient eventStoreDBClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public EventStoreService(EventStoreDBClient eventStoreDBClient, ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.objectMapper = objectMapper;
    }

    public void saveEvent(CourseRegistrationEvent event) throws Exception {
        String streamName = "course-registration-" + event.getStudentId();

        EventData eventData = EventData.builderAsJson(
                        UUID.randomUUID(),
                        "CourseRegistrationEvent",
                        objectMapper.writeValueAsBytes(event))
                .build();

        AppendToStreamOptions options = AppendToStreamOptions.get();
        eventStoreDBClient.appendToStream(streamName, options, eventData).get();
    }
}