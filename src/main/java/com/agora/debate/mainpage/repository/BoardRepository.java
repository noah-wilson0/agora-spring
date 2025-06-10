package com.agora.debate.mainpage.repository;

import com.agora.debate.mainpage.dto.BoardDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class BoardRepository {

    private final JdbcTemplate jdbcTemplate;

    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // CREATE
    public int insertBoard(BoardDto board) {
        if (board.getState() == null) {
            board.setState("대기중");
        }
        if (board.getCreatedAt() == null) {
            // BoardDto의 createdAt 타입이 LocalDate라면, 현재 날짜로 세팅
            board.setCreatedAt(LocalDate.now());
        }
        String sql = "INSERT INTO board (category_id, title, state, created_at) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                board.getCategoryId(),
                board.getTitle(),
                board.getState(),
                board.getCreatedAt());
    }



    // READ (전체 조회)
    public List<BoardDto> findAllBoards() {
        String sql = "SELECT board_id, title, category_id, state, TO_CHAR(created_at, 'YYYY-MM-DD') AS created_at FROM board";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BoardDto b = new BoardDto();
            b.setBoardId(rs.getLong("board_id"));
            b.setTitle(rs.getString("title"));
            b.setCategoryId(rs.getInt("category_id"));
            b.setState(rs.getString("state"));
            b.setCreatedAt(LocalDate.parse(rs.getString("created_at")));
            return b;
        });
    }

    // READ (단건 조회)
    public BoardDto findById(Long boardId) {
        String sql = "SELECT board_id, title, category_id, state, TO_CHAR(created_at, 'YYYY-MM-DD') AS created_at FROM board WHERE board_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{boardId}, (rs, rowNum) -> {
            BoardDto b = new BoardDto();
            b.setBoardId(rs.getLong("board_id"));
            b.setTitle(rs.getString("title"));
            b.setCategoryId(rs.getInt("category_id"));
            b.setState(rs.getString("state"));
            // String → LocalDate 변환
            b.setCreatedAt(LocalDate.parse(rs.getString("created_at")));
            return b;
        });
    }


    // UPDATE
    public int updateBoard(BoardDto board) {
        String sql = "UPDATE board SET title = ?, state = ? WHERE board_id = ?";
        return jdbcTemplate.update(sql,
                board.getTitle(),
                board.getState(),
                board.getBoardId());
    }

    // DELETE
    public int deleteBoard(Long boardId) {
        String sql = "DELETE FROM board WHERE board_id = ?";
        return jdbcTemplate.update(sql, boardId);
    }
}
