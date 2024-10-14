package com.friendbook1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.friendbook1.services.impl.PhotoLikeCommentServiceImpl;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/photos")
public class UserPhotoLikeCommentController {

	private static final Logger logger = LoggerFactory.getLogger(UserPhotoLikeCommentController.class);

	@Autowired
	private PhotoLikeCommentServiceImpl photoLikeCommentService;

	@PostMapping("sendLike")
	@ResponseBody
	public ResponseEntity<String> likePhoto(@RequestParam("id") int id, HttpSession session) {
		String username = session.getAttribute("username").toString();
		logger.info("User '{}' is liking photo with ID {}", username, id);

		photoLikeCommentService.likePhoto(id, username);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("sendLike1")
	@ResponseBody
	public ResponseEntity<String> likePhoto1(@RequestParam("id") int id, HttpSession session) {
		String username = session.getAttribute("username").toString();
		logger.info("User '{}' is liking photo with ID {}", username, id);

		photoLikeCommentService.likePhoto(id, username);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/sendComment")
	@ResponseBody
	public ResponseEntity<String> commentOnPhoto(@RequestParam("id") int id, HttpSession session,
			@RequestParam("comment") String comment) {
		String username = session.getAttribute("username").toString();
		logger.info("User '{}' is commenting on photo with ID {}. Comment: {}", username, id, comment);

		photoLikeCommentService.commentOnPhoto(id, username, comment);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/sendComment1")
	@ResponseBody
	public ResponseEntity<String> commentOnPhoto1(@RequestParam("id") int id, HttpSession session,
			@RequestParam("comment") String comment) {
		logger.info("User '{}' is commenting on photo with ID {}. Comment: {}",
				session.getAttribute("username").toString(), id, comment);

		photoLikeCommentService.commentOnPhoto(id, session.getAttribute("username").toString(), comment);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/getLikeComment")
	@ResponseBody
	public ModelAndView getLikeComment(Model model, @RequestParam("id") int id, HttpSession session) {
		model = photoLikeCommentService.getlikeComments(model, session.getAttribute("username").toString(), id);
		return new ModelAndView("PostLikeComment");
	}
}
