package com.example.register_subject_background_service.service.BackgroundJob;

import com.example.register_subject_background_service.model.CourseRegistrationEvent;
import com.example.register_subject_background_service.model.OutBoxMessage;
import com.example.register_subject_background_service.model.ReserveSlotEvent;
import com.example.register_subject_background_service.model.UpdateReadModelEvent;
import com.example.register_subject_background_service.repository.OutBoxMessageRepository;
import com.example.register_subject_background_service.service.event.SaveRegistrationEvent;
import com.example.register_subject_background_service.service.event.SaveReserveSlotEvent;
import com.example.register_subject_background_service.service.event.SaveUpdateReadModelEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@Service  // Changed from @Component to @Service
public class OutboxProcessor {
    @Autowired
    private  final SaveRegistrationEvent saveRegistrationEvent;
    private  final SaveReserveSlotEvent saveReserveSlotEvent;
    private  final OutBoxMessageRepository outBoxMessageRepository;
    private final SaveUpdateReadModelEvent saveUpdateReadModelEvent;
    private static final int BATCH_SIZE = 100; // Process 100 records at a time

    // This method will run every 1 second (1000 milliseconds)
    @Scheduled(fixedDelay = 200)
    public void processOutbox() {
        System.out.println("processing outbox");
        boolean isContinue = true;
        do {
            Pageable pageable = PageRequest.of(0, BATCH_SIZE);
            List<OutBoxMessage> outboxMessages = outBoxMessageRepository.findAllByDeletedAtIsNull(pageable);
            System.out.println("outboxMessages discovery: " + outboxMessages);
            if(outboxMessages.isEmpty()) {
                isContinue = false;
            }

            for (OutBoxMessage outboxMessage : outboxMessages) {
                try {
                    if(outboxMessage.getEventType().equals("CourseRegistrationEvent")) {
                        CourseRegistrationEvent event = new ObjectMapper().readValue(outboxMessage.getPayload(), CourseRegistrationEvent.class);
                        this.saveRegistrationEvent.call(event,"registration"); // Process each record();
                    }
                    else if(outboxMessage.getEventType().equals("ReserveSlotEvent")) {
                        ReserveSlotEvent event = new ObjectMapper().readValue(outboxMessage.getPayload(), ReserveSlotEvent.class);
                        this.saveReserveSlotEvent.call(event,"reserve-slot"); // Process each record();
                    }
                    else if(outboxMessage.getEventType().equals("UpdateReadModelEvent")) {
                        UpdateReadModelEvent event = new ObjectMapper().readValue(outboxMessage.getPayload(), UpdateReadModelEvent.class);
                        this.saveUpdateReadModelEvent.call(event,"update-read-model"); // Process each record();
                    }
                    outboxMessage.setDeletedAt(new java.util.Date());
                    outBoxMessageRepository.save(outboxMessage);

                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        while (isContinue);
    }
}