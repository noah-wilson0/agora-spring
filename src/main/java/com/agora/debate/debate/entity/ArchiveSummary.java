package com.agora.debate.debate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "archive_summary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ArchiveSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "archive_summary_id")
    private Long archiveSummaryId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "pros_content", length = 1000, nullable = false)
    private String prosContent;

    @Column(name = "cons_content", length = 1000, nullable = false)
    private String consContent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;
}