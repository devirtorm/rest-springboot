package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Event;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository <Event, Long>{
    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "JOIN FETCH e.category " +
            "LEFT JOIN FETCH e.speakers")
    List<Event> findAllWithCategoryAndSpeaker();

    @Query("SELECT e FROM Event e " +
            "JOIN FETCH e.category c " +
            "LEFT JOIN FETCH e.speakers " +
            "WHERE c.id = :categoryId")
    List<Event> findByIdWithCategoryAndSpeakers(Long categoryId);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"category", "speakers"})
    List<Event> findAll();

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"category", "speakers"})
    Optional<Event> findById(Long id);

    @EntityGraph(attributePaths = {"category", "speakers", "attendedUsers"})
    @Query("SELECT e FROM Event e")
    List<Event> findAllWithAllDetails();
}
