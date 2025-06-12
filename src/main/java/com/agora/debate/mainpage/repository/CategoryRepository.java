package com.agora.debate.mainpage.repository;

import com.agora.debate.mainpage.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
