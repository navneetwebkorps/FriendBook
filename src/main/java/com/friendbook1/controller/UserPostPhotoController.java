package com.friendbook1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.friendbook1.entities.PostPhoto;
import com.friendbook1.services.impl.PostPhotoServicesImpl;

import java.io.IOException;
import java.util.List;

@Controller
public class UserPostPhotoController {

	private static final Logger logger = LoggerFactory.getLogger(UserPostPhotoController.class);

	@Autowired
	private PostPhotoServicesImpl postPhotoService;

	@PostMapping("/upload")
	@ResponseBody
	public ResponseEntity<String> uploadPhoto(@RequestParam("username") String userName,
			@RequestParam("photo") MultipartFile photo, RedirectAttributes redirectAttributes) {
		try {
			logger.info("Uploading photo for user: {}", userName);
			byte[] photoBytes = photo.getBytes();
			String msg = postPhotoService.savePostPhoto(userName, photoBytes);
			redirectAttributes.addFlashAttribute("message", msg);
			logger.info("Photo uploaded successfully for user: {}", userName);
			return new ResponseEntity<>("Photo uploaded successfully", HttpStatus.OK);
		} catch (IOException e) {
			logger.error("Error uploading photo for user {}: {}", userName, e.getMessage());
			return new ResponseEntity<>("Failed to upload photo", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Unexpected error during photo upload for user {}: {}", userName, e.getMessage());
			return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{username}/latest")
	@ResponseBody
	public ResponseEntity<List<PostPhoto>> getLatestPhotos(@PathVariable String userName) {
		try {
			logger.info("Fetching latest photos for user: {}", userName);
			List<PostPhoto> photos = postPhotoService.getPhotosByUser(userName);
			if (photos.isEmpty()) {
				logger.info("No photos found for user: {}", userName);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			logger.info("Photos fetched successfully for user: {}", userName);
			return new ResponseEntity<>(photos, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error fetching photos for user {}: {}", userName, e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
