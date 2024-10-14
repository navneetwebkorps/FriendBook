package com.friendbook1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.friendbook1.Exception.UserException;
import com.friendbook1.repository.UserFollowingRepository;
import com.friendbook1.services.impl.UserFollowServiceImpl;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserFollowingController {

	private static final Logger logger = LoggerFactory.getLogger(UserFollowingController.class);

	@Autowired
	private UserFollowingRepository userFollowingRepository;

	@Autowired
	private UserFollowServiceImpl userFollowService;

	@PostMapping("/userFollowRequest")
	public ResponseEntity<String> userFollowRequest(@RequestParam("username") String userName,
			@RequestParam("followerUsername") String followerUserName) {
		try {
			logger.info("Starting follow request for user: {}, follower: {}", userName, followerUserName);
			String response = userFollowService.userFollowRequest(userName, followerUserName);
			logger.info("Follow request processed successfully.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error while processing follow request for user: {}, follower: {}. Error: {}", userName,
					followerUserName, e.getMessage(), e);
			throw new UserException("An error occurred while processing the follow request.");
		}
	}

	@PostMapping("/userFollowRequestInSearch")
	public ResponseEntity<String> userFollowRequestInSearch(@RequestParam("username") String userName,
			@RequestParam("followerUsername") String followerUserName, String searchData) {
		logger.info("Processing follow request from search for user: {}, follower: {}", userName, followerUserName);
		userFollowService.userFollowRequest(userName, followerUserName);
		return ResponseEntity.ok("Follow request sent successfully.");
	}

	@PostMapping("/userFollowRequestRemove")
	public ResponseEntity<String> userFollowRequestRemove(HttpSession session,
			@RequestParam("followerUsername") String followerUserName) {
		String userName = (String) session.getAttribute("username");
		logger.info("Removing follow request: user {}, follower: {}", userName, followerUserName);
		userFollowingRepository.deleteByUserNameAndFollowingUserName(followerUserName, userName);
		return ResponseEntity.ok("Follow request removed successfully.");
	}

	@PostMapping("/userUnfollow")
	public ResponseEntity<String> userUnfollow(HttpSession session,
			@RequestParam("followerUsername") String followerUserName) {
		String userName = (String) session.getAttribute("username");
		logger.info("User {} unfollowing {}", userName, followerUserName);
		userFollowingRepository.deleteByUserNameAndFollowingUserName(userName, followerUserName);
		return ResponseEntity.ok("User unfollowed successfully.");
	}

	@PostMapping("/userFollowRequestAcceptorReject")
	public ResponseEntity<String> userFollowRequestAcceptOrReject(@RequestParam("username") String userName,
			@RequestParam("followerUsername") String followerUserName, boolean status) {
		logger.info("Processing follow request accept/reject: user {}, follower: {}, status: {}", userName,
				followerUserName, status);
		userFollowService.acceptORreject(userName, followerUserName, status);
		return ResponseEntity.ok("Follow request processed successfully.");
	}

	@GetMapping(path = "/viewFollowersAndFollowing", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.TEXT_HTML_VALUE })
	@ResponseBody
	public Object viewFollowings(Model model, HttpSession session,
			@RequestHeader(value = "Accept", defaultValue = "text/html") String acceptHeader) {
		String username = session.getAttribute("username").toString();
		logger.info("Fetching followers and followings for user: {}", username);

		List<String> followers = userFollowingRepository.findFollowersByUserName(username);
		List<String> followings = userFollowingRepository.findFollowingByUserName(username);

		if (acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
			Map<String, List<String>> response = new HashMap<>();
			response.put("Followers", followers);
			response.put("Following", followings);
			logger.info("Returning followers and followings in JSON format for user: {}", username);
			return ResponseEntity.ok(response);
		} else {
			model.addAttribute("FollowersUsernameList", followers);
			model.addAttribute("followingUsernameList", followings);
			logger.info("Returning followers and followings in HTML format for user: {}", username);
			return new ModelAndView("ViewUsers");
		}
	}
}
