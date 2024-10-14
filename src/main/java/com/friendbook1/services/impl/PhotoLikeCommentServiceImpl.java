package com.friendbook1.services.impl;

import com.friendbook1.entities.Comment;
import com.friendbook1.entities.PhotoLikeComment;
import com.friendbook1.repository.PhotoLikeCommentRepository;
import com.friendbook1.services.PhotoLikeCommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PhotoLikeCommentServiceImpl implements PhotoLikeCommentService {

	private static final Logger logger = Logger.getLogger(PhotoLikeCommentServiceImpl.class.getName());

	@Autowired
	private PhotoLikeCommentRepository photoLikeCommentRepository;

	public String likePhoto(int id, String userName) {
		logger.log(Level.INFO, "User {0} is liking photo with ID: {1}", new Object[] { userName, id });

		try {
			Optional<PhotoLikeComment> photoLikeCommentOpt = photoLikeCommentRepository.findById(id);

			if (photoLikeCommentOpt.isPresent()) {
				PhotoLikeComment photoLikeComment = photoLikeCommentOpt.get();
				List<String> likedOrNot = photoLikeComment.getLiked();

				if (likedOrNot.contains(userName)) {

					likedOrNot.remove(userName);
					photoLikeComment.setLiked(likedOrNot);
					photoLikeCommentRepository.save(photoLikeComment);
					logger.log(Level.INFO, "Photo unliked by user: {0}", userName);

					return "User " + userName + " unliked photo with ID: " + id + " successfully";
				} else {
					// Like the photo
					likedOrNot.add(userName);
					photoLikeComment.setLiked(likedOrNot);
					photoLikeCommentRepository.save(photoLikeComment);
					logger.log(Level.INFO, "Photo liked successfully by user: {0}", userName);

					return "User " + userName + " liked photo with ID: " + id + " successfully";
				}
			} else {
				logger.log(Level.WARNING, "Photo not found with ID: {0}", id);
				return "Error: Photo with ID " + id + " not found";
			}

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error liking photo with ID: " + id + " by user: " + userName, e);
			return "Error: Unable to like photo with ID " + id + " by user " + userName;
		}
	}

	public String commentOnPhoto(int id, String userName, String comment) {
		logger.log(Level.INFO, "User is commenting on photo with ID");
		try {
			Optional<PhotoLikeComment> photoLikeCommentOpt = photoLikeCommentRepository.findById(id);
			if (photoLikeCommentOpt.isPresent()) {
				PhotoLikeComment photoLikeComment = photoLikeCommentOpt.get();

				Comment cm = new Comment();
				cm.setUserName(userName);
				cm.setComment(comment);

				photoLikeComment.getComments().add(cm);

				photoLikeCommentRepository.save(photoLikeComment);
				logger.log(Level.INFO, "Comment added successfully by user: {0} to photo with ID");
				return "successfully comment added on id=${id} by user= ${userNmae} and comment is ${comment}";
			} else {
				logger.log(Level.WARNING, "Photo not found with ID", id);
				return "Comment failed to save in database";

			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding comment to photo with ID: " + id + " by user: " + userName, e);
			return "Comment failed to save in database";
		}
	}

	@Override
	public Model getlikeComments(Model model, String username, int id) {

		logger.info("User '{}' is retrieving likes and comments for photo with ID {}");

		Optional<PhotoLikeComment> pLC = photoLikeCommentRepository.findById(id);

		if (pLC.isPresent()) {
			List<String> likeUsernames = pLC.get().getLiked();
			List<Comment> commentsList = pLC.get().getComments();
			Map<String, String> commentUsernameMap = new HashMap<>();

			for (Comment comment : commentsList) {
				commentUsernameMap.put(comment.getUserName(), comment.getComment());
			}
			System.out.println("com" + commentsList + "lik" + likeUsernames + "map" + commentUsernameMap);

			model.addAttribute("likeUsernames", likeUsernames);
			model.addAttribute("commentUsernameMap", commentsList);
		} else {
			logger.info("No likes or comments found for photo with ID {}");
		}
		return model;
	}

}
