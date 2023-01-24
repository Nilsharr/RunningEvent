package com.example.runningeventsserver.dtos.errors.wrappers;

import com.example.runningeventsserver.dtos.errors.EventError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventErrorWrapper {

    private EventError eventError;

    public EventErrorWrapper(List<FieldError> fieldErrors) {
        this.eventError = new EventError();
        for (FieldError error : fieldErrors) {
            switch (error.getField()) {
                case "address.country":
                    eventError.setLacksCountry(error.getDefaultMessage());
                    break;
                case "address.city":
                    eventError.setLacksCity(error.getDefaultMessage());
                    break;
                case "address.street":
                    eventError.setLacksStreet(error.getDefaultMessage());
                    break;
                case "maxParticipants":
                    if (Objects.requireNonNull(error.getCodes())[2].equals("Min.int")) {
                        eventError.setNotEnoughParticipants(error.getDefaultMessage());
                    } else {
                        eventError.setTooManyParticipants(error.getDefaultMessage());
                    }
                    break;
                case "date":
                    eventError.setInvalidDate(error.getDefaultMessage());
                    break;
            }
        }
    }

}
