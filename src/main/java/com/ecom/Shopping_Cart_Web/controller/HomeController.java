package com.ecom.Shopping_Cart_Web.controller;

import com.ecom.Shopping_Cart_Web.model.Category;
import com.ecom.Shopping_Cart_Web.model.Product;
import com.ecom.Shopping_Cart_Web.model.UserDtls;
import com.ecom.Shopping_Cart_Web.service.CategoryService;
import com.ecom.Shopping_Cart_Web.service.ProductService;
import com.ecom.Shopping_Cart_Web.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDtls());
        return "register";
    }

//    @GetMapping("/product")
//    public String product(Model model) {
//        List<Category> categories = categoryService.getAllActiveCategory();
//        List<Product> products = productService.getAllActiveProducts();
//        model.addAttribute("categories", categories);
//        model.addAttribute("products", products);
//
//        return "product";
//    }

    //modifying product module using request parameter in order to get page sorted according to required category
    @GetMapping("/product")
    public String product(Model model , @RequestParam(value = "category" ,defaultValue="")String category) {
        List<Category> categories = categoryService.getAllActiveCategory();
        List<Product> products = productService.getAllActiveProducts(category);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);

        //passing attribute to change color of category field when we click on particular category
        //i will use requestparm as i am using category in key value pair
        model.addAttribute("paramVal",category);

        return "product";
    }
    @GetMapping("/products/{product_id}")
    public String products(@PathVariable("product_id")Integer product_id, Model model) {
        Product product=productService.getProductById(product_id);
        model.addAttribute("product",product);
        return "products";
    }

    //methods for user registration


    @PostMapping("/admin/saveUser")
    public String saveUser(@ModelAttribute UserDtls user, @RequestParam("file") MultipartFile file) {
      String imageName=  file.isEmpty() ? "default.jpg":file.getOriginalFilename();
        user.setImage(imageName);
        UserDtls saveUser=userService.saveUser(user);
        //userService.saveUser(user);
       if(ObjectUtils.isEmpty(saveUser)){
           if(imageName!=null){
               try{
               File saveFile=new ClassPathResource("static/img").getFile();
               Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.separator+file.getOriginalFilename());
                   Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                   System.out.println("Saved User ");
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
       }else{
           System.out.println("Unable to save user");
       }
        return "redirect:/register";
    }
}
