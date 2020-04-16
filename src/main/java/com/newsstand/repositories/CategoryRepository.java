package com.newsstand.repositories;

import com.newsstand.models.Category;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM category WHERE user_id=:id")
    void deleteCategoriesofAUser(
            @Param("id") int id);

}
