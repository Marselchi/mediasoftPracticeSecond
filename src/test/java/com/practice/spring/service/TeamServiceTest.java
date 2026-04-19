package com.practice.spring.service;

import com.practice.spring.dto.TeamRequestDTO;
import com.practice.spring.dto.TeamResponseDTO;
import com.practice.spring.dto.mapper.TeamMapper;
import com.practice.spring.entity.Team;
import com.practice.spring.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @Mock private TeamRepository teamRepository;
    @Mock private TeamMapper teamMapper;
    @InjectMocks private TeamService teamService;

    @Test
    void createTeam_Success() {
        TeamRequestDTO dto = TeamRequestDTO.builder().name("Зенит").shortName("ЗЕН").build();
        when(teamRepository.existsByName("Зенит")).thenReturn(false);

        Team saved = Team.builder().id(1L).name("Зенит").shortName("ЗЕН").build();
        when(teamRepository.save(any(Team.class))).thenReturn(saved);

        TeamResponseDTO expected = TeamResponseDTO.builder().id(1L).name("Зенит").shortName("ЗЕН").build();
        when(teamMapper.toResponse(saved)).thenReturn(expected);

        TeamResponseDTO result = teamService.createTeam(dto);
        assertEquals(1L, result.id());
        verify(teamRepository).save(any());
    }

    @Test
    void createTeam_DuplicateName_ThrowsException() {
        TeamRequestDTO dto = TeamRequestDTO.builder().name("Зенит").build();
        when(teamRepository.existsByName("Зенит")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> teamService.createTeam(dto));
        verify(teamRepository, never()).save(any());
    }

    @Test
    void getAllTeams_ReturnsList() {
        Team t = Team.builder().id(1L).name("A").build();
        when(teamRepository.findAll()).thenReturn(List.of(t));

        TeamResponseDTO dto = TeamResponseDTO.builder().id(1L).name("A").build();
        when(teamMapper.toResponseList(any())).thenReturn(List.of(dto));

        List<TeamResponseDTO> result = teamService.getAllTeams();
        assertEquals(1, result.size());
        assertEquals("A", result.getFirst().name());
    }
}
