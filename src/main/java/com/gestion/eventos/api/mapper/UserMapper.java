package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.dto.RegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    User registerDtoToUser(RegisterDto registerDto);
}
