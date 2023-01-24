package com.example.runningeventsserver.dtos.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventError {
    private String lacksCountry;
    private String lacksCity;
    private String lacksStreet;
    private String invalidDate;
    private String notEnoughParticipants;
    private String tooManyParticipants;
}
