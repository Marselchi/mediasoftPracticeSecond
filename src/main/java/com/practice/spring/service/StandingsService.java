package com.practice.spring.service;

import com.practice.spring.dto.StandingsItemDTO;
import com.practice.spring.dto.StandingsResponseDTO;
import com.practice.spring.entity.Match;
import com.practice.spring.entity.Season;
import com.practice.spring.repository.MatchRepository;
import com.practice.spring.repository.SeasonRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StandingsService {
    private final MatchRepository matchRepository;
    private final SeasonRepository seasonRepository;

    public StandingsResponseDTO getStandings(Long seasonId, LocalDate date) {
        List<Match> matches = matchRepository.findBySeasonIdAndDateLessThanEqual(
                seasonId, date.atTime(LocalTime.MAX));

        Map<String, TeamStats> stats = new LinkedHashMap<>();

        for (Match m : matches) {
            TeamStats home = stats.computeIfAbsent(m.getHomeTeam().getName(), TeamStats::new);
            TeamStats guest = stats.computeIfAbsent(m.getGuestTeam().getName(), TeamStats::new);

            home.played++; guest.played++;
            home.gf += m.getHomeScore(); home.ga += m.getGuestScore();
            guest.gf += m.getGuestScore(); guest.ga += m.getHomeScore();

            if (m.getHomeScore() > m.getGuestScore()) {
                home.w++; home.pts += 3; guest.l++;
            } else if (m.getHomeScore() < m.getGuestScore()) {
                guest.w++; guest.pts += 3; home.l++;
            } else {
                home.d++; home.pts += 1;
                guest.d++; guest.pts += 1;
            }
        }

        List<StandingsItemDTO> standings = stats.values().stream()
                .map(this::toDTO)
                .sorted((s1, s2) -> {
                    int pointsCompare = Integer.compare(s2.getPoints(), s1.getPoints());
                    if (pointsCompare != 0) return pointsCompare;

                    int gdCompare = Integer.compare(s2.getGoalDifference(), s1.getGoalDifference());
                    if (gdCompare != 0) return gdCompare;

                    return s1.getTeamName().compareTo(s2.getTeamName());
                })
                .toList();

        String seasonName = seasonRepository.findById(seasonId)
                .map(Season::getName).orElse("Сезон не найден");

        return StandingsResponseDTO.builder()
                .seasonName(seasonName)
                .cutoffDate(date)
                .standings(standings)
                .build();
    }

    private StandingsItemDTO toDTO(TeamStats s) {
        return StandingsItemDTO.builder()
                .teamName(s.name).played(s.played).won(s.w).drawn(s.d).lost(s.l)
                .points(s.pts).goalsFor(s.gf).goalsAgainst(s.ga)
                .goalDifference(s.gf - s.ga).build();
    }

    @Getter
    private static class TeamStats {
        final String name;
        int played, w, d, l, pts, gf, ga;
        TeamStats(String name) { this.name = name; }
    }
}
