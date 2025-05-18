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

    @Value("${app.global.stream}")
    private String stream ;

    @Value("${app.global.group}")
    private String group;

    @Autowired
    public EventStoreService(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            CreatePersistentSubscription createPersistentSubscription,
            SubscribeReserveSlot subscribeReserveSlot
    ) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.createPersistentSubscription = createPersistentSubscription;
        this.subscribeReserveSlot = subscribeReserveSlot;
    }


    @PostConstruct
    public void init() throws Exception {

        // 1. Tạo persistent subscription
        this.createPersistentSubscription.call(stream, group);

        // 2. Đăng ký lắng nghe
        this.subscribeReserveSlot.call(stream, group);

        System.out.println("EventStore service initialized with persistent subscription");
    }

}


//}