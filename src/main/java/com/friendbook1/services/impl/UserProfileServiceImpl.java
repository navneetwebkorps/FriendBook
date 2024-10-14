package com.friendbook1.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.friendbook1.entities.PhotoLikeComment;
import com.friendbook1.entities.PostPhoto;
import com.friendbook1.entities.UserProfile;
import com.friendbook1.repository.PhotoLikeCommentRepository;
import com.friendbook1.repository.UserProfileRepository;
import com.friendbook1.services.PostPhotoServices;
import com.friendbook1.services.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

	private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

	@Autowired
	public UserProfileRepository userProfileRepository;
	@Autowired
	public PostPhotoServices postPhotoService;

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

	@Override
	public List<PostPhoto> userPhotosFilter(List<PostPhoto> postPhotos, String username,
			PhotoLikeCommentRepository photoLikeCommentRepository, UserProfile userProfile) {

		for (PostPhoto postPhoto : postPhotos) {

			String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(postPhoto.getPhoto());
			postPhoto.setBase64Photo(base64Photo);

			Optional<PhotoLikeComment> plc = photoLikeCommentRepository.findById(postPhoto.getRandomId());
			if (!plc.isEmpty()) {
				List<String> listOfLikes = plc.get().getLiked();
				if (listOfLikes.contains(username)) {
					postPhoto.setAlreadyLiked(true);
				} else {
					postPhoto.setAlreadyLiked(false);
				}
			}
		}

		if (userProfile.getProfilePhoto() != null) {
			String base64ProfilePhoto = "data:image/jpeg;base64,"
					+ Base64.getEncoder().encodeToString(userProfile.getProfilePhoto());
			userProfile.setBase64Photo(base64ProfilePhoto);

		}
		return postPhotos;
	}

	@Override
	public Map<Integer, String> fUserPhotosFilter(List<PostPhoto> postPhoto, UserProfile userProfile) {
		Map<Integer, String> photoMap = postPhoto.stream().collect(Collectors.toMap(PostPhoto::getRandomId,

				photo -> "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo.getPhoto())

		));
		if (userProfile.getProfilePhoto() != null) {
			String base64ProfilePhoto = "data:image/jpeg;base64,"
					+ Base64.getEncoder().encodeToString(userProfile.getProfilePhoto());
			userProfile.setBase64Photo(base64ProfilePhoto);
		}
		return photoMap;
	}

	@Override
	public Model UserUpdateProfile(String username, Model model, String favBooks, String favSongs, String favPlaces,
			MultipartFile profilePhoto) {
		UserProfile userProfile = userProfileRepository.findByUsername(username);

		if (!profilePhoto.isEmpty()) {
			try {
				userProfile.setProfilePhoto(profilePhoto.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("error", "Failed to upload profile photo.");
			}
		}

		List<String> songs = new ArrayList<>();
		List<String> books = new ArrayList<>();
		List<String> places = new ArrayList<>();
		System.out.println("jds j" + favBooks + favPlaces + favSongs);
		if (!favSongs.isEmpty())
			songs.addAll(Arrays.asList(favSongs.split(",")));
		if (!favBooks.isEmpty())
			books.addAll(Arrays.asList(favBooks.split(",")));
		if (!favPlaces.isEmpty())
			places.addAll(Arrays.asList(favPlaces.split(",")));
		System.out.println("songs " + songs + "books" + books + "places" + places);
		userProfile.setFavSongs(songs);
		userProfile.setFavBooks(books);
		userProfile.setFavPlaces(places);

		userProfileRepository.save(userProfile);

		return model;
	}

	/*
	 * public void updateProfilePhoto(String username, byte[] profilePhoto) { try {
	 * UserProfile userProfile = userProfileRepository.findByUsername(username); if
	 * (userProfile != null) { userProfile.setProfilePhoto(profilePhoto);
	 * userProfileRepository.save(userProfile);
	 * logger.info("Profile photo updated for username: {}", username); } else {
	 * logger.warn("User profile not found for username: {}", username); } } catch
	 * (Exception e) {
	 * logger.error("Error updating profile photo for username {}: {}", username,
	 * e.getMessage()); } }
	 * 
	 * public void updateFavSongs(String username, List<String> favSongs) { try {
	 * UserProfile userProfile = userProfileRepository.findByUsername(username); if
	 * (userProfile != null) { userProfile.setFavSongs(favSongs);
	 * userProfileRepository.save(userProfile);
	 * logger.info("Favorite songs updated for username: {}", username); } else {
	 * logger.warn("User profile not found for username: {}", username); } } catch
	 * (Exception e) {
	 * logger.error("Error updating favorite songs for username {}: {}", username,
	 * e.getMessage()); } }
	 * 
	 * public void updateFavBooks(String username, List<String> favBooks) { try {
	 * UserProfile userProfile = userProfileRepository.findByUsername(username); if
	 * (userProfile != null) { userProfile.setFavBooks(favBooks);
	 * userProfileRepository.save(userProfile);
	 * logger.info("Favorite books updated for username: {}", username); } else {
	 * logger.warn("User profile not found for username: {}", username); } } catch
	 * (Exception e) {
	 * logger.error("Error updating favorite books for username {}: {}", username,
	 * e.getMessage()); } }
	 * 
	 * public void updateFavPlaces(String username, List<String> favPlaces) { try {
	 * UserProfile userProfile = userProfileRepository.findByUsername(username); if
	 * (userProfile != null) { userProfile.setFavPlaces(favPlaces);
	 * userProfileRepository.save(userProfile);
	 * logger.info("Favorite places updated for username: {}", username); } else {
	 * logger.warn("User profile not found for username: {}", username); } } catch
	 * (Exception e) {
	 * logger.error("Error updating favorite places for username {}: {}", username,
	 * e.getMessage()); } }
	 */

}
