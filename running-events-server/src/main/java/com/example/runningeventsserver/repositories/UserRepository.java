package com.example.runningeventsserver.repositories;

import com.example.runningeventsserver.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);

    Boolean existsByEmail(String email);
}
