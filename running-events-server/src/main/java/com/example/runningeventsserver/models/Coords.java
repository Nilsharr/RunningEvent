package com.example.runningeventsserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Coords {
    private double latitude;
    private double longitude;
    private double altitude;
    private double accuracy;
    private double heading;
    private double speed;
}
