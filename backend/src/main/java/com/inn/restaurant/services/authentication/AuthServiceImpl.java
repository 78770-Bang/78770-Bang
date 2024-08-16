package com.inn.restaurant.services.authentication;

import com.inn.restaurant.dto.SignupRequestDTO;
import com.inn.restaurant.dto.UserDto;
import com.inn.restaurant.entity.User;
import com.inn.restaurant.enums.UserRole;
import com.inn.restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;



    public UserDto signupCustomer(SignupRequestDTO signupRequestDTO) {
        User user = new User();

        user.setName(signupRequestDTO.getName());
        user.setContactNumber(signupRequestDTO.getContactNumber());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));
        user.setRole(UserRole.CUSTOMER);

        return userRepository.save(user).getDto();

    }


    public Boolean presentByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
