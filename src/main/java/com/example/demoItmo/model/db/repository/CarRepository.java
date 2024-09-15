package com.example.demoItmo.model.db.repository;

import com.example.demoItmo.model.db.entity.CarEntity;
import com.example.demoItmo.model.enums.CarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {

    @Query("select c from CarEntity c where c.status <> :status")
    Page<CarEntity> findAllByStatusNot(Pageable request, CarStatus status);

    @Query("select c from CarEntity c where c.status <> :status and (lower(c.brand) like %:filter% or lower(c.model) like %:filter%)")
    Page<CarEntity> findAllByStatusNotFiltered(Pageable request, CarStatus status, String filter);
}
