package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.mapper.IEventMapper;
import com.gestion.eventos.api.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.EventManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final EventRepository eventRepository;
    private final IEventMapper eventMapper;
    private final CategoryServiceImpl categoryService;
    private final SpeakerServiceImpl speakerService;

    @Override
    public Page<EventResponseDto> findAll(String name, Pageable pageable) {
        Page<Event> eventsPage;
        if (name!=null && !name.trim().isEmpty()){
            eventsPage = eventRepository.findByNameContainingIgnoreCase(name, pageable);
        }else {
            eventsPage = eventRepository.findAll(pageable);
        }

        List<EventResponseDto> dtos = eventsPage.getContent().stream()
                .map(eventMapper::toResponseDto)
                .toList();

        return new PageImpl<>(dtos, pageable, eventsPage.getTotalElements());
    }

    @Override
    public Event save(EventRequestDto eventRequestDto) {
        Event event = eventMapper.toEntity(eventRequestDto);
        Category category = categoryService.findById(eventRequestDto.getCategoryId());
        event.setCategory(category);

        if(eventRequestDto.getSpeakersIds() != null && !eventRequestDto.getSpeakersIds().isEmpty()){
            Set<Speaker> speakers =  eventRequestDto.getSpeakersIds().stream()
                    .map(speakerService::findById)
                    .collect(Collectors.toSet());

            speakers.forEach(event::addSpeaker);
        }

        return eventRepository.save(event);
    }

    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Event not found with id: " + id)
        );
    }

    @Override
    public Event update(Long id, EventRequestDto requestDto) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Event not found with id: " + id)
        );

        eventMapper.updateEventFromDto(requestDto, existingEvent);

        if(!existingEvent.getCategory().getId().equals(requestDto.getCategoryId())){
            Category category = categoryService.findById(requestDto.getCategoryId());
            existingEvent.setCategory(category);
        }

        Set<Speaker> updatedSpeaker;
        if(requestDto.getSpeakersIds() != null && !requestDto.getSpeakersIds().isEmpty()){
            updatedSpeaker =  requestDto.getSpeakersIds().stream()
                    .map(speakerService::findById)
                    .collect(Collectors.toSet());
        } else {
            updatedSpeaker = new HashSet<>();
        }

        new HashSet<>(existingEvent.getSpeakers())
                .forEach(currentSpeaker -> {
                    if(!updatedSpeaker.contains(currentSpeaker)){
                        existingEvent.removeSpeaker(currentSpeaker);
                    }
                });

        updatedSpeaker.forEach(newSpeaker ->{
            if (!existingEvent.getSpeakers().contains(newSpeaker)){
                existingEvent.addSpeaker(newSpeaker);
            }
        });
        return eventRepository.save(existingEvent);
    }

    @Override
    public void deleteById(Long id) {
        Event evenToDelete = findById(id);
        eventRepository.delete(evenToDelete);
    }
}
