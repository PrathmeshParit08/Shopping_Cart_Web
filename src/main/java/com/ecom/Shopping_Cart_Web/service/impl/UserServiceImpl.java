package com.ecom.Shopping_Cart_Web.service.impl;

import com.ecom.Shopping_Cart_Web.model.UserDtls;
import com.ecom.Shopping_Cart_Web.repository.UserRepository;
import com.ecom.Shopping_Cart_Web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDtls saveUser(UserDtls userDtls) {
        UserDtls saveUser=userRepository.save(userDtls);
        return saveUser;
    }
}
