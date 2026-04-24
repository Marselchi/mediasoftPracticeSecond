package com.practice.spring;

import com.practice.spring.entity.Season;
import com.practice.spring.entity.Team;
import com.practice.spring.repository.MatchRepository;
import com.practice.spring.repository.SeasonRepository;
import com.practice.spring.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ChampionshipApiIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:18-alpine"))
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "testdb")
            .withEnv("POSTGRES_USER", "test")
            .withEnv("POSTGRES_PASSWORD", "secret");

    @Autowired private MockMvc mockMvc;
    @Autowired private TeamRepository teamRepo;
    @Autowired private SeasonRepository seasonRepo;
    @Autowired private MatchRepository matchRepo;

    @BeforeEach
    void cleanUp() {
        matchRepo.deleteAll();
        teamRepo.deleteAll();
        seasonRepo.deleteAll();
    }

    @Test
    void fullFlow_createMatches_and_getStandings() throws Exception {
        Team a = teamRepo.save(Team.builder().name("Team A").build());
        Team b = teamRepo.save(Team.builder().name("Team B").build());
        Team c = teamRepo.save(Team.builder().name("Team C").build());

        Season season = seasonRepo.save(Season.builder()
                .name("2026/27")
                .startDate(java.time.LocalDateTime.now())
                .build());

        String json1 = createMatchJson(season.getId(), a.getId(), b.getId(), 2, 0);
        String json2 = createMatchJson(season.getId(), b.getId(), c.getId(), 1, 1);
        String json3 = createMatchJson(season.getId(), a.getId(), c.getId(), 0, 1);

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json1))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json3))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/seasons/{id}/standings", season.getId())
                        .param("date", "2026-10-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.standings.length()").value(3))
                .andExpect(jsonPath("$.standings[0].teamName").value("Team C"))
                .andExpect(jsonPath("$.standings[0].points").value(4))
                .andExpect(jsonPath("$.standings[1].teamName").value("Team A"))
                .andExpect(jsonPath("$.standings[1].points").value(3))
                .andExpect(jsonPath("$.standings[2].teamName").value("Team B"))
                .andExpect(jsonPath("$.standings[2].points").value(1));
    }

    private String createMatchJson(Long seasonId, Long homeId, Long guestId, int hScore, int gScore) {
        return """
            {
                "seasonId": %d,
                "homeTeamId": %d,
                "guestTeamId": %d,
                "matchDate": "2026-10-01T18:00:00",
                "homeScore": %d,
                "guestScore": %d,
                "round": 1
            }
            """.formatted(seasonId, homeId, guestId, hScore, gScore);
    }
}