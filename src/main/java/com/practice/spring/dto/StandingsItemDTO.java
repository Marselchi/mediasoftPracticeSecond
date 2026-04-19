package com.practice.spring.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StandingsItemDTO {
    String teamName;
    Integer played;
    Integer points;
    Integer won;
    Integer drawn;
    Integer lost;
    Integer goalsFor;
    Integer goalsAgainst;
    Integer goalDifference;
}
