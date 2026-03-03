package com.practice.spring.entity;


import com.practice.spring.util.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_team_id", nullable = false)
    Team guestTeam;

    @Column(nullable = false)
    LocalDateTime date;

    Integer homeScore;

    Integer guestScore;

    @Enumerated(EnumType.STRING)
    MatchStatus status;

    Integer round;
}
