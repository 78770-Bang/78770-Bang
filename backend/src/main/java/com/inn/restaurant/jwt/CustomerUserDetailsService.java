package com.inn.restaurant.jwt;

import com.inn.restaurant.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service

public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    private com.inn.restaurant.model.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("User name is {}", username);
        userDetail = userRepo.findByEmailId(username);
        if (!Objects.isNull(userDetail))
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        else
            throw new UsernameNotFoundException("User not found with username: " + username);
    }

    public com.inn.restaurant.model.User getUserDetail() {
        com.inn.restaurant.model.User user = userDetail;
        user.setPassword(null);
        return user;
    }
}
