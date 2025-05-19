package com.example.enrollment_service.service.event;

import com.eventstore.dbclient.*;
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
    private final SubscribeReserveSlot subscribeReserveSlot;

    private  final SubscribeCourse subscribeCourse;

    @Value("${app.global.stream}")
    private String stream ;

    @Value("${app.global.group}")
    private String group;

    @Autowired
    public EventStoreService(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            CreatePersistentSubscription createPersistentSubscription,
            SubscribeReserveSlot subscribeReserveSlot,
            SubscribeCourse subscribeCourse
    ) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.createPersistentSubscription = createPersistentSubscription;
        this.subscribeReserveSlot = subscribeReserveSlot;
        this.subscribeCourse = subscribeCourse;
    }


    @PostConstruct
    public void init() throws Exception {

        // 1. Tạo persistent subscription
        this.createPersistentSubscription.call(stream, group);
        this.createPersistentSubscription.call("update-read-model", "update-read-model");


        // 2. Đăng ký lắng nghe
        this.subscribeReserveSlot.call(stream, group);
        this.subscribeCourse.call("course-transaction", "course-transaction");

        System.out.println("EventStore service initialized with persistent subscription");
    }

}