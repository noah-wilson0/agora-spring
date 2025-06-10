package com.agora.debate.mainpage.dto;

public class CategoryDto {

    private String categoryName;
    private Integer partId; // 부모 카테고리의 ID

    public CategoryDto() {
    }

    public CategoryDto(String categoryName, Integer partId) {
        this.categoryName = categoryName;
        this.partId = partId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }
}
