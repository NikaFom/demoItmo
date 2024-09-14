package com.example.demoItmo.service;

import com.example.demoItmo.model.db.entity.UserEntity;
import com.example.demoItmo.model.db.repository.UserRepository;
import com.example.demoItmo.model.dto.request.UserInfoRequest;
import com.example.demoItmo.model.dto.response.UserInfoResponse;
import com.example.demoItmo.model.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final ObjectMapper mapper;
    private final UserRepository userRepository;

    public UserInfoResponse createUser(@Valid UserInfoRequest request) {
        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        UserEntity user = mapper.convertValue(request, UserEntity.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.CREATED);

        UserEntity savedUser = userRepository.save(user);

        return mapper.convertValue(savedUser, UserInfoResponse.class);
    }
    
    public UserInfoResponse getUser(Long id) {
        UserEntity user = userRepository.findById(id).orElse(new UserEntity());
        return mapper.convertValue(user, UserInfoResponse.class);
    }

    public UserInfoResponse updateUser(Long id, @Valid UserInfoRequest request) {
        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        return UserInfoResponse.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .age(request.getAge())
                .gender(request.getGender())
                .build();
    }

    public void deleteUser(Long id) {
    }

    public List<UserInfoResponse> getAllUsers() {
        return Collections.emptyList();
    }
}
