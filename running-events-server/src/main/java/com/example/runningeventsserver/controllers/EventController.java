package com.example.runningeventsserver.controllers;

import com.example.runningeventsserver.dtos.EventDto;
import com.example.runningeventsserver.dtos.errors.Error;
import com.example.runningeventsserver.dtos.errors.wrappers.EventErrorWrapper;
import com.example.runningeventsserver.models.Event;
import com.example.runningeventsserver.models.User;
import com.example.runningeventsserver.repositories.EventRepository;
import com.example.runningeventsserver.services.EventService;
import com.example.runningeventsserver.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class EventController {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public ResponseEntity<?> getEvents(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit,
                                       @RequestParam(defaultValue = "name") String sortBy, @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String[] country, @RequestParam(required = false) String[] city,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd) {

        List<EventDto> events = eventService.getAllEvents(page, limit, sortBy, name, country, city, dateStart, dateEnd);
        
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable String id) {
        EventDto event = eventService.getEventDto(id);
        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<?> addEvent(@Valid @RequestBody EventDto event, BindingResult result, UriComponentsBuilder uriComponentsBuilder) {
        if (!userService.isUserAdmin(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (result.hasErrors()) {
            EventErrorWrapper error = new EventErrorWrapper(result.getFieldErrors());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        EventDto created = eventService.addEvent(event);
        URI location = uriComponentsBuilder.path("/api/events/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editEvent(@PathVariable String id, @Valid @RequestBody EventDto event, BindingResult result) {
        if (!userService.isUserAdmin(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (result.hasErrors()) {
            EventErrorWrapper error = new EventErrorWrapper(result.getFieldErrors());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        EventDto updated = eventService.editEvent(id, event);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable String id) {
        if (!userService.isUserAdmin(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinEvent(@PathVariable String id) {
        User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (event.get().getUserIds().contains(new ObjectId(user.getId()))) {
            return new ResponseEntity<>(new Error("You already joined this event"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (event.get().getUserIds().size() >= event.get().getMaxParticipants()) {
            return new ResponseEntity<>(new Error("Unable to join because event has maximum number of participants"), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        event.get().getUserIds().add(new ObjectId(user.getId()));
        eventRepository.save(event.get());
        user.getEventIds().add(new ObjectId(id));
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<?> leaveEvent(@PathVariable String id) {
        User user = userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        event.get().getUserIds().remove(new ObjectId(user.getId()));
        eventRepository.save(event.get());
        user.getEventIds().remove(new ObjectId(id));
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
