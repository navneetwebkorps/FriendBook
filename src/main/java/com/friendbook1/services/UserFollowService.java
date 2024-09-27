package com.friendbook1.services;

import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook1.entities.UserFollowing;
import com.friendbook1.repository.UserFollowingRepository;

@Service
public class UserFollowService {
    private static final Logger logger = LoggerFactory.getLogger(UserFollowService.class);

    @Autowired
    UserFollowingRepository userFollowingRepository;

    public void userFollowRequest(String userName, String followUsername) {
        try {
            UserFollowing userRequest = new UserFollowing();
            userRequest.setUserName(userName);
            userRequest.setFollowingUserName(followUsername);
            userRequest.setStatus(null);
            userFollowingRepository.save(userRequest);
            logger.info("Follow request saved: User {} -> Follow {}", userName, followUsername);
        } catch (Exception e) {
            logger.error("Error in follow request for user {} and follow {}: {}", userName, followUsername, e.getMessage());
        }
    }


    public void acceptORreject(String userName, String followUsername, Boolean status) {
        try {
            UserFollowing request = userFollowingRepository.findByUserNameAndFollowingUserName(followUsername, userName);
            if (request != null) {
                request.setStatus(status);
                userFollowingRepository.save(request);
                logger.info("Follow request updated: User {} -> Follow {} Status: {}", userName, followUsername, status);
            } else {
                logger.warn("No follow request found for User {} and Follow {}", userName, followUsername);
            }
        } catch (Exception e) {
            logger.error("Error updating follow request for User {} and Follow {}: {}", userName, followUsername, e.getMessage());
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

    /*
     * public void followRequest(String userName, String followerUserName) {
     * UserFollowing userFollow = userFollowingRepository.findById(userName)
     *         .orElseThrow(() -> new RuntimeException("Error in follow request because obj is null"));
     * Map<String, Boolean> followRequest = new HashMap<>();
     * followRequest.put(followerUserName, false);
     * userFollow.getFollowing().add(followRequest);
     * userFollowingRepository.save(userFollow);
     * }
     */

    /*
     * public long userFollowers(String userName) {
     *     UserFollowing userFollowers = userFollowingRepository.findById(userName)
     *         .orElseThrow(() -> new RuntimeException("Error in follow request because obj is null"));
     *     Map<String, Boolean> uf = (Map<String, Boolean>) userFollowers.getFollowing();
     *     long count = uf.entrySet().stream()
     *         .filter(entry -> entry.getValue().equals(true))
     *         .count();
     *     return count;
     * }
     */
}
