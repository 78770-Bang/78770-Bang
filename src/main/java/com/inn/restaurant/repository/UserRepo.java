package com.inn.restaurant.repository;

import com.inn.restaurant.model.User;
import com.inn.restaurant.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Integer> {

    User findByEmailId(@Param("email") String email);

    List<UserDto> getAllUsers();

    List<String> getAllAdmin();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

}



