package com.ecom.Shopping_Cart_Web.repository;

import com.ecom.Shopping_Cart_Web.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public Boolean existsByName(String name);

    public List<Category> findByIsActiveTrue();


}
