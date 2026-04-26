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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final MatchMapper matchMapper;

    @Transactional
    public MatchResponseDTO registerMatch(MatchRequestDTO dto) {
        validateTeams(dto.getHomeTeamId(), dto.getGuestTeamId());

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
                .status(dto.getStatus() != null ? dto.getStatus() : MatchStatus.SCHEDULED)
                .round(dto.getRound())
                .build();

        return matchMapper.toResponse(matchRepository.save(match));
    }

    public List<MatchResponseDTO> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(matchMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MatchResponseDTO getMatchById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Матч не найден"));
        return matchMapper.toResponse(match);
    }

    @Transactional
    public MatchResponseDTO updateMatch(Long id, MatchRequestDTO dto) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Матч не найден"));

        if (!match.getHomeTeam().getId().equals(dto.getHomeTeamId()) ||
                !match.getGuestTeam().getId().equals(dto.getGuestTeamId())) {
            validateTeams(dto.getHomeTeamId(), dto.getGuestTeamId());

            Team homeTeam = teamRepository.findById(dto.getHomeTeamId())
                    .orElseThrow(() -> new RuntimeException("Команда хозяев не найдена"));
            Team guestTeam = teamRepository.findById(dto.getGuestTeamId())
                    .orElseThrow(() -> new RuntimeException("Команда гостей не найдена"));

            match.setHomeTeam(homeTeam);
            match.setGuestTeam(guestTeam);
        }

        if (dto.getSeasonId() != null && !match.getSeason().getId().equals(dto.getSeasonId())) {
            Season season = seasonRepository.findById(dto.getSeasonId())
                    .orElseThrow(() -> new RuntimeException("Сезон не найден"));
            match.setSeason(season);
        }

        match.setDate(dto.getMatchDate());
        match.setHomeScore(dto.getHomeScore());
        match.setGuestScore(dto.getGuestScore());
        match.setRound(dto.getRound());
        if (dto.getStatus() != null) {
            match.setStatus(dto.getStatus());
        }

        return matchMapper.toResponse(matchRepository.save(match));
    }

    @Transactional
    public void deleteMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("Матч не найден");
        }
        matchRepository.deleteById(id);
    }

    private void validateTeams(Long homeId, Long guestId) {
        if (homeId.equals(guestId)) {
            throw new IllegalArgumentException("Команда не может играть сама с собой");
        }
    }
}