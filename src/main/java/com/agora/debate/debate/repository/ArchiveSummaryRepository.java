package com.agora.debate.debate.repository;

import com.agora.debate.debate.entity.ArchiveSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArchiveSummaryRepository extends JpaRepository<ArchiveSummary, Long> {
    // board_id로 요약본 찾기
    Optional<ArchiveSummary> findByBoardId(Long boardId);
}