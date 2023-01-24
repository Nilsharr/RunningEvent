package com.example.runningeventsserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document("users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private boolean admin;
    private List<ObjectId> eventIds = new ArrayList<>();
}
