package com.inn.restaurant.entity;

import com.inn.restaurant.dto.UserDto;
import com.inn.restaurant.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data

public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;

    private String name;

    private String contactNumber;

    private String email;

    private String password;

    private UserRole role;


    public UserDto getDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(this.name);
        userDto.setEmail(this.email);
        userDto.setRole(role);

        return userDto;
    }

}
