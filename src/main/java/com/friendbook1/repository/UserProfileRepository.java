package com.friendbook1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.friendbook1.entities.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {


    UserProfile findByUsername(String username);
}
