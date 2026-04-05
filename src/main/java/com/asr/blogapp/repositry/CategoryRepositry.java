package com.asr.blogapp.repositry;

import com.asr.blogapp.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepositry extends JpaRepository<CategoryEntity, Long> {

    boolean existsByNameIgnoreCase(String name);

}
