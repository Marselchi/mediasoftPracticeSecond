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
}
