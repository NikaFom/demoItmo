package com.example.demoItmo.model.dto.request;

import com.example.demoItmo.model.enums.Colour;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarInfoRequest {
    String brand;
    String model;
    Integer year;
    Integer doors;
    Double capacity;
    Double speed;
    Colour colour;
}
