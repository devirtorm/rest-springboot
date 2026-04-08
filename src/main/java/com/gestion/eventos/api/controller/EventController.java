package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.mapper.IEventMapper;
import com.gestion.eventos.api.service.IEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final IEventService eventService;
    private final IEventMapper eventManager;

    @GetMapping
    public List<EventResponseDto> getAllEvents() {
        List<Event> events = eventService.findAll();
        return eventManager.toEventResponseDtoList(events);
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@Valid @RequestBody EventRequestDto requestDto){
        Event eventToSave = eventManager.toEntity(requestDto);
        Event eventSaved = eventService.save(eventToSave);
        EventResponseDto responseDto = eventManager.toResponseDto(eventSaved);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id){
        Event event = eventService.findById(id);
        EventResponseDto responseDto = eventManager.toResponseDto(event);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long id,
                                                        @Valid @RequestBody EventRequestDto requestDto) {
        Event eventToUpdate = eventService.findById(id);
        eventManager.updateEventFromDto(requestDto, eventToUpdate);
        Event updatedEvent = eventService.save(eventToUpdate);
        return ResponseEntity.ok(eventManager.toResponseDto(updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id){
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
