package com.inn.restaurant.services.authentication;

import com.inn.restaurant.dto.SignupRequestDTO;
import com.inn.restaurant.dto.UserDto;

public interface AuthService {

    UserDto signupCustomer(SignupRequestDTO signupRequestDTO);

    Boolean presentByEmail(String email);

}
