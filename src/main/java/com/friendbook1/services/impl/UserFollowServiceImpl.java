package com.friendbook1.services.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook1.entities.User;
import com.friendbook1.entities.UserFollowing;
import com.friendbook1.repository.UserFollowingRepository;
import com.friendbook1.services.UserFollowService;
import com.friendbook1.services.UserService;

@Service
public class UserFollowServiceImpl implements UserFollowService {
	private static final Logger logger = LoggerFactory.getLogger(UserFollowServiceImpl.class);

	@Autowired
	UserFollowingRepository userFollowingRepository;

	public String userFollowRequest(String userName, String followUsername) {
		try {
			UserFollowing userRequest = new UserFollowing();
			userRequest.setUserName(userName);
			userRequest.setFollowingUserName(followUsername);
			userRequest.setStatus(null);
			userFollowingRepository.save(userRequest);
			logger.info("Follow request saved: User {} -> Follow {}", userName, followUsername);
			return "Request send succeessfully user=" + userName + "and follwer=" + followUsername + " ";
		} catch (Exception e) {
			logger.error("Error in follow request for user {} and follow {}: {}", userName, followUsername,
					e.getMessage());
			return "failed for operation";
		}
	}

	public String acceptORreject(String userName, String followUsername, Boolean status) {
		try {

			UserFollowing request = userFollowingRepository.findByUserNameAndFollowingUserName(followUsername,
					userName);

			if (request != null) {

				request.setStatus(status);
				userFollowingRepository.save(request);

				logger.info("Follow request updated: User {} -> Follow {} Status: {}", userName, followUsername,
						status);
				return "Follow request from " + followUsername + " to " + userName + " has been "
						+ (status ? "accepted" : "rejected") + " successfully.";
			} else {
				logger.warn("No follow request found for User {} and Follow {}", userName, followUsername);
				return "Error: No follow request found for " + followUsername + " to " + userName;
			}

		} catch (Exception e) {
			logger.error("Error updating follow request for User {} and Follow {}: {}", userName, followUsername,
					e.getMessage());
			return "Error: Unable to update follow request for " + followUsername + " to " + userName;
		}
	}

	public long userFollowing(String userName) {
		try {
			long count = userFollowingRepository.countByUserNameAndStatus(userName, true);
			logger.info("User {} is following {} users", userName, count);
			return count;
		} catch (Exception e) {
			logger.error("Error fetching following count for user {}: {}", userName, e.getMessage());
			return 0;
		}
	}

	public long userFollowers(String userName) {
		try {
			long count = userFollowingRepository.countByFollowingUserNameAndStatus(userName, true);
			logger.info("User {} has {} followers", userName, count);
			return count;
		} catch (Exception e) {
			logger.error("Error fetching followers count for user {}: {}", userName, e.getMessage());
			return 0;
		}
	}

	@Override
	public Map<String, Boolean> searchUsernameStatusFilter(String username, String searchData,
			UserService userService) {
		List<User> searchUsername = userService.searchUser(searchData);
		if (searchUsername == null || searchUsername.isEmpty()) {
			System.out.println("No users found for the search data.");
			return Collections.emptyMap();
		} else {

		}
		Map<String, Boolean> searchUsernameStatus = new HashMap<>();
		List<String> followerUsername = userFollowingRepository.findFollowingUserNameByUserName1(username);
		searchUsername.removeIf(u -> u.getUsername().equals(username));
		for (User u : searchUsername) {

			if (followerUsername.contains(u.getUsername())) {
				searchUsernameStatus.put(u.getUsername(), true);
			} else {
				searchUsernameStatus.put(u.getUsername(), false);
			}

		}
		return searchUsernameStatus;
	}

	/*
	 * public void followRequest(String userName, String followerUserName) {
	 * UserFollowing userFollow = userFollowingRepository.findById(userName)
	 * .orElseThrow(() -> new
	 * RuntimeException("Error in follow request because obj is null")); Map<String,
	 * Boolean> followRequest = new HashMap<>(); followRequest.put(followerUserName,
	 * false); userFollow.getFollowing().add(followRequest);
	 * userFollowingRepository.save(userFollow); }
	 */

	/*
	 * public long userFollowers(String userName) { UserFollowing userFollowers =
	 * userFollowingRepository.findById(userName) .orElseThrow(() -> new
	 * RuntimeException("Error in follow request because obj is null")); Map<String,
	 * Boolean> uf = (Map<String, Boolean>) userFollowers.getFollowing(); long count
	 * = uf.entrySet().stream() .filter(entry -> entry.getValue().equals(true))
	 * .count(); return count; }
	 */
}
