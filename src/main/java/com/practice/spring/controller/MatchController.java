package com.practice.spring.controller;

import com.practice.spring.dto.MatchRequestDTO;
import com.practice.spring.dto.MatchResponseDTO;
import com.practice.spring.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
@Tag(name = "Матчи", description = "API для управления матчами")
public class MatchController {
    private final MatchService matchService;

    @PostMapping
    @Operation(summary = "Зарегистрировать матч", description = "Создает новый матч между двумя командами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Матч успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    public ResponseEntity<MatchResponseDTO> registerMatch(@Valid @RequestBody MatchRequestDTO dto) {
        MatchResponseDTO created = matchService.registerMatch(dto);
        return ResponseEntity.created(URI.create("/matches/" + created.id())).body(created);
    }

    @GetMapping
    @Operation(summary = "Получить все матчи", description = "Возвращает список всех матчей")
    public ResponseEntity<List<MatchResponseDTO>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить матч по ID", description = "Возвращает детали конкретного матча")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Матч найден"),
            @ApiResponse(responseCode = "404", description = "Матч не найден")
    })
    public ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить матч", description = "Обновляет счет, статус или другие данные матча")
    public ResponseEntity<MatchResponseDTO> updateMatch(
            @PathVariable Long id,
            @Valid @RequestBody MatchRequestDTO dto) {
        return ResponseEntity.ok(matchService.updateMatch(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить матч", description = "Удаляет матч из системы")
    @ApiResponse(responseCode = "204", description = "Матч успешно удален")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}