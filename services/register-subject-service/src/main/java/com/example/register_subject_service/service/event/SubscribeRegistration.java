package com.example.register_subject_service.service.event;

import com.eventstore.dbclient.*;
import com.example.register_subject_service.model.CourseRegistrationEvent;
import com.example.register_subject_service.model.RegisterResponse;
import com.example.register_subject_service.model.RegisterSubjectRequest;
import com.example.register_subject_service.model.Schedule;
import com.example.register_subject_service.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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


    @Value("${app.global.schedule-service-url}")
    private String scheduleURL;

    @Autowired
    public SubscribeRegistration(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            ServiceAPI serviceAPI,
            ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.objectMapper = objectMapper;
        this.serviceAPI = serviceAPI;
    }


    private List<RegisterResponse> processRegistrationEvent(CourseRegistrationEvent event) {
            System.out.println("Processing registration event: " + event);

            List<RegisterResponse> messages = new ArrayList<>();

            messages.addAll(this.validateConflictSchedule(event));
            messages.addAll(this.validateEnoughCredit(event));

            //fail validate
            if (!messages.isEmpty()) return messages;

            messages.add(RegisterResponse.builder()
                    .success(true)
                    .status(200L)
                    .message("Success")
                    .build());

            return messages;
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
                            CourseRegistrationEvent registrationEvent = objectMapper.readValue(
                                    jsonData, CourseRegistrationEvent.class);

//                            List<RegisterResponse> success = processtrueRegistrationEvent(registrationEvent);

                            if (2  > 1) {
                                // Báo xử lý thành công
                                System.out.println("send ACK");
                                subscription.ack(event);
                            } else {
                                // Báo xử lý thất bại, cần thử lại
                                System.out.println("send NACK");
                                subscription.nack(NackAction.Retry, "Failed to process", event);
                            }
                        } else {
                            // Process other
                        }
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

    public ArrayList<RegisterResponse> validateConflictSchedule(CourseRegistrationEvent event) {
        ArrayList<RegisterResponse> messages = new ArrayList<>();
        // get schedule list
        ArrayList<ArrayList<Schedule>> courseSchedules = new ArrayList<>();

        for (int i = 0; i < event.getCourseIds().size(); i++) {
            Long courseId = event.getCourseIds().get(i);
            ArrayList<Schedule> schedules = (ArrayList<Schedule>) this.serviceAPI.callForList(
                    this.scheduleURL + "/schedule?courseId=" + courseId,
                    HttpMethod.GET,
                    null,
                    Schedule.class,
                    event.getToken()
            );

            courseSchedules.add(schedules);
        }
        System.out.println("Call for schedule: " + courseSchedules);
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < courseSchedules.size(); i++) {
            for (int j = i + 1; j < courseSchedules.size(); j++) {
                for (int ii = 0; ii < courseSchedules.get(i).size(); ii++) {
                    for (int jj = 0; jj < courseSchedules.get(j).size(); jj++) {
                        Schedule scheduleI = mapper.convertValue(courseSchedules.get(i).get(ii), Schedule.class);
                        Schedule scheduleJ = mapper.convertValue(courseSchedules.get(j).get(jj), Schedule.class);
                        Long startTime1 = scheduleI.getStartTime().getTime();
                        Long endTime1 = scheduleI.getEndTime().getTime();
                        Long startTime2 = scheduleJ.getStartTime().getTime();
                        Long endTime2 = scheduleJ.getEndTime().getTime();

                        if (isConflictTime(startTime1, endTime1, startTime2, endTime2)) {
                            messages.add(RegisterResponse.builder()
                                    .success(false)
                                    .status(400L)
                                    .message("Môn " + "//TODO" + "trùng lịch với môn " + "//TODO")
                                    .build());
                        }

                    }
                }
            }
        }
        // have conflict
        if (!messages.isEmpty()) return messages;

        // else no conflict
        messages.add(RegisterResponse.builder()
                .success(true)
                .status(200L)
                .message("Success")
                .build());

        return messages;
    }

    public ArrayList<RegisterResponse> validateEnoughCredit(CourseRegistrationEvent event) {
        ArrayList<RegisterResponse> messages = new ArrayList<>();

        messages.add(RegisterResponse.builder()
                .success(true)
                .status(200L)
                .message("Success")
                .build());

        return messages;
    }

    public boolean isConflictTime(Long startTime1, Long endTime1, Long startTime2, Long endTime2) {
        return !(endTime1 <= startTime2 || startTime1 >= endTime2);
    }

}