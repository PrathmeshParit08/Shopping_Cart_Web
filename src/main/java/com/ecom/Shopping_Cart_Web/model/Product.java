package com.ecom.Shopping_Cart_Web.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.processing.Generated;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;
    @Column(length = 500)
    private String title;
    @Column(length = 500)
    private String description;
    private String category;
    private double price;
    private int stock;
    private String image;
    private int discount;
    private double discount_price;
    private Boolean isActive;

}
