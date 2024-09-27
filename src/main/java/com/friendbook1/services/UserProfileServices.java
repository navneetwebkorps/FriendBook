package com.friendbook1.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook1.entities.UserProfile;
import com.friendbook1.repository.UserProfileRepository;

@Service
public class UserProfileServices {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileServices.class);

    @Autowired
    public UserProfileRepository userProfileRepository;

    public void updateUserProfile(UserProfile userProfile) {
        try {
            UserProfile updateUser = userProfileRepository.findByUsername(userProfile.getUsername());
            if (updateUser != null) {
                updateUser.setFavBooks(userProfile.getFavBooks());
                updateUser.setFavPlaces(userProfile.getFavPlaces());
                updateUser.setFavSongs(userProfile.getFavSongs());
                updateUser.setProfilePhoto(userProfile.getProfilePhoto());
                userProfileRepository.save(updateUser);
                logger.info("User profile updated for username: {}", userProfile.getUsername());
            } else {
                logger.warn("User profile not found for username: {}", userProfile.getUsername());
            }
        } catch (Exception e) {
            logger.error("Error updating user profile for username {}: {}", userProfile.getUsername(), e.getMessage());
        }
    }

    public void updateProfilePhoto(String username, byte[] profilePhoto) {
        try {
            UserProfile userProfile = userProfileRepository.findByUsername(username);
            if (userProfile != null) {
                userProfile.setProfilePhoto(profilePhoto);
                userProfileRepository.save(userProfile);
                logger.info("Profile photo updated for username: {}", username);
            } else {
                logger.warn("User profile not found for username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error updating profile photo for username {}: {}", username, e.getMessage());
        }
    }

    public void updateFavSongs(String username, List<String> favSongs) {
        try {
            UserProfile userProfile = userProfileRepository.findByUsername(username);
            if (userProfile != null) {
                userProfile.setFavSongs(favSongs);
                userProfileRepository.save(userProfile);
                logger.info("Favorite songs updated for username: {}", username);
            } else {
                logger.warn("User profile not found for username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error updating favorite songs for username {}: {}", username, e.getMessage());
        }
    }

    public void updateFavBooks(String username, List<String> favBooks) {
        try {
            UserProfile userProfile = userProfileRepository.findByUsername(username);
            if (userProfile != null) {
                userProfile.setFavBooks(favBooks);
                userProfileRepository.save(userProfile);
                logger.info("Favorite books updated for username: {}", username);
            } else {
                logger.warn("User profile not found for username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error updating favorite books for username {}: {}", username, e.getMessage());
        }
    }

    public void updateFavPlaces(String username, List<String> favPlaces) {
        try {
            UserProfile userProfile = userProfileRepository.findByUsername(username);
            if (userProfile != null) {
                userProfile.setFavPlaces(favPlaces);
                userProfileRepository.save(userProfile);
                logger.info("Favorite places updated for username: {}", username);
            } else {
                logger.warn("User profile not found for username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error updating favorite places for username {}: {}", username, e.getMessage());
        }
    }

    public UserProfile userProfile(String userName) {
        try {
            UserProfile user = userProfileRepository.findByUsername(userName);
            if (user != null) {
                logger.info("User profile fetched for username: {}", userName);
                return user;
            } else {
                logger.warn("User profile not found for username: {}", userName);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error fetching user profile for username {}: {}", userName, e.getMessage());
            return null;
        }
    }
}
