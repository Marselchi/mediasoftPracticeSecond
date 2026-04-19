package com.practice.spring.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class MatchRequestDTO {
    @NotNull
    Long seasonId;
    @NotNull
    Long homeTeamId;
    @NotNull
    Long guestTeamId;
    @NotNull
    LocalDateTime matchDate;
    @PositiveOrZero
    Integer homeScore;
    @PositiveOrZero
    Integer guestScore;
    Integer round;
}
