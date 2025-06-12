package com.agora.debate;

import com.agora.debate.debate.entity.Category;
import com.agora.debate.mainpage.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void 카테고리_생성_조회_수정_삭제_테스트() {
        // 생성
        Category category = new Category();
        category.setCategoryId(1); // 직접 id 부여 (중복 주의)
        category.setCategoryName("토론");
        category.setPart(null);
        Category saved = categoryService.create(category);
        assertThat(saved.getCategoryId()).isNotNull();


        // 조회
        List<Category> all = categoryService.findAll();
        assertThat(all.size()).isGreaterThan(0);

        // 수정
        saved.setCategoryName("정치 토론");
        Category updated = categoryService.update(saved.getCategoryId(), saved);
        assertThat(updated.getCategoryName()).isEqualTo("정치 토론");

        // 삭제
        categoryService.delete(saved.getCategoryId());
        assertThat(categoryService.findById(saved.getCategoryId())).isEmpty();
    }
}
