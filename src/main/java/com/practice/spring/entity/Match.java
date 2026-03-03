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
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_team_id", nullable = false)
    private Team guestTeam;

    @Column(nullable = false)
    private LocalDateTime date;

    private Integer homeScore;

    private Integer guestScore;

    @Enumerated(EnumType.STRING)
    private MatchStatus status;

    private Integer round;
}
