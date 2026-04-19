package com.practice.spring.dto.mapper;
import com.practice.spring.dto.MatchResponseDTO;
import com.practice.spring.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(source = "season.name", target = "seasonName")
    @Mapping(source = "homeTeam.name", target = "homeTeamName")
    @Mapping(source = "guestTeam.name", target = "guestTeamName")
    @Mapping(source = "date", target = "matchDate")
    MatchResponseDTO toResponse(Match match);

    List<MatchResponseDTO> toResponseList(List<Match> matches);
}