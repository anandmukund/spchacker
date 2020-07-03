package com.covid.spchacker.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.covid.spchacker.entity.User;
 
public interface UserRepository extends CrudRepository<User, Long> {
 
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
    
    @Query("SELECT u FROM User u WHERE u.auth = :auth")
    public User getUserByAuth(@Param("auth") String auth);
    
    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);
}