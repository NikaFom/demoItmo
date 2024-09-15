package com.example.demoItmo.service;

import com.example.demoItmo.model.db.entity.UserEntity;
import com.example.demoItmo.model.db.repository.UserRepository;
import com.example.demoItmo.model.dto.request.UserInfoRequest;
import com.example.demoItmo.model.dto.response.UserInfoResponse;
import com.example.demoItmo.model.enums.UserStatus;
import com.example.demoItmo.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        UserEntity user = getUserFromDB(id);
        return mapper.convertValue(user, UserInfoResponse.class);
    }

    public UserEntity getUserFromDB(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserInfoResponse updateUser(Long id, @Valid UserInfoRequest request) {
        if(!EmailValidator.getInstance().isValid(request.getEmail())) {
            return null;
        }

        UserEntity user = getUserFromDB(id);

        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword() == null ? user.getPassword() : request.getPassword());
        user.setFirstName(request.getFirstName() == null ? user.getFirstName() : request.getFirstName());
        user.setLastName(request.getLastName() == null ? user.getLastName() : request.getLastName());
        user.setMiddleName(request.getMiddleName() == null ? user.getMiddleName() : request.getMiddleName());
        user.setAge(request.getAge() == null ? user.getAge() : request.getAge());
        user.setGender(request.getGender() == null ? user.getGender() : request.getGender());

        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.UPDATED);

        UserEntity savedUser = userRepository.save(user);

        return mapper.convertValue(savedUser, UserInfoResponse.class);
    }

    public void deleteUser(Long id) {
        UserEntity user = getUserFromDB(id);
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    public Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<UserEntity> all;
        if(filter == null) {
            all = userRepository.findAllByStatusNot(pageRequest, UserStatus.DELETED);
        } else {
            all = userRepository.findAllByStatusNotFiltered(pageRequest, UserStatus.DELETED, filter.toLowerCase());
        }

        List<UserInfoResponse> content = all.getContent().stream()
                .map(userEntity -> mapper.convertValue(userEntity, UserInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }

    public UserEntity updateUserData(UserEntity user) {
        return userRepository.save(user);
    }
}
