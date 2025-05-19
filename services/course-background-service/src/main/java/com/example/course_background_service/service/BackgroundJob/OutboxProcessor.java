package com.example.course_background_service.service.BackgroundJob;

import com.example.course_background_service.model.ChangeQuantitySlotEvent;
import com.example.course_background_service.model.CommitEvent;
import com.example.course_background_service.model.OutBoxMessage;
import com.example.course_background_service.model.RollBackEvent;
import com.example.course_background_service.repository.OutBoxMessageRepository;
import com.example.course_background_service.service.event.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@Service  // Changed from @Component to @Service
public class OutboxProcessor {
    @Autowired
    private  final SaveRegistrationEvent saveRegistrationEvent;
    private  final SaveReserveSlotEvent saveReserveSlotEvent;
    private final SaveChangeQuantitySlotEvent saveChangeQuantitySlotEvent;
    private SaveCommitEvent saveCommitEvent;
    private SaveRollbackEvent saveRollbackEvent;
    private  final OutBoxMessageRepository outBoxMessageRepository;
    private static final int BATCH_SIZE = 100; // Process 100 records at a time

    // This method will run every 1 second (1000 milliseconds)
    @Scheduled(fixedDelay = 5000)
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
                    if(outboxMessage.getEventType().equals("CommitChangeQuantitySlotEvent")) {
                        CommitEvent event = new ObjectMapper().readValue(outboxMessage.getPayload(), CommitEvent.class);
                        this.saveCommitEvent.call(event,"course-transaction"); // Process each record();
                    }
                    else if(outboxMessage.getEventType().equals("RollBackChangeQuantitySlotEvent")) {
                        RollBackEvent event = new ObjectMapper().readValue(outboxMessage.getPayload(), RollBackEvent.class);
                        this.saveRollbackEvent.call(event,"course-transaction"); // Process each record();
                    }
                    outboxMessage.setDeletedAt(new Date());
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