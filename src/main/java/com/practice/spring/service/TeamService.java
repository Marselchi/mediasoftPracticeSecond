package com.practice.spring.service;

import com.practice.spring.dto.TeamRequestDTO;
import com.practice.spring.dto.TeamResponseDTO;
import com.practice.spring.dto.mapper.TeamMapper;
import com.practice.spring.entity.Team;
import com.practice.spring.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Transactional
    public TeamResponseDTO createTeam(TeamRequestDTO dto) {
        if (teamRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Команда с таким названием уже существует");
        }
        Team team = Team.builder()
                .name(dto.getName())
                .shortName(dto.getShortName())
                .build();
        return teamMapper.toResponse(teamRepository.save(team));
    }

    public List<TeamResponseDTO> getAllTeams() {
        return teamMapper.toResponseList(teamRepository.findAll());
    }

    public TeamResponseDTO getTeamById(Long id) {
        return teamMapper.toResponse(
                teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Команда не найдена"))
        );
    }

    @Transactional
    public TeamResponseDTO updateTeam(Long id, TeamRequestDTO dto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Команда не найдена"));

        if (!team.getName().equals(dto.getName()) && teamRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Команда с таким названием уже существует");
        }

        team.setName(dto.getName());
        team.setShortName(dto.getShortName());

        return teamMapper.toResponse(teamRepository.save(team));
    }

    @Transactional
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Команда не найдена");
        }
        // По хорошему каскадное удаление но в тз про это ни слова, в целом про круд чего либо ничего не написано
        teamRepository.deleteById(id);
    }
}