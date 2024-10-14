package com.friendbook1.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.friendbook1.Exception.UserException;
import com.friendbook1.entities.User;
import com.friendbook1.entities.UserProfile;
import com.friendbook1.repository.UserRepository;
import com.friendbook1.services.UserProfileService;
import com.friendbook1.services.UserService;
import com.friendbook1.services.impl.UserFollowServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserFollowServiceImpl userFollowService;
	@Autowired
	UserProfileService userProfileService;

	@GetMapping("/home")
	@ResponseBody
	public Object home(Model model, HttpSession session, @ModelAttribute("user") User user, String email) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = "";

		try {
			if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				username = userDetails.getUsername();
			} else {
				username = authentication.getPrincipal().toString();
			}

			User user1 = userRepository.findByEmail(username).orElseThrow(() -> new UserException("User not found"));
			username = user1.getUsername();
			model = userService.userHome(username, model);
			session.setAttribute("username", username);
		} catch (UserException e) {
			logger.error("Error fetching user details: {0}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error in home: {0}", e.getMessage());
			throw new UserException("An unexpected error occurred");
		}

		return new ModelAndView("Home");
	}

	@GetMapping("/profile")
	public Object userProfile(Model model, HttpSession session) {
		String username = session.getAttribute("username").toString();
		model = userService.userProfile(username, model);
		return new ModelAndView("Profile");
	}

	@GetMapping("/userSuggestion")
	public ModelAndView userSuggestion(Model model, HttpSession session) {
		try {
			String username = (String) session.getAttribute("username");
			User user1 = userRepository.findByUsername(username).orElseThrow(() -> new UserException("User not found"));
			Map<String, byte[]> userSuggestions = userService.getUserSuggestions(user1.getUsername());
			model.addAttribute("userSuggestions", userSuggestions);
			return new ModelAndView("Suggestion");
		} catch (Exception e) {
			logger.error("Unexpected error occurred while fetching user suggestions: {}", e.getMessage());
			throw new UserException("An unexpected error occurred while accessing user suggestions.");
		}
	}

	@GetMapping("/getUserSuggestion")
	public Object getuserSuggestion(Model model, HttpSession session) {
		return userSuggestion(model, session);
	}

	@GetMapping("/searchUser")
	public Object searchUser(Model model, String searchData, HttpSession session) {
		try {
			String username = (String) session.getAttribute("username");
			System.out.println("searchdata" + searchData);
			Map<String, Boolean> searchUsernameStatus = userFollowService.searchUsernameStatusFilter(username,
					searchData, userService);
			model.addAttribute("searchUsernameStatus", searchUsernameStatus);
			model.addAttribute("searchData", searchData);
			return new ModelAndView("SearchedUser");
		} catch (Exception e) {
			logger.error("Unexpected error occurred during user search: {}", e.getMessage());
			throw new UserException("An unexpected error occurred while searching for users.");
		}
	}

	@GetMapping("/fViewProfile")
	public Object fViewProfile(Model model, String fUsername, HttpSession session) {
		try {
			model = userService.fViewProfile(fUsername, model);
			return new ModelAndView("fViewProfile");
		} catch (Exception e) {
			logger.error("Unexpected error occurred while viewing profile of user {}: {}", fUsername, e.getMessage());
			throw new UserException("An unexpected error occurred while viewing the user's profile.");
		}
	}

	@GetMapping("/notifications")
	public Object notifications(Model model, HttpSession session) {
		try {
			String username = session.getAttribute("username").toString();
			model = userService.userNotification(username, model);
			return new ModelAndView("Notification");
		} catch (Exception e) {
			logger.error("Unexpected error occurred while fetching notifications: {}", e.getMessage());
			throw new UserException("An unexpected error occurred while accessing notifications.");
		}
	}

	@GetMapping("/RegisterPage")
	public Object RegisterPage() {
		return new ModelAndView("Register");
	}

	@GetMapping("/")
	public Object loginView() {
		return new ModelAndView("Login");
	}

	@GetMapping("login")
	public Object login() {
		return new ModelAndView("Login");
	}

	@GetMapping("/logout")
	public Object logout(HttpSession session) {
		session.invalidate();
		return new ModelAndView("Login");
	}

	@PostMapping("/register")
	public ModelAndView register(@ModelAttribute("user") User user, Model model) {
		try {
			new UserProfile(user.getUsername(), null, null, null, null);
			String msg = userService.registerUser(user);
			model.addAttribute("message", msg);
			return new ModelAndView("Login");
		} catch (Exception e) {
			logger.error("Error occurred during registration: {}", e.getMessage());

			throw new UserException("An unexpected error occurred during registration.");
		}
	}
}
