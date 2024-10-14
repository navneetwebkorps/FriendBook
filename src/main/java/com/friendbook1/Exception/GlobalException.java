package com.friendbook1.Exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalException {

	@ExceptionHandler(UserException.class)
	public ModelAndView handleUserException(UserException ex, Model model) {
		return createErrorModelAndView(ex.getMessage(), "User related error", HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(FollowException.class)
	public ModelAndView handleFollowException(FollowException ex, Model model) {
		return createErrorModelAndView(ex.getMessage(), "Follow request error", HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(PhotoLikeCommentException.class)
	public ModelAndView handlePhotoLikeCommentException(PhotoLikeCommentException ex, Model model) {
		return createErrorModelAndView(ex.getMessage(), "PhotoLikeCommentException request error",
				HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(PostException.class)
	public ModelAndView postException(PostException ex, Model model) {
		return createErrorModelAndView(ex.getMessage(), "Post request error", HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(ProfileException.class)
	public ModelAndView profileException(ProfileException ex, Model model) {
		return createErrorModelAndView(ex.getMessage(), "Profile request error", HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleGeneralException(Exception ex, Model model) {
		return createErrorModelAndView(ex.getMessage(), "An unexpected error occurred",
				HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	@ExceptionHandler(IOException.class)
	public ModelAndView handleIoException(IOException ex, Model model) {
		return createErrorModelAndView(ex.getMessage(), "An IO error occurred",
				HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	private ModelAndView createErrorModelAndView(String errorMessage, String errorDetails, int errorCode) {
		ModelAndView modelAndView = new ModelAndView("Error"); // Name of your error page
		modelAndView.addObject("errorMessage", errorMessage);
		modelAndView.addObject("errorDetails", errorDetails);
		modelAndView.addObject("errorCode", errorCode);
		modelAndView.addObject("errorTime", LocalDateTime.now());
		return modelAndView;
	}
}
