package com.gestion.eventos.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventRequestDto {
    @NotBlank(message = "El nombre del evento es obligatorio")
    private String name;
    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDate date;
    @NotBlank(message = "La ubicación del evento es obligatoria")
    private String location;
}
