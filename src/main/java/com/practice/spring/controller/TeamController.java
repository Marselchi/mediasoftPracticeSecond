package com.practice.spring.controller;

import com.practice.spring.dto.TeamRequestDTO;
import com.practice.spring.dto.TeamResponseDTO;
import com.practice.spring.service.TeamService;
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
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Команды", description = "API для управления командами")
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    @Operation(summary = "Создать команду", description = "Регистрирует новую команду")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Команда успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или команда уже существует")
    })
    public ResponseEntity<TeamResponseDTO> createTeam(@Valid @RequestBody TeamRequestDTO dto) {
        TeamResponseDTO created = teamService.createTeam(dto);
        return ResponseEntity.created(URI.create("/teams/" + created.id())).body(created);
    }

    @GetMapping
    @Operation(summary = "Получить все команды", description = "Возвращает список всех зарегистрированных команд")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить команду по ID", description = "Возвращает информацию о конкретной команде")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда найдена"),
            @ApiResponse(responseCode = "404", description = "Команда не найдена")
    })
    public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить команду", description = "Обновляет название или короткое имя команды")
    public ResponseEntity<TeamResponseDTO> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequestDTO dto) {
        return ResponseEntity.ok(teamService.updateTeam(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить команду", description = "Удаляет команду по ID")
    @ApiResponse(responseCode = "204", description = "Команда успешно удалена")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}