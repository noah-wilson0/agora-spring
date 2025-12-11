package com.agora.debate.debate.service;

import com.agora.debate.debate.dto.FastApiSummaryResponse;
import com.agora.debate.debate.entity.ArchiveSummary;
import com.agora.debate.debate.entity.DebateRoom;
import com.agora.debate.debate.repository.ArchiveSummaryRepository;
import com.agora.debate.debate.repository.DebateRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebateSummaryService {

    private final ArchiveSummaryRepository summaryRepository;
    private final DebateRoomRepository roomRepository; // 1번 요청하신 레포지토리 사용
    private final RestTemplate restTemplate;

    // FastAPI 주소
    private final String FASTAPI_URL = "http://127.0.0.1:8001/api/v1/summarize";

    @Transactional
    public ArchiveSummary getOrGenerateSummary(Long boardId) {
        // 1. DB에 요약본이 있는지 확인 (있으면 바로 리턴)
        return summaryRepository.findByBoardId(boardId)
                .orElseGet(() -> requestToAiAndSave(boardId));
    }

    // AI에게 요청하고 결과를 저장하는 메서드
    private ArchiveSummary requestToAiAndSave(Long boardId) {
        log.info("AI 요약 요청 시작 - Board ID: {}", boardId);

        // 1. 토론방 정보 및 로그 가져오기
        DebateRoom room = roomRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 토론방입니다."));

        // 2. FastAPI로 보낼 데이터 준비
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("board_id", boardId);
        requestMap.put("topic", room.getTopic());
        requestMap.put("logs", room.getLogs());

        // 3. FastAPI 호출 (결과를 DTO로 바로 받음!)
        FastApiSummaryResponse response = restTemplate.postForObject(
                FASTAPI_URL,
                requestMap,
                FastApiSummaryResponse.class
        );

        if (response == null) {
            throw new RuntimeException("FastAPI 서버 응답이 없습니다.");
        }

        // 🔍 [디버깅용 로그] 실제로 뭐가 왔는지 눈으로 확인해보세요!
        log.info("FastAPI 응답 확인: pros={}, cons={}", response.getProsContent(), response.getConsContent());

        // ★ [핵심 해결책] 값이 null이면 "요약 실패"라는 문자열이라도 넣어서 DB 에러를 막습니다.
        String safePros = (response.getProsContent() != null) ? response.getProsContent() : "찬성 측 요약 내용을 가져오지 못했습니다.";
        String safeCons = (response.getConsContent() != null) ? response.getConsContent() : "반대 측 요약 내용을 가져오지 못했습니다.";

        ArchiveSummary newSummary = ArchiveSummary.builder()
                .boardId(boardId)
                .prosContent(safePros)  // 절대 null이 들어갈 수 없음
                .consContent(safeCons)  // 절대 null이 들어갈 수 없음
                // .createdAt(LocalDate.now()) // 엔티티에 @CreationTimestamp 있다면 생략 가능
                .build();

        // 4. 받아온 데이터를 내 DB 엔티티(ArchiveSummary)로 변환
//        ArchiveSummary newSummary = ArchiveSummary.builder()
//                .boardId(response.getBoardId())
//                .prosContent(response.getProsContent()) // 찬성 요약
//                .consContent(response.getConsContent()) // 반대 요약
//                .createdAt(LocalDate.now())
//                .build();

        // 5. 저장 및 리턴
        return summaryRepository.save(newSummary);
    }
}