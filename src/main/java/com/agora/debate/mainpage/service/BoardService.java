package com.agora.debate.mainpage.service;

import com.agora.debate.mainpage.dto.BoardDto;
import com.agora.debate.mainpage.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<BoardDto> getAllBoards() {
        return boardRepository.findAllBoards();
    }

    public BoardDto getBoard(Long boardId) {
        return boardRepository.findById(boardId);
    }

    public boolean createBoard(BoardDto board) {
        return boardRepository.insertBoard(board) > 0;
    }

    public boolean updateBoard(BoardDto board) {
        return boardRepository.updateBoard(board) > 0;
    }

    public boolean deleteBoard(Long boardId) {
        return boardRepository.deleteBoard(boardId) > 0;
    }

}
