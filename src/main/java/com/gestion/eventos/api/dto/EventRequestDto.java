package com.gestion.eventos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Schema(name = "EventRequestDto", description = "DTO for creating or updating an event")
public class EventRequestDto {
    @Schema(description = "Name of the event", example = "Tech Conference 2024", required = true)
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private Set<Long> speakersIds;
}
