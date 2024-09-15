package com.example.demoItmo.model.db.repository;

import com.example.demoItmo.model.db.entity.UserEntity;
import com.example.demoItmo.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select u from UserEntity u where u.status <> :status")
    Page<UserEntity> findAllByStatusNot(Pageable request, UserStatus status);

    @Query("select u from UserEntity u where u.status <> :status and (lower(u.firstName) like %:filter% or lower(u.lastName) like %:filter%)")
    Page<UserEntity> findAllByStatusNotFiltered(Pageable request, UserStatus status, String filter);
}
