package com.inn.restaurant.serviceImpl;

import com.google.common.base.Strings;
import com.inn.restaurant.jwt.CustomerUserDetailsService;
import com.inn.restaurant.jwt.JwtFilter;
import com.inn.restaurant.jwt.JwtUtil;
import com.inn.restaurant.model.User;
import com.inn.restaurant.constents.RestaurantConstants;
import com.inn.restaurant.repository.UserRepo;
import com.inn.restaurant.service.UserService;
import com.inn.restaurant.utils.EmailUtils;
import com.inn.restaurant.utils.RestaurantUtils;
import com.inn.restaurant.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.*;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<String> signUp(@RequestBody(required = false) Map<String, String> requestMap) {
        if (requestMap == null || requestMap.isEmpty()) {
            return ResponseEntity.badRequest().body("Request body is missing or empty");
        }
        log.info("Inside signUp {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userRepo.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    User newUser = getUserFromMap(requestMap);
                    newUser.setPassword(passwordEncoder.encode(requestMap.get("password"))); // Encrypt password
                    userRepo.save(getUserFromMap(requestMap));

                    return RestaurantUtils.getResponseEntity("Sign Up successful", HttpStatus.OK);
                } else {
                    return RestaurantUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }

            } else {
                return RestaurantUtils.getResponseEntity(RestaurantConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }


    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password"))); // Password encryption
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",
                            HttpStatus.BAD_REQUEST);

                }
            }

        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return new ResponseEntity<String>("{\"message\":\"" + "Invalid credentials." + "\"}",
                HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userRepo.getAllUsers(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userRepo.findById(Integer.parseInt(requestMap.get("id")));
                if (!optional.isEmpty()) {
                    userRepo.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userRepo.getAllAdmin());
                    return RestaurantUtils.getResponseEntity("Status updated successfully", HttpStatus.OK);
                } else {
                    return RestaurantUtils.getResponseEntity(("user id does not exist"), HttpStatus.OK);
                }
            } else {
                return RestaurantUtils.getResponseEntity(RestaurantConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepo.findByEmailId(jwtFilter.getCurrentUser());
            if (userObj != null) {
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                    userObj.setPassword(requestMap.get("newPassword"));
                    userRepo.save(userObj);
                    return RestaurantUtils.getResponseEntity("Password changed successfully", HttpStatus.OK);
                }
                return RestaurantUtils.getResponseEntity("Old password is incorrect", HttpStatus.BAD_REQUEST);
            }
            return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepo.findByEmailId(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgetMail(user.getEmail(),"Credentials by DineDirect user",user.getPassword());
            return RestaurantUtils.getResponseEntity("Check your email for password", HttpStatus.OK);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER:-" + user + " \n  is approved by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);

        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Deactivated", "USER:-" + user + " \n  is deactivated by \nADMIN:-" + jwtFilter.getCurrentUser(), allAdmin);
        }


    }
}

