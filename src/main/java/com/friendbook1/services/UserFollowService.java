package com.friendbook1.services;

import java.util.Map;

public interface UserFollowService {

	String userFollowRequest(String userName, String followUsername);

	String acceptORreject(String userName, String followUsername, Boolean status);

	long userFollowing(String userName);

	long userFollowers(String userName);

	Map<String, Boolean> searchUsernameStatusFilter(String username, String searchData, UserService userService);
}
