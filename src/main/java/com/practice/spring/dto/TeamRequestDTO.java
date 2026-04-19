package com.practice.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TeamRequestDTO {
    @NotBlank
    String name;
    String shortName;
}
