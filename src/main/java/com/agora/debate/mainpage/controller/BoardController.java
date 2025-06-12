package com.agora.debate.mainpage.controller;

import com.agora.debate.mainpage.dto.BoardDto;
import com.agora.debate.mainpage.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Boolean> createBoard(@RequestBody BoardDto boardDto) {
        log.info("성공");
        boolean result = boardService.createBoard(boardDto);
        log.info("faile");
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
        log.info("시작");
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
