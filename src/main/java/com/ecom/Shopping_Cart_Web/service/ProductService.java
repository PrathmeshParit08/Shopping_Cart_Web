package com.ecom.Shopping_Cart_Web.service;

import com.ecom.Shopping_Cart_Web.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);
    public List<Product> getAllProducts();

    Boolean deleteProduct(int id);

    public Product getProductById(Integer id);
    public Product updateProduct(Product product, MultipartFile file) throws IOException;

    //method for getting all active products on user side view

    public  List<Product> getAllActiveProducts(String category);



}
