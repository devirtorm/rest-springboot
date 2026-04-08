package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final EventRepository eventRepository;

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event not found with id: " + id)
        );
    }

    @Override
    public void deleteById(Long id) {
        Event evenToDelete = findById(id);
        eventRepository.delete(evenToDelete);
    }
}
