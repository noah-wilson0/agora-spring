package com.agora.debate.debate.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "debate_rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebateRoom {

    @Id
    @Column(name = "board_id")
    private Long boardId; // 서버에서 직접 넣어줄 것이므로 @GeneratedValue 없음

    @Column(nullable = false)
    private String topic;

    // Hibernate 6 방식의 JSONB 매핑
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    @Builder.Default // 빌더 패턴 사용 시 리스트 초기화 유지
    private List<DebateLog> logs = new ArrayList<>();

    // ==========================================
    // [해결] DebateLog 클래스 정의 (DTO 역할)
    // ==========================================
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DebateLog implements Serializable {
        private String name;    // 화자 이름
        private String stance;  // 찬성/반대
        private String stage;   // 입론/반론/결론
        private String content; // 발언 내용
    }
}