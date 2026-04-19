package com.practice.spring.dto.mapper;

import com.practice.spring.dto.TeamResponseDTO;
import com.practice.spring.entity.Team;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamResponseDTO toResponse(Team team);
    List<TeamResponseDTO> toResponseList(List<Team> teams);
}
