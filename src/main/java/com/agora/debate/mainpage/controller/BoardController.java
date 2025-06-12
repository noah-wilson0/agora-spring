package com.agora.debate.mainpage.controller;

import com.agora.debate.mainpage.dto.BoardDto;
import com.agora.debate.mainpage.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Boolean> createBoard(@RequestBody BoardDto boardDto) {
        boolean result = boardService.createBoard(boardDto);
        return ResponseEntity.ok(result);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<BoardDto>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable Long id) {
        BoardDto board = boardService.getBoard(id);
        return board != null ? ResponseEntity.ok(board) : ResponseEntity.notFound().build();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto) {
        boardDto.setBoardId(id);
        boolean result = boardService.updateBoard(boardDto);
        return ResponseEntity.ok(result);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBoard(@PathVariable Long id) {
        boolean result = boardService.deleteBoard(id);
        return ResponseEntity.ok(result);
    }
}
