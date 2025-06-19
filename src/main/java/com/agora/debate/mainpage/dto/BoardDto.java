package com.agora.debate.mainpage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class BoardDto {
    private Long boardId;
    private String title;
    @JsonProperty("category_id")
    private int categoryId;
    private String description;
    private String state;
    private LocalDate createdAt;

    public BoardDto() {}

    public BoardDto(Long boardId, String title, int categoryId, String description, String state, LocalDate createdAt) {
        this.boardId = boardId;
        this.title = title;
        this.description=description;
        this.categoryId = categoryId;
        this.state = state;
        this.createdAt = createdAt;
    }

    // getter / setter 생략하지 말고 다 만들어주세요.
    // IDE로 자동 생성 가능
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getBoardId() { return boardId; }
    public void setBoardId(Long boardId) { this.boardId = boardId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
