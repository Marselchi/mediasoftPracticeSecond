package com.practice.spring.repository;

import com.practice.spring.entity.Match;
import com.practice.spring.util.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findBySeasonIdAndDateLessThanEqualAndStatus(
            Long seasonId,
            LocalDateTime date,
            MatchStatus status
    );
}
