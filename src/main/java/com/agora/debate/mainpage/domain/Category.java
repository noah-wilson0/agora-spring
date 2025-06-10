package com.agora.debate.mainpage.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    private Integer categoryId;

    @Column(nullable = false)
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "part")
    private Category part; // 자기 자신을 참조하는 연관 관계

    // Getter, Setter
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category getPart() {
        return part;
    }

    public void setPart(Category part) {
        this.part = part;
    }
}
