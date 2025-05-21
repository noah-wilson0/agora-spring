package com.agora.debate.debate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agora.debate.debate.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}

