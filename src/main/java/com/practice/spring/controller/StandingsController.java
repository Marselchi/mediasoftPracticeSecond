package com.practice.spring.controller;

import com.practice.spring.dto.StandingsResponseDTO;
import com.practice.spring.service.StandingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor
public class StandingsController {
    private final StandingsService standingsService;

    @GetMapping("/{seasonId}/standings")
    public ResponseEntity<StandingsResponseDTO> getStandings(
            @PathVariable Long seasonId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(standingsService.getStandings(seasonId, targetDate));
    }
}
