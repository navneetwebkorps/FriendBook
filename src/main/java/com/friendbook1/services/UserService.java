package com.friendbook1.services;

import java.util.List;
import java.util.Map;

import com.friendbook1.entities.User;
import com.friendbook1.entities.UserProfile;

public interface UserService {
	 String registerUser(User user) ;

	 User findUserById(Integer userId) ;

	 User findUserByUsername(String username) ;

	 List<User> searchUser(String query);

	 User updateUser(User user) ;
	Boolean userLogin(User user);
	Map<String,byte[]> getUserSuggestions(String username);

}
