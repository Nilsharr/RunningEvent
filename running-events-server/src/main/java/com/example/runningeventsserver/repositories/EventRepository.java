package com.example.runningeventsserver.repositories;

import com.example.runningeventsserver.models.Event;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByUserIdsContains(ObjectId userId);

}
