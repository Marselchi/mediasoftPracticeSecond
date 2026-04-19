package com.practice.spring.dto;

import lombok.Value;

@Value
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
