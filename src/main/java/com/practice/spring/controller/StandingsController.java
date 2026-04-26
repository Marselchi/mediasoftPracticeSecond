package com.practice.spring.controller;

import com.practice.spring.dto.StandingsResponseDTO;
import com.practice.spring.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/seasons")
@RequiredArgsConstructor
@Tag(name = "Турнирная таблица", description = "API для получения турнирной таблицы сезонов")
public class StandingsController {
    private final StandingsService standingsService;

    @GetMapping("/{seasonId}/standings")
    @Operation(summary = "Получить турнирную таблицу", description = "Возвращает таблицу на указанную дату или на текущий момент")
    public ResponseEntity<StandingsResponseDTO> getStandings(
            @PathVariable Long seasonId,
            @Parameter(description = "Дата среза таблицы (по умолчанию сегодня)", example = "2026-10-01")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        return ResponseEntity.ok(standingsService.getStandings(seasonId, targetDate));
    }
}