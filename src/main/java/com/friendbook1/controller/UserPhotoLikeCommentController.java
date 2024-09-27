package com.friendbook1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.friendbook1.entities.Comment;
import com.friendbook1.entities.PhotoLikeComment;
import com.friendbook1.repository.PhotoLikeCommentRepository;
import com.friendbook1.services.PhotoLikeCommentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/photos")
public class UserPhotoLikeCommentController {

    private static final Logger logger = LoggerFactory.getLogger(UserPhotoLikeCommentController.class);

    @Autowired
    private PhotoLikeCommentService photoLikeCommentService;
    
    @Autowired 
    private PhotoLikeCommentRepository photoLikeCommentRepository;

    @PostMapping("sendLike")
    public String likePhoto(@RequestParam("id") int id, HttpSession session) {
        String username = session.getAttribute("username").toString();
        logger.info("User '{}' is liking photo with ID {}", username, id);
        
        photoLikeCommentService.likePhoto(id, username);
        return "redirect:/home";
    }
    
    @PostMapping("sendLike1")
    public String likePhoto1(@RequestParam("id") int id, HttpSession session) {
        String username = session.getAttribute("username").toString();
        logger.info("User '{}' is liking photo with ID {}", username, id);
        
        photoLikeCommentService.likePhoto(id, username);
        return "redirect:/profile";
    }

    @PostMapping("/sendComment")
    public String commentOnPhoto(@RequestParam("id") int id,
                                 HttpSession session,
                                 @RequestParam("comment") String comment) {
        String username = session.getAttribute("username").toString();
        logger.info("User '{}' is commenting on photo with ID {}. Comment: {}", username, id, comment);
        
        photoLikeCommentService.commentOnPhoto(id, username, comment);
        return "redirect:/home";
    }
    @PostMapping("/sendComment1")
    public String commentOnPhoto1(@RequestParam("id") int id,
                                 HttpSession session,
                                 @RequestParam("comment") String comment) {
        String username = session.getAttribute("username").toString();
        logger.info("User '{}' is commenting on photo with ID {}. Comment: {}", username, id, comment);
        
        photoLikeCommentService.commentOnPhoto(id, username, comment);
        return "redirect:/profile";
    }

    @GetMapping("/getLikeComment")
    public String getLikeComment(Model model, @RequestParam("id") int id, HttpSession session) {
        String username = (String) session.getAttribute("username");
        logger.info("User '{}' is retrieving likes and comments for photo with ID {}", username, id);
        
        Optional<PhotoLikeComment> pLC = photoLikeCommentRepository.findById(id);
        
        if (pLC.isPresent()) {
            List<String> likeUsernames = pLC.get().getLiked();
            List<Comment> commentsList = pLC.get().getComments();
            Map<String, String> commentUsernameMap = new HashMap<>();
            
            for (Comment comment : commentsList) {
                commentUsernameMap.put(comment.getUserName(), comment.getComment());
            }
            
            model.addAttribute("likeUsernames", likeUsernames);
            model.addAttribute("commentUsernameMap", commentUsernameMap);
        } else {
            logger.warn("No likes or comments found for photo with ID {}", id);
        }
        
        return "PostLikeComment";
    }
}
