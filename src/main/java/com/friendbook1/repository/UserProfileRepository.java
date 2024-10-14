package com.friendbook1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.friendbook1.entities.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {


	UserProfile findByUsername(String username);
}
