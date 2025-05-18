package com.example.enrollment_service.service.event;

import com.eventstore.dbclient.*;
import com.example.enrollment_service.model.ReserveSlotEvent;
import com.example.enrollment_service.service.EnrollmentService;
import com.example.enrollment_service.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.Base64;


@Service
public class SubscribeReserveSlot {

    // Dùng để ghi sự kiện
    private final EventStoreDBClient eventStoreDBClient;

    // Dùng để quản lý persistent subscriptions
    private final EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient;

    private final ObjectMapper objectMapper;

    private ServiceAPI serviceAPI;

    private EnrollmentService enrollmentService;

    @Autowired
    public SubscribeReserveSlot(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            ServiceAPI serviceAPI,
            EnrollmentService enrollmentService,
            ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.objectMapper = objectMapper;
        this.serviceAPI = serviceAPI;
        this.enrollmentService = enrollmentService;
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
                        if ("ReserveSlotEvent".equals(eventType)) {
                            ReserveSlotEvent reserveSlotEvent = objectMapper.readValue(jsonData, ReserveSlotEvent.class);
                            System.out.println("this is ReserveSlotEvent");
                            System.out.println(reserveSlotEvent);
                            enrollmentService.reserve(reserveSlotEvent);
                        }
                        System.out.println("send ACK");
                        subscription.ack(event);

                    } catch (Exception e) {
                        System.err.println("Error processing event: " + e.getMessage());
                        e.printStackTrace();

                        // Nếu số lần thử lại vượt quá ngưỡng, có thể xử lý khác
                        if (retryCount > 10) {
                            System.err.println("Too many retries (" + retryCount + "), moving to park");
                            subscription.nack(NackAction.Park, "Too many retries: " + e.getMessage(), event);
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



}