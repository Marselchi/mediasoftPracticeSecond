package com.practice.spring.controller;

import com.practice.spring.dto.MatchRequestDTO;
import com.practice.spring.dto.MatchResponseDTO;
import com.practice.spring.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    //"Сервис должен реализовывать возможность регистрации результата матча..."
    // Соответственно тоже без круда
    @PostMapping
    public ResponseEntity<MatchResponseDTO> registerMatch(@Valid @RequestBody MatchRequestDTO dto) {
        MatchResponseDTO created = matchService.registerMatch(dto);
        return ResponseEntity.created(URI.create("/matches/" + created.id())).body(created);
    }
}