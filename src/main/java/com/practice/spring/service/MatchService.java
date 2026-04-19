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
import com.practice.spring.util.enums.MatchStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final MatchMapper matchMapper;

    @Transactional
    public MatchResponseDTO registerMatch(MatchRequestDTO dto) {
        if (dto.getHomeTeamId().equals(dto.getGuestTeamId())) {
            throw new IllegalArgumentException("Команда не может играть сама с собой");
        }

        Season season = seasonRepository.findById(dto.getSeasonId())
                .orElseThrow(() -> new RuntimeException("Сезон не найден"));

        Team homeTeam = teamRepository.findById(dto.getHomeTeamId())
                .orElseThrow(() -> new RuntimeException("Команда хозяев не найдена"));

        Team guestTeam = teamRepository.findById(dto.getGuestTeamId())
                .orElseThrow(() -> new RuntimeException("Команда гостей не найдена"));

        Match match = Match.builder()
                .season(season)
                .homeTeam(homeTeam)
                .guestTeam(guestTeam)
                .date(dto.getMatchDate())
                .homeScore(dto.getHomeScore())
                .guestScore(dto.getGuestScore())
                .status(MatchStatus.FINISHED)
                .round(dto.getRound())
                .build();

        return matchMapper.toResponse(matchRepository.save(match));
    }
}
