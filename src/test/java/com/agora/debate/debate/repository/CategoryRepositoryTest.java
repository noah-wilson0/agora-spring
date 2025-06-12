package com.agora.debate.debate.repository;

import com.agora.debate.debate.entity.Category;
import com.agora.debate.mainpage.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void category_test() {
        List<Category> categories = categoryRepository.findAll();
        for (Category c : categories) {
            System.out.println("ID: " + c.getCategoryId() + ", 이름: " + c.getCategoryName() + ", part: " + c.getPart());
        }
    }
}
