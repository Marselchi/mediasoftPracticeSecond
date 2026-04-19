package com.practice.spring.dto;

import java.time.LocalDate;
import java.util.List;

public record StandingsResponseDTO (
    String seasonName,
    LocalDate cutoffDate,
    List<StandingsItemDTO> standings )
{ }
