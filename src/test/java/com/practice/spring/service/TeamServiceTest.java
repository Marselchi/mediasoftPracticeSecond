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
import java.util.Optional;

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

    @Test
    void getTeamById_Success() {
        Long teamId = 1L;
        Team team = Team.builder().id(teamId).name("Спартак").build();
        TeamResponseDTO expectedDto = TeamResponseDTO.builder().id(teamId).name("Спартак").build();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(teamMapper.toResponse(team)).thenReturn(expectedDto);

        TeamResponseDTO result = teamService.getTeamById(teamId);

        assertEquals("Спартак", result.name());
        verify(teamRepository).findById(teamId);
    }

    @Test
    void getTeamById_NotFound_ThrowsException() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> teamService.getTeamById(999L));
    }

    @Test
    void updateTeam_Success() {
        Long teamId = 1L;
        TeamRequestDTO dto = TeamRequestDTO.builder().name("Динамо").shortName("ДИН").build();

        Team existingTeam = Team.builder().id(teamId).name("СтароеИмя").build();
        Team updatedTeam = Team.builder().id(teamId).name("Динамо").shortName("ДИН").build();
        TeamResponseDTO expectedDto = TeamResponseDTO.builder().id(teamId).name("Динамо").build();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.existsByName("Динамо")).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(updatedTeam);
        when(teamMapper.toResponse(updatedTeam)).thenReturn(expectedDto);

        TeamResponseDTO result = teamService.updateTeam(teamId, dto);

        assertEquals("Динамо", result.name());
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void updateTeam_DuplicateName_ThrowsException() {
        Long teamId = 1L;
        TeamRequestDTO dto = TeamRequestDTO.builder().name("Зенит").build();

        Team existingTeam = Team.builder().id(teamId).name("Спартак").build();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.existsByName("Зенит")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> teamService.updateTeam(teamId, dto));
        verify(teamRepository, never()).save(any());
    }

    @Test
    void updateTeam_NotFound_ThrowsException() {
        when(teamRepository.findById(999L)).thenReturn(Optional.empty());
        TeamRequestDTO dto = TeamRequestDTO.builder().build();

        assertThrows(RuntimeException.class, () -> teamService.updateTeam(999L, dto));
    }


    @Test
    void deleteTeam_Success() {
        Long teamId = 1L;
        when(teamRepository.existsById(teamId)).thenReturn(true);

        teamService.deleteTeam(teamId);

        verify(teamRepository).deleteById(teamId);
    }

    @Test
    void deleteTeam_NotFound_ThrowsException() {
        Long teamId = 999L;
        when(teamRepository.existsById(teamId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> teamService.deleteTeam(teamId));
        verify(teamRepository, never()).deleteById(any());
    }
}
