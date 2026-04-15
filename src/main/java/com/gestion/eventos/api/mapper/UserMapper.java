package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.dto.RegisterDto;
import com.gestion.eventos.api.repository.IRoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected IRoleRepository roleRepository;

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract User registerDtoToUser(RegisterDto registerDto);

    public Set<Role> mapRoleStringToRoles(Set<String> roleNames){
        if (roleNames == null || roleNames.isEmpty()){
            return roleRepository.findByName("R OLE_USER")
                    .map(Collections::singleton)
                    .orElseThrow(() -> new RuntimeException("Default role not found in the database."));
        }
        return roleNames.stream()
                .map(
                        roleName -> roleRepository.findByName(roleName)
                                .orElseThrow(
                                        () -> new RuntimeException("Role not found: " + roleName)
                                )
                ).collect(Collectors.toSet());
    }
}
