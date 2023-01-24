package com.example.runningeventsserver.services;

import com.example.runningeventsserver.dtos.EventDto;
import com.example.runningeventsserver.mappers.MapperUtil;
import com.example.runningeventsserver.models.Event;
import com.example.runningeventsserver.repositories.EventRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private MapperUtil mapper;

    public List<EventDto> getAllEvents(int page, int limit, String sortBy, String name, String[] countries, String[] cities, Date dateStart, Date dateEnd) {
        sortBy = sortBy.toLowerCase();
        sortBy = sortBy.equals("country") ? "address.country" : sortBy;
        sortBy = sortBy.equals("city") ? "address.city" : sortBy;

        Query query = new Query();
        if (name != null) {
            Criteria titleCriteria = Criteria.where("name").regex(name, "i");
            Criteria cityCriteria = Criteria.where("address.city").regex(name, "i");
            // search by name or city
            query.addCriteria(new Criteria().orOperator(titleCriteria, cityCriteria));
        }
        if (countries != null) {
            query.addCriteria(Criteria.where("address.country").in(Arrays.asList(countries)));
        }
        if (cities != null) {
            query.addCriteria(Criteria.where("address.city").in(Arrays.asList(cities)));
        }
        if (dateStart != null && dateEnd != null) {
            query.addCriteria(Criteria.where("date").gt(dateStart).lt(dateEnd));
        } else if (dateStart != null) {
            query.addCriteria(Criteria.where("date").gt(dateStart));
        } else if (dateEnd != null) {
            query.addCriteria(Criteria.where("date").lt(dateEnd));
        }

        Collation collation = Collation.of("en").strength(Collation.ComparisonLevel.primary());
        query.collation(collation);
        query.skip((long) (page - 1) * limit);
        query.limit(limit);
        query.with(Sort.by(sortBy));
        List<Event> events = mongoTemplate.find(query, Event.class);
        return events.stream().map(mapper::mapEventToEventDto).collect(Collectors.toList());
    }

    public EventDto getEventDto(String id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(value -> mapper.mapEventToEventDto(value)).orElse(null);
    }

    public List<EventDto> getUserEvents(String userId) {
        List<Event> events = eventRepository.findByUserIdsContains(new ObjectId(userId));
        return events.stream()
                .map(mapper::mapEventToEventDto).collect(Collectors.toList());
    }

    public EventDto addEvent(EventDto event) {
        Event ev = eventRepository.save(mapper.mapEventDtoToEvent(event));
        return mapper.mapEventToEventDto(ev);
    }

    public EventDto editEvent(String id, EventDto event) {
        event.setId(id);
        Event ev = eventRepository.save(mapper.mapEventDtoToEvent(event));
        return mapper.mapEventToEventDto(ev);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }
}
