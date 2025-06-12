package com.agora.debate.debate.entity;

import com.agora.debate.global.enums.BoardState;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(nullable = false, length = 255)
    private String title;

    @Enumerated(EnumType.STRING)   // <- 중요! 문자열로 Enum을 저장
    @Column(nullable = false, length = 20)
    private BoardState state = BoardState.대기중;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 연관관계 (옵션, 필요 시 사용)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "category_id", insertable = false, updatable = false)
    // private Category category;
}