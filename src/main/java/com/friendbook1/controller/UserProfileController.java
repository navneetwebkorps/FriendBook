package com.friendbook1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.friendbook1.entities.UserProfile;
import com.friendbook1.services.UserProfileServices;
import com.friendbook1.services.UserServices;

@Controller
public class UserProfileController {

    private static final Model Model = null;
	@Autowired
    private UserProfileServices userProfileService;
    
    @PostMapping("/updateUserProfile")
    public void updateUserProfile(UserProfile userProfile)
    {
    	userProfileService.updateUserProfile(userProfile);
    }
    
    @GetMapping("/userProfile")
    public void userProfile( String userName)
    {
    	userProfileService.userProfile(userName);
    }

    @PostMapping("/uploadProfilePhoto")
    public String uploadProfilePhoto(@RequestParam("username") String username,
                                     @RequestParam("profilePhoto") byte[] profilePhoto) {
        userProfileService.updateProfilePhoto(username, profilePhoto);
       Model.addAttribute("SuccessMessage", "Profile photo updated successfully");
        return "Home";
    }

    @PutMapping("/updateFavSongs")
    public String updateFavSongs(@RequestParam("username") String username,
                                 @RequestParam("favSongs") List<String> favSongs) {
        userProfileService.updateFavSongs(username, favSongs);
        return "Favorite songs updated successfully.";
    }

    @PutMapping("/updateFavBooks")
    public String updateFavBooks(@RequestParam("username") String username,
                                 @RequestParam("favBooks") List<String> favBooks) {
        userProfileService.updateFavBooks(username, favBooks);
        return "Favorite books updated successfully.";
    } 

    @PutMapping("/updateFavPlaces")
    public String updateFavPlaces(@RequestParam("username") String username,
                                  @RequestParam("favPlaces") List<String> favPlaces) {
        userProfileService.updateFavPlaces(username, favPlaces);
        return "Favorite places updated successfully.";
    }
}
