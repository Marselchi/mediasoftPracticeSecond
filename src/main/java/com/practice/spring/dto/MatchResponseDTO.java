package com.practice.spring.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MatchResponseDTO (
    Long id,
    String seasonName,
    String homeTeamName,
    String guestTeamName,
    LocalDateTime matchDate,
    Integer homeScore,
    Integer guestScore,
    String status,
    Integer round )
{ }
