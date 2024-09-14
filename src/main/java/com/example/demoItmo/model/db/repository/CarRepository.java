package com.example.demoItmo.model.db.repository;

import com.example.demoItmo.model.db.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long> {
}
