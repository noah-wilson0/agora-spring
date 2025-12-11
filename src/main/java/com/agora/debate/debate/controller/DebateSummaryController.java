package com.agora.debate.debate.controller;

import com.agora.debate.debate.entity.ArchiveSummary;
import com.agora.debate.debate.service.DebateSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/debates")
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class DebateSummaryController {

    private final DebateSummaryService summaryService;

    // GET /api/debates/{boardId}/summary
    @GetMapping("/{boardId}/summary")
    public ResponseEntity<ArchiveSummary> getSummary(@PathVariable Long boardId) {
        ArchiveSummary summary = summaryService.getOrGenerateSummary(boardId);
        return ResponseEntity.ok(summary);
    }
}