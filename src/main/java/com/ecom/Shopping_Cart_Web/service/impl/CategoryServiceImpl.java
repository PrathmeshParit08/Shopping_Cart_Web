package com.ecom.Shopping_Cart_Web.service.impl;

import com.ecom.Shopping_Cart_Web.model.Category;
import com.ecom.Shopping_Cart_Web.repository.CategoryRepository;
import com.ecom.Shopping_Cart_Web.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

import static org.thymeleaf.util.ObjectUtils.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    //method to check whether the category we are adding already exists or not

    @Override
    public Boolean existCategory(String Name) {
        return categoryRepository.existsByName(Name);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
//method to delete category from category details
    @Override
    public Boolean deleteCategory(int id) {
 //getting category to delete it
        Category category= categoryRepository.findById(id).orElse(null);
        if(!ObjectUtils.isEmpty(category)){
            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        Category category= categoryRepository.findById(id).orElse(null);
       return category;
    }
    //method for getting active categories from admin database
    @Override
    public List<Category> getAllActiveCategory() {
       List<Category> categories= categoryRepository.findByIsActiveTrue();
       return categories;
    }




}
