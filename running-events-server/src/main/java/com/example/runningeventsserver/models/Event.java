package com.example.runningeventsserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document("events")
public class Event {
    @Id
    private String id;
    private String name;
    private String details;
    private Date date;
    private int maxParticipants;
    private Address address;
    private Route[] route;
    private List<ObjectId> userIds = new ArrayList<>();
}
