package com.practice.spring.dto;

import lombok.Builder;

@Builder
public record TeamResponseDTO (
    Long id,
    String name,
    String shortName)
{ }
