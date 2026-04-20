package com.gestion.eventos.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private String username;

    private List<RoleDto> roles;
    private List<EventSummaryDto> events;
}
