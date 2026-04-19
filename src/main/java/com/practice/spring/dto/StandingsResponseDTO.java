package com.practice.spring.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record StandingsResponseDTO (
    String seasonName,
    LocalDate cutoffDate,
    List<StandingsItemDTO> standings )
{ }
