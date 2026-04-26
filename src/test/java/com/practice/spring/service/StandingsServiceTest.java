package com.practice.spring.service;

import com.practice.spring.dto.StandingsItemDTO;
import com.practice.spring.dto.StandingsResponseDTO;
import com.practice.spring.entity.Match;
import com.practice.spring.entity.Season;
import com.practice.spring.entity.Team;
import com.practice.spring.repository.MatchRepository;
import com.practice.spring.repository.SeasonRepository;
import com.practice.spring.util.enums.MatchStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class StandingsServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private SeasonRepository seasonRepository;
    @InjectMocks private StandingsService standingsService;

    @Test
    void getStandings_CalculationAndSorting() {
        // Сценарий:
        // 1. A 2:0 B -> A(+3), B(+0)
        // 2. B 1:1 C -> B(+1), C(+1)
        // 3. A 0:1 C -> C(+3), A(+0)
        // Итого: C=4 очка, A=3 очка, B=1 очко. Порядок: C, A, B.
        List<Match> matches = List.of(
                buildMatch("A", "B", 2, 0),
                buildMatch("B", "C", 1, 1),
                buildMatch("A", "C", 0, 1)
        );

        when(matchRepository.findBySeasonIdAndDateLessThanEqual(
                eq(1L),
                any(LocalDateTime.class)
        )).thenReturn(matches);

        when(seasonRepository.findById(1L)).thenReturn(
                Optional.of(Season.builder().name("Test Season").build()));

        // When
        StandingsResponseDTO result = standingsService.getStandings(1L, LocalDate.of(2024, 12, 31));

        // Then
        assertNotNull(result);
        assertEquals(3, result.standings().size(), "Должно быть 3 команды");

        List<StandingsItemDTO> standings = result.standings();
        assertEquals("C", standings.get(0).getTeamName(), "1 место: Команда C");
        assertEquals(4, standings.get(0).getPoints(), "У команды C должно быть 4 очка");

        assertEquals("A", standings.get(1).getTeamName(), "2 место: Команда A");
        assertEquals(3, standings.get(1).getPoints(), "У команды A должно быть 3 очка");

        assertEquals("B", standings.get(2).getTeamName(), "3 место: Команда B");
        assertEquals(1, standings.get(2).getPoints(), "У команды B должно быть 1 очко");
    }

    private Match buildMatch(String homeName, String guestName, int homeScore, int guestScore) {
        return Match.builder()
                .homeTeam(Team.builder().name(homeName).build())
                .guestTeam(Team.builder().name(guestName).build())
                .homeScore(homeScore)
                .guestScore(guestScore)
                .status(MatchStatus.FINISHED)
                .date(LocalDateTime.now())
                .build();
    }
}