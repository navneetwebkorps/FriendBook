package com.friendbook1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.friendbook1.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	 Optional<User> findByEmail(String email);
	 
	 Optional<User> findByUsername(String Username);
	 List<User> findByUsernameNot(String username);
	 
	 @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.Fname LIKE %:keyword% OR u.Lname LIKE %:keyword%")
	    List<User> searchByUsernameOrName(@Param("keyword") String keyword);
}
