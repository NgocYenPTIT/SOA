package com.example.register_subject_service.service.event;

import com.eventstore.dbclient.*;
import com.example.register_subject_service.model.CourseRegistrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
public class EventStoreService {

    // Dùng để ghi sự kiện
    private final EventStoreDBClient eventStoreDBClient;

    // Dùng để quản lý persistent subscriptions
    private final EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient;
    private final CreatePersistentSubscription createPersistentSubscription;
    private final SaveEvent saveEvent;
    private final SubscribeRegistration subscribeRegistration;

    @Value("${app.global.stream}")
    private String stream ;

    @Value("${app.global.group}")
    private String group;

    @Autowired
    public EventStoreService(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            CreatePersistentSubscription createPersistentSubscription,
            SaveEvent saveEvent,
            SubscribeRegistration subscribeRegistration
    ) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.createPersistentSubscription = createPersistentSubscription;
        this.saveEvent = saveEvent;
        this.subscribeRegistration = subscribeRegistration;
    }


    @PostConstruct
    public void init() throws Exception {

        // 1. Tạo persistent subscription
        this.createPersistentSubscription.call(stream, group);

        // 2. Đăng ký lắng nghe
        this.subscribeRegistration.call(stream, group);

        System.out.println("EventStore service initialized with persistent subscription");
    }

    public  void  saveEvent(CourseRegistrationEvent event) throws Exception {
        this.saveEvent.call(event);
    }

}