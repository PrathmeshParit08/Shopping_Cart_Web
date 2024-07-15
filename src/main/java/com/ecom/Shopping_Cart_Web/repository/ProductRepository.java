package com.ecom.Shopping_Cart_Web.repository;

import com.ecom.Shopping_Cart_Web.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


     List<Product> findByIsActive(Boolean isActive);

     List<Product> findByCategory(String category);
}
