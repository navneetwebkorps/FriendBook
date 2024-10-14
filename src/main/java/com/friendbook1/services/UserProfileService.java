package com.friendbook1.services;

import com.friendbook1.entities.PostPhoto;
import com.friendbook1.entities.UserProfile;
import com.friendbook1.repository.PhotoLikeCommentRepository;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
	void updateUserProfile(UserProfile userProfile);

	UserProfile userProfile(String userName);

	Model UserUpdateProfile(String username, Model model, String favBooks, String favSongs, String favPlaces,
			MultipartFile profilePhoto);

	List<PostPhoto> userPhotosFilter(List<PostPhoto> postPhotos, String username,
			PhotoLikeCommentRepository photoLikeCommentRepository, UserProfile userProfile);

	Map<Integer, String> fUserPhotosFilter(List<PostPhoto> postPhoto, UserProfile userProfile);
}
