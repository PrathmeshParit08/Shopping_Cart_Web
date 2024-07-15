package com.ecom.Shopping_Cart_Web.controller;

import com.ecom.Shopping_Cart_Web.model.Product;
import com.ecom.Shopping_Cart_Web.service.ProductService;
import org.springframework.ui.Model;

import com.ecom.Shopping_Cart_Web.model.Category;
import com.ecom.Shopping_Cart_Web.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller

public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private ProductService productService;

    //load admin page

    @GetMapping("/admin")
    public String adminpage(){
        return "admin/index";
    }
    //load add product page method to add product
 @GetMapping("/admin/add_product")
    public String add_product(Model model){
        List<Category> categories= categoryService.getAllCategory();
        model.addAttribute("categories",categories);

        return "admin/add_product";
 }

 //methods for add category
 //load category page
 @GetMapping("/admin/category")
    public String category(Model model){
        //i used Model to show list of   incoming categories in category details coming from add category      
        model.addAttribute("categories",categoryService.getAllCategory()); //getting all categories and storing inside categories to fetch using loop
                                                                                        //to print inside category details
        return "admin/category";
 }



 @PostMapping("/admin/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file
                               ) throws IOException {
     //httpsession =data get removed from method after refresh
    String filename= file!=null ? file.getOriginalFilename():"default.jpg";
    category.setImageName(filename);
     Boolean existsCategory=categoryService.existCategory(category.getName());
     if (existsCategory) {
         //if category already exists then display error message
         System.out.println("Category already exist");
     } else {
         //else save category
         Category saveCategory=categoryService.saveCategory(category);
         //checking whether objet is empty

         if(saveCategory==null){
             System.out.println("Unable to save !Internal server error");
         }else{

             //when isActive is true and also file is not null we want to save uploaded image in category_img in order to add it in ui page
             //implementing logic for image upload in  src/resources/static/img/category_img/

             File saveFile=new ClassPathResource("static/img").getFile(); //File.seperator is basically "/"
             //File.getAbsolutePath() moves our scope to static/umg i.e path..
             Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+
                     file.getOriginalFilename());
             System.out.println(path);
             Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
             System.out.println("saved successfully" );
         }

     }
         return "redirect:/admin/category"; //redirect method used when we want to save an item and remove after
      }                                      // want to store that but remove from method


//storing data from add category form to category details by creating controller method..

    @GetMapping("/admin/category/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") int id,HttpSession session) {
        Boolean deleteCategory=categoryService.deleteCategory(id);
        if(deleteCategory){
            session.setAttribute("deleteMsg", "Category deleted successfully");
        }else{
            session.setAttribute("errorMsg", "Unable to delete category");
        }
        return "redirect:/admin/category";
    }
    //Model model attribute is used to reflect changes is ui

    @GetMapping("/admin/editcategory/{id}")
    public String loadeditcategory(@PathVariable("id") int id,Model model) {
        model.addAttribute("category",categoryService.getCategoryById(id));
        return "admin/editcategory";
    }

    //method to update category inside admin/category
    @PostMapping("/admin/updateCategory")
    public String updateCategory(@ModelAttribute Category category,
                                 @RequestParam("file") MultipartFile file) throws IOException {
       Category Oldcategory= categoryService.getCategoryById(category.getId());
       String ImageName= file!=null ? file.getOriginalFilename():Oldcategory.getImageName();


        if(Oldcategory!=null){
           Oldcategory.setName(category.getName());
           Oldcategory.setIsActive(category.getIsActive());
            Oldcategory.setImageName(ImageName);
        }
    Category updateCategory=  categoryService.saveCategory(Oldcategory);
    if(updateCategory!=null){
        if(file!=null){
            File saveFile=new ClassPathResource("static/img").getFile(); //File.seperator is basically "/"
            //File.getAbsolutePath() moves our scope to static/umg i.e path..
            Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+"category_img"+File.separator+
                    file.getOriginalFilename());
            System.out.println(path);
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("saved successfully" );
        }
        System.out.println("updateCategory");

    }else{
        System.out.println(" unable to update Category");
    }
        return "redirect:/admin/editcategory/"+category.getId();
    }




    //methods for add product

    @PostMapping("/admin/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image) throws IOException {
        String imageName = image != null ? image.getOriginalFilename() : "default.jpg";
        product.setImage(imageName);
        product.setDiscount(0); // default discount 0%
        product.setDiscount_price(product.getPrice());
        Product saveproduct = productService.saveProduct(product);
        if (saveproduct != null) {
            File saveFile = new ClassPathResource("static/img").getFile();
            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator + image.getOriginalFilename());
            try {
                Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image saved successfully");
            } catch (IOException e) {
                System.out.println("Error saving image: " + e.getMessage());
                // Optionally delete the product record if image saving fails
                productService.deleteProduct(saveproduct.getProduct_id());
            }
        }
        return "redirect:/admin/add_product";
    }


    //methods for viewproduct
    @GetMapping("/admin/viewProducts")
    public String viewProduct(Model model){
        model.addAttribute("products",productService.getAllProducts());

        return "admin/viewProducts";
    }
    @GetMapping("/admin/viewProducts/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") int id, Model model){
       productService.deleteProduct(id);

        return "redirect:/admin/viewProducts";
    }
    @GetMapping("/admin/editPrice/{id}")
    public String editPrice(Model model,@PathVariable("id") int id){
        model.addAttribute("products",productService.getProductById(id));
        //to get categories at panel
        model.addAttribute("categories",categoryService.getAllCategory());
        return "/admin/editPrice";

    }
    @PostMapping("/admin/updateProduct")
    public String updateProduct(@ModelAttribute Product product,Model m,@RequestParam("file") MultipartFile file) throws IOException {

       Product updatep= productService.updateProduct(product,file);
       if(updatep!=null){
           System.out.println("update successfully");

       }else{
           System.out.println("unable to update Product");
       }return "redirect:/admin/editPrice/"+product.getProduct_id();
    }

}
