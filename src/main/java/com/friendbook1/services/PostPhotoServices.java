package com.friendbook1.services;

import com.friendbook1.entities.Comment;
import com.friendbook1.entities.PhotoLikeComment;
import com.friendbook1.entities.PostPhoto;
import com.friendbook1.repository.PhotoLikeCommentRepository;
import com.friendbook1.repository.PostPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PostPhotoServices {

    private static final Logger logger = Logger.getLogger(PostPhotoServices.class.getName());
@Autowired
private PhotoLikeCommentRepository photoLikeCommentRepository;
    @Autowired
    private PostPhotoRepository postPhotoRepository;

    public String savePostPhoto(String userName, byte[] photo) {
        logger.log(Level.INFO, "Attempting to save photo for user: {0}", userName);
        try {
            // Generate a unique random ID
            int randomId = generateUniqueRandomId();
            
            
            
            
            PhotoLikeComment PLC=new PhotoLikeComment();
            
            PLC.setRandomId(randomId);
            List<String> l=new ArrayList<>();
            PLC.setLiked(l);
            List<Comment> c = null;
            PLC.setComments(c);
            
            PLC.setUserName(userName);
            photoLikeCommentRepository.save(PLC);

           
            PostPhoto postPhoto = new PostPhoto(userName, photo, LocalDateTime.now(), randomId);

            
            PostPhoto savedPostPhoto = postPhotoRepository.save(postPhoto);
            
            

            logger.log(Level.INFO, "Photo successfully saved for user: {0}", userName);
            return "Post uploaded";

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving photo for user: " + userName, e);
            return "please retry";  
        }
    }


    private int generateUniqueRandomId() {
        Random random = new Random();
        int randomId;
        Optional<PostPhoto> existingPostPhoto;

        try {
            do {
                randomId = 100000 + random.nextInt(900000); 
                existingPostPhoto = postPhotoRepository.findByRandomId(randomId);
            } while (existingPostPhoto.isPresent());

            return randomId;

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error generating unique random ID", e);
            return -1; 
        }
    }

    
    public List<PostPhoto> getPhotosByUser(String userName) {
        logger.log(Level.INFO, "Fetching photos for user: {0}", userName);
        try {
            return postPhotoRepository.findAllByUserNameOrderByUploadTimeDesc(userName);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching photos for user: " + userName, e);
            return null; 
        }
    }
}
