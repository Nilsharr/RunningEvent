package com.example.runningeventsserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
public class Address {
    @NotBlank(message = "You must specify country")
    private String country;
    @NotBlank(message = "You must specify city")
    private String city;
    @NotBlank(message = "You must specify street")
    private String street;
}
