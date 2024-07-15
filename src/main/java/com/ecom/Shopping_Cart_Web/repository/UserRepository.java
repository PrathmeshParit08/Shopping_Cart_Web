package com.ecom.Shopping_Cart_Web.repository;


import com.ecom.Shopping_Cart_Web.model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer> {
}
