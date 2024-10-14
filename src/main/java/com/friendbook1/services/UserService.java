package com.friendbook1.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.ui.Model;

import com.friendbook1.entities.User;

public interface UserService {
	String registerUser(User user);

	User findUserById(Integer userId);

	User findUserByUsername(String username);

	List<User> searchUser(String query);

	User updateUser(User user);

	Boolean userLogin(User user);

	Map<String, byte[]> getUserSuggestions(String username);

	Optional<User> authenticate(String email);

	Model userHome(String username, Model model);

	Model userProfile(String username, Model model);

	Model userNotification(String username, Model model);

	Model fViewProfile(String fUsername, Model model);

}
