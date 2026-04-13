package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.dto.JwtAuthResponseDto;
import com.gestion.eventos.api.dto.LoginDto;
import com.gestion.eventos.api.dto.RegisterDto;
import com.gestion.eventos.api.mapper.UserMapper;
import com.gestion.eventos.api.repository.IRoleRepository;
import com.gestion.eventos.api.repository.IUserRepository;
import com.gestion.eventos.api.security.jwt.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.hibernate.mapping.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final IRoleRepository roleRepository;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> authenticationUser(@RequestBody LoginDto loginDto) {
        Authentication authenntication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenntication);

        String token = jwtGenerator.generateToken(authenntication);

        return new ResponseEntity<>( new JwtAuthResponseDto(token), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto){
        if(userRepository.existsByUsername(registerDto.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}
