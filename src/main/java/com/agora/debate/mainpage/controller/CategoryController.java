package com.agora.debate.mainpage.controller;

import com.agora.debate.debate.entity.Category;
import com.agora.debate.mainpage.dto.CategoryDto;
import com.agora.debate.mainpage.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // DTO -> Entity 변환
    private Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setCategoryName(dto.getCategoryName());

        if (dto.getPartId() != null) {
            categoryService.findById(dto.getPartId()).ifPresent(category::setPart);
        } else {
            category.setPart(null);
        }
        return category;
    }

    // Entity -> DTO 변환
    private CategoryDto toDto(Category category) {
        Integer partId = (category.getPart() != null) ? category.getPart().getCategoryId() : null;
        return new CategoryDto(category.getCategoryName(), partId);
    }

    // 생성 (Create)
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        Category category = toEntity(dto);
        Category saved = categoryService.create(category);
        return ResponseEntity.ok(toDto(saved));
    }

    // 전체 조회 (Read all)
    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        List<CategoryDto> list = categoryService.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // 단건 조회 (Read one)
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Integer id) {
        return categoryService.findById(id)
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 수정 (Update)
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Integer id, @RequestBody CategoryDto dto) {
        Category category = toEntity(dto);
        try {
            Category updated = categoryService.update(id, category);
            return ResponseEntity.ok(toDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 삭제 (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
