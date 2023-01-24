package com.example.runningeventsserver.dtos;

import com.example.runningeventsserver.models.Address;
import com.example.runningeventsserver.models.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private String id;
    @NotBlank
    @Size(max = 50, message = "Name must have less than 50 characters")
    private String name;
    @Size(max = 500, message = "Details must have less than 500 characters")
    private String details;
    @NotNull
    @Future(message = "The event can take place tomorrow at the earliest")
    private Date date;
    @Min(value = 1, message = "The event must have at least one participant")
    @Max(value = 99999, message = "The event must have less than 100000 participants")
    private int maxParticipants;
    @Valid
    @NotNull
    private Address address;
    @NotNull
    @NotEmpty
    private Route[] route;

    private List<String> participants;
}
