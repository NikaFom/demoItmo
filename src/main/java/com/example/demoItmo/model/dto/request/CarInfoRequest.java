package com.example.demoItmo.model.dto.request;

import com.example.demoItmo.model.enums.Colour;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarInfoRequest {
    @NotEmpty
    String brand;
    String model;
    Integer year;
    Integer doors;
    Double capacity;
    Double speed;
    Colour colour;
}
