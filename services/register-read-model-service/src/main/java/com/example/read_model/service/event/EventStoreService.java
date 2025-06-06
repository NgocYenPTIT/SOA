package com.example.read_model.service.event;

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

    private  final SubscribeUpdateReadModel subscribeUpdateReadModel;

    @Value("${app.global.stream}")
    private String stream ;

    @Value("${app.global.group}")
    private String group;

    @Autowired
    public EventStoreService(
            EventStoreDBClient eventStoreDBClient,
            EventStoreDBPersistentSubscriptionsClient persistentSubscriptionsClient,
            CreatePersistentSubscription createPersistentSubscription,
            SubscribeUpdateReadModel subscribeUpdateReadModel
    ) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.persistentSubscriptionsClient = persistentSubscriptionsClient;
        this.createPersistentSubscription = createPersistentSubscription;
        this.subscribeUpdateReadModel = subscribeUpdateReadModel;
    }


    @PostConstruct
    public void init() throws Exception {

        // 1. Tạo persistent subscription
        this.createPersistentSubscription.call(stream, group);


        // 2. Đăng ký lắng nghe
        this.subscribeUpdateReadModel.call(stream, group);

        System.out.println("EventStore service initialized with persistent subscription");
    }

}