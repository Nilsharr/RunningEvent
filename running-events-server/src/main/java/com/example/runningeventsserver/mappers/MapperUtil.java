package com.example.runningeventsserver.mappers;

import com.example.runningeventsserver.dtos.EventDto;
import com.example.runningeventsserver.models.Event;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface MapperUtil {

    default EventDto mapEventToEventDto(Event event) {
        if (event == null) {
            return null;
        }
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setDetails(event.getDetails());
        eventDto.setDate(event.getDate());
        eventDto.setMaxParticipants(event.getMaxParticipants());
        eventDto.setAddress(event.getAddress());
        eventDto.setRoute(event.getRoute());
        eventDto.setParticipants(mapToStringList(event.getUserIds()));
        return eventDto;
    }

    default Event mapEventDtoToEvent(EventDto eventDto) {
        if (eventDto == null) {
            return null;
        }
        Event event = new Event();
        event.setId(eventDto.getId());
        event.setName(eventDto.getName());
        event.setDetails(eventDto.getDetails());
        event.setDate(eventDto.getDate());
        event.setMaxParticipants(eventDto.getMaxParticipants());
        event.setAddress(eventDto.getAddress());
        event.setRoute(eventDto.getRoute());
        event.setUserIds(mapToObjectIdList(eventDto.getParticipants()));
        return event;
    }

    default List<String> mapToStringList(List<ObjectId> objectIds) {
        if (objectIds == null) {
            return null;
        }
        List<String> stringList = new ArrayList<>();
        for (ObjectId objectId : objectIds) {
            stringList.add(objectId.toString());
        }
        return stringList;
    }

    default List<ObjectId> mapToObjectIdList(List<String> stringIds) {
        if (stringIds == null) {
            return null;
        }
        List<ObjectId> objectIdList = new ArrayList<>();
        for (String stringId : stringIds) {
            objectIdList.add(new ObjectId(stringId));
        }
        return objectIdList;
    }

}