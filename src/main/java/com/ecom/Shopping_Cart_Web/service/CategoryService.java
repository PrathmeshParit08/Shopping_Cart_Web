package com.ecom.Shopping_Cart_Web.service;

import com.ecom.Shopping_Cart_Web.model.Category;

import java.util.List;

public interface CategoryService {

    public Boolean existCategory(String categoryName);
    public Category saveCategory(Category category);
    //method to fetch all categories store in database inside category module
    public List<Category> getAllCategory();

    //method to delete category
    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);
    //method to get all active categories in order to show on user side pages

    public List<Category> getAllActiveCategory();
}
