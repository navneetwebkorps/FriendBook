package com.friendbook1.services;

import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.Catch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook1.entities.UserProfile;
import com.friendbook1.entities.User;
import com.friendbook1.entities.UserFollowing;
import com.friendbook1.repository.UserFollowingRepository;
import com.friendbook1.repository.UserProfileRepository;
import com.friendbook1.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServices {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserProfileRepository userProfileRepository;
	@Autowired
	private UserFollowingRepository userFollowingRepository;

	public String sessionCheck(HttpSession session)
	{System.out.println("in session");
		try {
		if(session.equals(null))
		return "LogIn";
		
	String username=	(String)session.getAttribute("username");
	
	if(username==null)
	{
		return "LogIn";
	}
	
		}catch (Exception e) {
			// TODO: handle exception
			return "LogIn";
		}
	

	return "";

	}		}
	


