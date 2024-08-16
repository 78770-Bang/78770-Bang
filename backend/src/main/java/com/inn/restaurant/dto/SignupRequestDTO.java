package com.inn.restaurant.dto;

import lombok.Data;


@Data
public class SignupRequestDTO {

    private Long id;

    private String name;

    private String contactNumber;

    private String email;

    private String password;

}
