package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    Optional<Speaker> findByEmail(String name);
    Boolean existsByName(String name);

    Boolean existsByEmail(String mail);
}
