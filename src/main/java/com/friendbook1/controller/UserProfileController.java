package com.friendbook1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.friendbook1.entities.UserProfile;
import com.friendbook1.repository.UserProfileRepository;
import com.friendbook1.services.impl.UserProfileServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserProfileController {

	private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

	@Autowired
	UserProfileRepository userProfileRepository;

	@Autowired
	private UserProfileServiceImpl userProfileService;

	@GetMapping("/updateProfileView")
	public Object updateProfileView(HttpSession session, Model model) {
		try {
			String username = session.getAttribute("username").toString();
			logger.info("Fetching profile view for user: {}", username);

			UserProfile userProfile = userProfileRepository.findByUsername(username);
			if (userProfile == null) {
				logger.warn("No profile found for user: {}", username);
				return new ModelAndView("redirect:/");
			}

			model.addAttribute("userProfile", userProfile);
			logger.info("Profile view fetched for user: {}", username);
			return new ModelAndView("UpdateProfile");
		} catch (Exception e) {
			logger.error("Error fetching profile view: {}", e.getMessage());
			return new ModelAndView("error");
		}
	}

	@GetMapping("/updateProfile")
	@ResponseBody
	public ResponseEntity<?> updateProfile1(HttpSession session, Model model) {
		try {
			String username = session.getAttribute("username").toString();
			logger.info("Fetching profile for user: {}", username);

			UserProfile userProfile = userProfileRepository.findByUsername(username);
			if (userProfile == null) {
				logger.warn("No profile found for user: {}", username);
				return new ResponseEntity<>("Profile not found", HttpStatus.NOT_FOUND);
			}

			model.addAttribute("userProfile", userProfile);
			logger.info("Profile fetched successfully for user: {}", username);
			return new ResponseEntity<>(userProfile, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching profile: {}", e.getMessage());
			return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/updateProfile")
	public Object updateProfile(@RequestParam MultipartFile profilePhoto, @RequestParam String favSongs,
			@RequestParam String favBooks, @RequestParam String favPlaces, HttpSession session, Model model) {

		String username = (String) session.getAttribute("username");
		if (username == null || username.isEmpty()) {
			logger.warn("User not logged in.");
			model.addAttribute("error", "User not logged in.");
			return "redirect:/";
		}

		try {
			logger.info("Updating profile for user: {}", username);
			model = userProfileService.UserUpdateProfile(username, model, favBooks, favSongs, favPlaces, profilePhoto);
			model.addAttribute("message", "Profile updated successfully!");
			logger.info("Profile updated successfully for user: {}", username);
			return new ModelAndView("redirect:/profile");
		} catch (Exception e) {
			logger.error("Error updating profile for user {}: {}", username, e.getMessage());
			model.addAttribute("error", "An error occurred while updating profile.");
			return new ModelAndView("UpdateProfile");
		}
	}
}
