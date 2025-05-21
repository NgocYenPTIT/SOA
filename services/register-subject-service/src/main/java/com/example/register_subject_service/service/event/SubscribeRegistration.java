package com.example.register_subject_service.service.event;

import com.eventstore.dbclient.*;
import com.example.register_subject_service.model.*;
import com.example.register_subject_service.repository.OutBoxMessageRepository;
import com.example.register_subject_service.service.ProcessRegisterService;
import com.example.register_subject_service.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.Base64;


@Service
public class SubscribeRegistration {

    // Dùng để ghi sự kiện
    private final EventStoreDBClient eventStoreDBClient;

    // Dùng để quản lý persistent subscriptions
    private final EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient;

    private final ObjectMapper objectMapper;

    private  ServiceAPI serviceAPI;

    private ProcessRegisterService processRegisterService;

    private OutBoxMessageRepository outBoxMessageRepository;

    @Value("${app.global.schedule-service-url}")
    private String scheduleURL;

    @Autowired
    public SubscribeRegistration(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            ServiceAPI serviceAPI,
            OutBoxMessageRepository outBoxMessageRepository,
            ProcessRegisterService processRegisterService,
            ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.objectMapper = objectMapper;
        this.serviceAPI = serviceAPI;
        this.outBoxMessageRepository = outBoxMessageRepository;
        this.processRegisterService = processRegisterService;
    }




    public void call(String streamName, String groupName) {
        try {
            // Tạo listener cho persistent subscription
            PersistentSubscriptionListener listener = new PersistentSubscriptionListener() {
                @Override
                public void onEvent(PersistentSubscription subscription, int retryCount, ResolvedEvent event) {
                    try {
                        System.out.println("start onEvent");
                        String eventType = event.getOriginalEvent().getEventType();
                        byte[] eventData = event.getOriginalEvent().getEventData();
                        String baseText64 = new String(eventData, StandardCharsets.UTF_8);
                        String jsonData = new String(Base64.getDecoder().decode(baseText64.substring(1, baseText64.length() - 1)), StandardCharsets.UTF_8);
                        System.out.println(jsonData);
                        System.out.println("Received event: " + eventType + " from stream: " + streamName);
                        System.out.println("Retry count: " + retryCount);

//                         Nếu là CourseRegistrationEvent, xử lý
                        if ("CourseRegistrationEvent".equals(eventType)) {
                            CourseRegistrationEvent registrationEvent = objectMapper.readValue(jsonData, CourseRegistrationEvent.class);

                            processRegisterService.call(registrationEvent);
                        }
                        System.out.println("send ACK");
                        subscription.ack(event);

                    } catch (Exception e) {
                        System.err.println("Error processing event: " + e.getMessage());
                        e.printStackTrace();
                        // Nếu số lần thử lại vượt quá ngưỡng, có thể xử lý khác
                        if (retryCount > 9) {
                            try {
                                System.err.println("Too many retries (" + retryCount + "),");
                                byte[] eventData = event.getOriginalEvent().getEventData();
                                String baseText64 = new String(eventData, StandardCharsets.UTF_8);
                                String jsonData = new String(Base64.getDecoder().decode(baseText64.substring(1, baseText64.length() - 1)), StandardCharsets.UTF_8);
                                System.out.println(jsonData);
                                CourseRegistrationEvent registrationEvent = objectMapper.readValue(jsonData, CourseRegistrationEvent.class);

                                emitRollback(registrationEvent);
                                subscription.nack(NackAction.Park, "Too many retries: " + e.getMessage(), event);
                            }
                            catch (Exception ex) {
                                System.out.println("tambiet");
                                e.printStackTrace();
                            }
                        } else {
                            // Yêu cầu thử lại
                            subscription.nack(NackAction.Retry, "Exception: " + e.getMessage(), event);
                        }
                    }
                }

                @Override
                public void onError(PersistentSubscription subscription, Throwable throwable) {
                    System.err.println("Subscription error network or authenication : " + throwable.getMessage());
                    throwable.printStackTrace();
                }

                @Override
                public void onCancelled(PersistentSubscription subscription) {
                    System.out.println("Subscription cancelled");
                }
            };

            // Tạo options cho persistent subscription
            SubscribePersistentSubscriptionOptions options = SubscribePersistentSubscriptionOptions.get();

            // Đăng ký lắng nghe persistent subscription
            persistentSubscriptionsClient.subscribeToStream(
                    streamName, groupName, options, listener).get();

            System.out.println("Subscribed to persistent subscription - stream: " + streamName + ", group: " + groupName);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Error subscribing to persistent subscription: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void emitRollback(CourseRegistrationEvent event) throws Exception {
        String eventType = "UpdateReadModelEvent";

        UpdateReadModelEvent updateReadModelEvent = UpdateReadModelEvent.builder()
                .eventId(java.util.UUID.randomUUID())
                .eventType(eventType)
                .correlationId(event.getCorrelationId())
                .studentId(event.getStudentId())
                .success(false)
                .status("ROLLBACK")
                .messages(new ArrayList<>(Collections.singleton("")))
                .token(event.getToken())
                .timestamp(System.currentTimeMillis())
                .build();
        System.out.println("updateReadModelEvent: " + updateReadModelEvent);
        // Save outbox
        outBoxMessageRepository.save(OutBoxMessage.builder().eventType(eventType).payload(new ObjectMapper().writeValueAsString(updateReadModelEvent)).build());
        System.out.println("ROLLBACK FOR READ-MODEL");
    }

}