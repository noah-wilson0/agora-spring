package com.agora.debate;

import com.agora.debate.mainpage.dto.BoardDto;
import com.agora.debate.mainpage.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void testCRUD() {
        // 1. CREATE
        BoardDto newBoard = new BoardDto();
        newBoard.setCategoryId(1);
        newBoard.setTitle("테스트 게시글 생성");
        newBoard.setState("대기중");
        newBoard.setCreatedAt(LocalDate.parse(LocalDate.now().toString()));  // "2025-05-28"
        boolean created = boardService.createBoard(newBoard);
        assertThat(created).isTrue();

        // 2. READ ALL
        List<BoardDto> allBoards = boardService.getAllBoards();
        assertThat(allBoards).isNotEmpty();

        // 3. READ ONE (가장 최근 게시글)
        BoardDto lastBoard = allBoards.get(allBoards.size() - 1);
        BoardDto foundBoard = boardService.getBoard(lastBoard.getBoardId());
        assertThat(foundBoard).isNotNull();
        assertThat(foundBoard.getTitle()).isEqualTo("테스트 게시글 생성");

        // 4. UPDATE
        foundBoard.setTitle("수정된 제목");
        foundBoard.setState("완료");
        boolean updated = boardService.updateBoard(foundBoard);
        assertThat(updated).isTrue();

        BoardDto updatedBoard = boardService.getBoard(foundBoard.getBoardId());
        assertThat(updatedBoard.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedBoard.getState()).isEqualTo("완료");

        // 5. DELETE
        boolean deleted = boardService.deleteBoard(updatedBoard.getBoardId());
        assertThat(deleted).isTrue();

        BoardDto deletedBoard = null;
        try {
            deletedBoard = boardService.getBoard(updatedBoard.getBoardId());
        } catch (Exception e) {
            deletedBoard = null;
        }
        assertThat(deletedBoard).isNull();
    }
}
