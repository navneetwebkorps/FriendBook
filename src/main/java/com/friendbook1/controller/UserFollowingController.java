package com.friendbook1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.friendbook1.repository.UserFollowingRepository;
import com.friendbook1.services.UserFollowService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserFollowingController {
	@Autowired
	private UserFollowingRepository userFollowingRepository;
	 @Autowired
	    private UserFollowService userFollowService;
	 @PostMapping("/userFollowRequest")
	public String UserFollowRequest(@RequestParam("username") String userName,@RequestParam("followerUsername") String followerUserName)
	{
		userFollowService.userFollowRequest(userName, followerUserName);
		 return "redirect:/userSuggestion";
	}
	 @PostMapping("/userFollowRequest1")
		public String UserFollowRequest1(@RequestParam("username") String userName,@RequestParam("followerUsername") String followerUserName,String searchData)
		{
			userFollowService.userFollowRequest(userName, followerUserName);
			return "redirect:/searchUser?searchData=" + searchData;
		}
	 @PostMapping("/userFollowRequest2")
		public String UserFollowRequest2(@RequestParam("username") String userName,@RequestParam("followerUsername") String followerUserName)
		{
			userFollowService.userFollowRequest(userName, followerUserName);
			return "redirect:/notifications";
		}
	 @PostMapping("/userFollowRequest3")
		public String UserFollowRequest3(HttpSession session,@RequestParam("followerUsername") String followerUserName)
		{
		 String userName=(String) session.getAttribute("username");
			userFollowingRepository.deleteByUserNameAndFollowingUserName(followerUserName, userName);
			return "redirect:/viewFollowersAndFollowing";
		}
	 @PostMapping("/userFollowRequest4")
		public String UserFollowRequest4(HttpSession session,@RequestParam("followerUsername") String followerUserName)
		{
		 String userName=(String) session.getAttribute("username");
			userFollowingRepository.deleteByUserNameAndFollowingUserName(userName, followerUserName);
			return "redirect:/viewFollowersAndFollowing";
		}
	 @PostMapping("/userFollowRequestAcceptorReject")
		public String UserFollowRequestAccept(@RequestParam("username") String userName,@RequestParam("followerUsername") String followerUserName,boolean status)
		{
			userFollowService.acceptORreject( userName, followerUserName,status);
			System.out.println("user"+userName+"folo"+followerUserName);
			 return "redirect:/notifications";
		}
	
	 @GetMapping("/userFollower")
	 public void userFollower(@RequestParam("username") String userName)
	 {
		 userFollowService.userFollowers(userName);
	 }
	 
	 @GetMapping("/userFollowing")
	 public void userFollowing(@RequestParam("username") String userName)
	 {
		 userFollowService.userFollowing(userName);
	 }
	@PutMapping("/acceptORreject")
	 public void acceptORrejectRequest(String userName,String followerUsername, Boolean status)
	 {
		 userFollowService.acceptORreject(userName, followerUsername, status);
	 }
	
	
	@GetMapping("/viewFollowersAndFollowing")
	public String viewFollowings(Model model, HttpSession session)
	{
		List<String> usernames=	 userFollowingRepository.findFollowersByUserName(session.getAttribute("username").toString());
		
		model.addAttribute("FollowersUsernameList", usernames);
		List<String> followersUsername=userFollowingRepository.findFollowingByUserName(session.getAttribute("username").toString());
		model.addAttribute("followingUsernameList", followersUsername);
		
		return "ViewUsers";
	}

}
