package com.practice.spring.service;

import com.practice.spring.dto.MatchRequestDTO;
import com.practice.spring.dto.MatchResponseDTO;
import com.practice.spring.dto.mapper.MatchMapper;
import com.practice.spring.entity.Match;
import com.practice.spring.entity.Season;
import com.practice.spring.entity.Team;
import com.practice.spring.repository.MatchRepository;
import com.practice.spring.repository.SeasonRepository;
import com.practice.spring.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {
    @Mock private MatchRepository matchRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private SeasonRepository seasonRepository;
    @Mock private MatchMapper matchMapper;
    @InjectMocks private MatchService matchService;

    @Test
    void registerMatch_Success() {
        // Given
        MatchRequestDTO dto = MatchRequestDTO.builder()
                .seasonId(1L).homeTeamId(10L).guestTeamId(20L)
                .matchDate(LocalDateTime.now()).homeScore(2).guestScore(1).round(1).build();

        Season season = Season.builder().id(1L).name("2023/24").build();
        Team home = Team.builder().id(10L).name("TeamA").build();
        Team guest = Team.builder().id(20L).name("TeamB").build();
        Match saved = Match.builder().id(100L).season(season).homeTeam(home).guestTeam(guest).build();
        MatchResponseDTO expectedDto = MatchResponseDTO.builder().id(100L).build();

        when(seasonRepository.findById(1L)).thenReturn(Optional.of(season));
        when(teamRepository.findById(10L)).thenReturn(Optional.of(home));
        when(teamRepository.findById(20L)).thenReturn(Optional.of(guest));
        when(matchRepository.save(any(Match.class))).thenReturn(saved);
        when(matchMapper.toResponse(saved)).thenReturn(expectedDto);

        // When
        MatchResponseDTO result = matchService.registerMatch(dto);

        // Then
        assertEquals(100L, result.id());
        verify(matchRepository).save(any(Match.class));
    }

    @Test
    void registerMatch_SameTeam_ThrowsException() {
        MatchRequestDTO dto = MatchRequestDTO.builder()
                .seasonId(1L).homeTeamId(10L).guestTeamId(10L).build();

        assertThrows(IllegalArgumentException.class, () -> matchService.registerMatch(dto));
        verifyNoInteractions(matchRepository);
    }

    @Test
    void getMatchById_Success() {
        Long matchId = 100L;
        Match match = Match.builder().id(matchId).build();
        MatchResponseDTO expectedDto = MatchResponseDTO.builder().id(matchId).build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchMapper.toResponse(match)).thenReturn(expectedDto);

        MatchResponseDTO result = matchService.getMatchById(matchId);

        assertEquals(matchId, result.id());
        verify(matchRepository).findById(matchId);
    }

    @Test
    void getMatchById_NotFound_ThrowsException() {
        Long matchId = 999L;
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matchService.getMatchById(matchId));
    }

    @Test
    void getAllMatches_ReturnsList() {
        Match m1 = Match.builder().id(1L).build();
        Match m2 = Match.builder().id(2L).build();

        when(matchRepository.findAll()).thenReturn(List.of(m1, m2));
        when(matchMapper.toResponse(any(Match.class))).thenReturn(MatchResponseDTO.builder().id(1L).build());

        List<MatchResponseDTO> result = matchService.getAllMatches();

        assertEquals(2, result.size());
        verify(matchRepository).findAll();
    }

    @Test
    void updateMatch_Success() {
        Long matchId = 100L;
        MatchRequestDTO dto = MatchRequestDTO.builder()
                .homeTeamId(10L).guestTeamId(20L)
                .homeScore(3).guestScore(0).build();

        Season season = Season.builder().id(1L).build();
        Team home = Team.builder().id(10L).name("TeamA").build();
        Team guest = Team.builder().id(20L).name("TeamB").build();

        Match existingMatch = Match.builder()
                .id(matchId)
                .season(season)
                .homeTeam(home)
                .guestTeam(guest)
                .build();

        Match updatedMatch = Match.builder().id(matchId).homeScore(3).guestScore(0).build();
        MatchResponseDTO expectedDto = MatchResponseDTO.builder().id(matchId).homeScore(3).build();

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(existingMatch));
        when(matchRepository.save(any(Match.class))).thenReturn(updatedMatch);
        when(matchMapper.toResponse(updatedMatch)).thenReturn(expectedDto);

        MatchResponseDTO result = matchService.updateMatch(matchId, dto);

        assertEquals(3, result.homeScore());
        verify(matchRepository).save(any(Match.class));
    }

    @Test
    void updateMatch_NotFound_ThrowsException() {
        when(matchRepository.findById(999L)).thenReturn(Optional.empty());
        MatchRequestDTO dto = MatchRequestDTO.builder().build();

        assertThrows(RuntimeException.class, () -> matchService.updateMatch(999L, dto));
    }

    @Test
    void deleteMatch_Success() {
        Long matchId = 100L;
        when(matchRepository.existsById(matchId)).thenReturn(true);

        matchService.deleteMatch(matchId);

        verify(matchRepository).deleteById(matchId);
    }

    @Test
    void deleteMatch_NotFound_ThrowsException() {
        Long matchId = 999L;
        when(matchRepository.existsById(matchId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> matchService.deleteMatch(matchId));
        verify(matchRepository, never()).deleteById(any());
    }
}
