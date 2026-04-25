package com.practice.spring.controller;

import com.practice.spring.dto.MatchRequestDTO;
import com.practice.spring.dto.MatchResponseDTO;
import com.practice.spring.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchResponseDTO> registerMatch(@Valid @RequestBody MatchRequestDTO dto) {
        MatchResponseDTO created = matchService.registerMatch(dto);
        return ResponseEntity.created(URI.create("/matches/" + created.id())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<MatchResponseDTO>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> getMatchById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchResponseDTO> updateMatch(
            @PathVariable Long id,
            @Valid @RequestBody MatchRequestDTO dto) {
        return ResponseEntity.ok(matchService.updateMatch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}