package com.agora.debate.mainpage.service;

import com.agora.debate.mainpage.domain.Category;
import com.agora.debate.mainpage.dto.CategoryDto;
import com.agora.debate.mainpage.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category update(Integer id, Category updatedCategory) {
        return categoryRepository.findById(id).map(category -> {
            category.setCategoryName(updatedCategory.getCategoryName());
            category.setPart(updatedCategory.getPart());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }

    public Category createFromDto(CategoryDto dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());

        if (dto.getPartId() != null) {
            category.setPart(categoryRepository.findById(dto.getPartId()).orElse(null));
        } else {
            category.setPart(null);
        }

        return categoryRepository.save(category);
    }


}
