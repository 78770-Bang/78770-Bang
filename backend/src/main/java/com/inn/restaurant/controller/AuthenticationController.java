package com.inn.restaurant.controller;

import com.inn.restaurant.dto.AuthenticationRequest;
import com.inn.restaurant.dto.SignupRequestDTO;
import com.inn.restaurant.dto.UserDto;
import com.inn.restaurant.repository.UserRepository;
import com.inn.restaurant.services.authentication.AuthService;
import com.inn.restaurant.services.jwt.UserDetailsServiceImpl;
import com.inn.restaurant.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;


@RestController
public class AuthenticationController {


    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private   JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";


    @PostMapping("/user/signup")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequestDTO signupRequestDTO) {
       if(authService.presentByEmail(signupRequestDTO.getEmail())){
           return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
       }

        UserDto createdUser = authService.signupCustomer(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                         HttpServletResponse response) throws IOException, JSONException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()
            ));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }

        com.inn.restaurant.entity.User user = userRepository.findByEmail(authenticationRequest.getUsername());
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>()
        );

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Authorization", TOKEN_PREFIX + jwt);

        JSONObject responseJson = new JSONObject()
                .put("userid", user.getId())
                .put("role", user.getRole().toString())
                .put("token", jwt);

        return ResponseEntity.ok(responseJson.toString());
    }



}


