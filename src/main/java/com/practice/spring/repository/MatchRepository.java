package com.practice.spring.repository;

import com.practice.spring.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findBySeasonIdAndDateLessThanEqual(
            Long seasonId,
            LocalDateTime date
    );
}
