package com.example.runningeventsserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Route {
    private long timestamp;
    private Coords coords;
}

