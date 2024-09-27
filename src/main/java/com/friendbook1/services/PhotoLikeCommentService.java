package com.friendbook1.services;

import com.friendbook1.entities.Comment;
import com.friendbook1.entities.PhotoLikeComment;
import com.friendbook1.repository.PhotoLikeCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PhotoLikeCommentService {

    private static final Logger logger = Logger.getLogger(PhotoLikeCommentService.class.getName());

    @Autowired
    private PhotoLikeCommentRepository photoLikeCommentRepository;

    // Logic to like a photo with logging
    public void likePhoto(int id, String userName) {
        logger.log(Level.INFO, "User {0} is liking photo with ID: {1}", new Object[]{userName, id});
        try {
            Optional<PhotoLikeComment> photoLikeCommentOpt = photoLikeCommentRepository.findById(id);
            List<String> likedOrNot=photoLikeCommentOpt.get().getLiked();
            if(likedOrNot.contains(userName))
            {
            	likedOrNot.remove(userName);
            	photoLikeCommentOpt.get().setLiked(likedOrNot);
            	  PhotoLikeComment photoLikeComment = photoLikeCommentOpt.get();
            	
            	 photoLikeCommentRepository.save(photoLikeComment);
            	
            }
            else   if (photoLikeCommentOpt.isPresent()) {
                PhotoLikeComment photoLikeComment = photoLikeCommentOpt.get();
                photoLikeComment.getLiked().add(userName);
                photoLikeCommentRepository.save(photoLikeComment);
                logger.log(Level.INFO, "Photo liked successfully by user: {0}", userName);
            } else {
            	
            	
              //  logger.log(Level.WARNING, "Photo not found with ID: {0}", id);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error liking photo with ID: " + id + " by user: " + userName, e);
        }
    }

    // Logic to comment on a photo with logging
    public void commentOnPhoto(int id, String userName, String comment) {
        logger.log(Level.INFO, "User is commenting on photo with ID");
        try {
            Optional<PhotoLikeComment> photoLikeCommentOpt = photoLikeCommentRepository.findById(id);
            if (photoLikeCommentOpt.isPresent()) {
                PhotoLikeComment photoLikeComment = photoLikeCommentOpt.get();

                // Create a comment map or Comment entity and add it to the photo's comments list
                Comment cm = new Comment();
                cm.setUserName(userName);
                cm.setComment(comment);
                photoLikeComment.getComments().add(cm);

                photoLikeCommentRepository.save(photoLikeComment);
                logger.log(Level.INFO, "Comment added successfully by user: {0} to photo with ID");
            } else {
                logger.log(Level.WARNING, "Photo not found with ID", id);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error adding comment to photo with ID: " + id + " by user: " + userName, e);
        }
    }
    
    
}
