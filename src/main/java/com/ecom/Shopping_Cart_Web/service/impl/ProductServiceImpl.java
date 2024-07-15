package com.ecom.Shopping_Cart_Web.service.impl;

import com.ecom.Shopping_Cart_Web.model.Category;
import com.ecom.Shopping_Cart_Web.model.Product;
import com.ecom.Shopping_Cart_Web.repository.ProductRepository;
import com.ecom.Shopping_Cart_Web.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Boolean deleteProduct(int id) {
        Product product=productRepository.findById(id).orElse(null);
        if(product!=null){
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Product product, MultipartFile file) {
        Product dbProduct = getProductById(product.getProduct_id());
        if (dbProduct == null) {
            // Handle the case where product is not found
            return null;
        }

        String imageName = file.isEmpty() ? dbProduct.getImage() : file.getOriginalFilename();

        dbProduct.setTitle(product.getTitle());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setCategory(product.getCategory());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setDiscount(product.getDiscount());
        //setting discount price at compile time
        dbProduct.setDiscount_price(product.getPrice()-(product.getPrice()*product.getDiscount()/100));
        dbProduct.setIsActive(product.getIsActive());
        dbProduct.setStock(product.getStock());

        try {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            return productRepository.save(dbProduct);
        } catch (IOException e) {
            // Handle file operation error
            e.printStackTrace();
            return null;
        }
    }
    //this method is for to get products page sorted according to required category

    @Override
    public List<Product> getAllActiveProducts(String category) {
        List<Product>products =null;
        if(category==null || category.isEmpty()){
            products= productRepository.findByIsActive(true);
        }else{
            products=productRepository.findByCategory(category);
        }


    return products;
    }


}


