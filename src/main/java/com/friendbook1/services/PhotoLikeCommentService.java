package com.friendbook1.services;

import org.springframework.ui.Model;

public interface PhotoLikeCommentService {

	String likePhoto(int id, String userName);

	String commentOnPhoto(int id, String userName, String comment);

	Model getlikeComments(Model model, String username, int id);
}
