package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository <Event, Long>{


}
