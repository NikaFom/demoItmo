package com.example.demoItmo.service;

import com.example.demoItmo.exceptions.CustomException;
import com.example.demoItmo.model.db.entity.UserEntity;
import com.example.demoItmo.model.db.repository.UserRepository;
import com.example.demoItmo.model.dto.request.UserInfoRequest;
import com.example.demoItmo.model.dto.response.UserInfoResponse;
import com.example.demoItmo.model.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@gmail.com");

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserInfoResponse result = userService.createUser(request);

        assertEquals(user.getId(), result.getId());
    }

    @Test(expected = CustomException.class)
    public void createUser_badEmail() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test.gmail.com");

        userService.createUser(request);
    }

    @Test(expected = CustomException.class)
    public void createUser_userExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@gmail.com");

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        userService.createUser(request);
    }

    @Test
    public void getUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserInfoResponse result = userService.getUser(user.getId());

        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void updateUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@gmail.com");

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.updateUser(user.getId(), request);

        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertEquals(UserStatus.UPDATED, user.getStatus());
    }

    @Test(expected = CustomException.class)
    public void updateUser_badEmail() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test.gmail.com");

        UserEntity user = new UserEntity();
        user.setId(1L);

        userService.updateUser(user.getId(), request);
    }

    @Test(expected = CustomException.class)
    public void updateUser_userExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@gmail.com");

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        userService.updateUser(user.getId(), request);
    }

    @Test(expected = CustomException.class)
    public void updateUser_missingFields() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("");
        request.setFirstName("");
        request.setLastName("");
        request.setMiddleName("");
        request.setAge(0);

        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        userService.updateUser(user.getId(), request);

        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertEquals(UserStatus.UPDATED, user.getStatus());
    }

    @Test
    public void deleteUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertEquals(UserStatus.DELETED, user.getStatus());
    }

    @Test
    public void getAllUsers_notFiltered() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setFirstName("Bob");
        UserEntity user2 = new UserEntity();
        user2.setId(2L);

        List<UserEntity> users = List.of(user1, user2);

        Page<UserEntity> pagedUsers = new PageImpl<>(users);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, user1.getFirstName());

        when(userRepository.findAllByStatusNot(pageRequest, UserStatus.DELETED)).thenReturn(pagedUsers);

        Page<UserInfoResponse> result = userService
                .getAllUsers(0, 10, user1.getFirstName(), Sort.Direction.ASC, null);

        assertEquals(pagedUsers.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void getAllUsers_Filtered() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setFirstName("Bob");
        UserEntity user2 = new UserEntity();
        user2.setId(2L);

        List<UserEntity> users = List.of(user1, user2);

        Page<UserEntity> pagedUsers = new PageImpl<>(users);

        Pageable pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, user1.getFirstName());

        when(userRepository.findAllByStatusNotFiltered(pageRequest, UserStatus.DELETED, "a"))
                .thenReturn(pagedUsers);

        Page<UserInfoResponse> result = userService
                .getAllUsers(0, 10, user1.getFirstName(), Sort.Direction.ASC, "a");

        assertEquals(pagedUsers.getTotalElements(), result.getTotalElements());
    }

    @Test
    public void updateUserData() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        userService.updateUserData(user);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}