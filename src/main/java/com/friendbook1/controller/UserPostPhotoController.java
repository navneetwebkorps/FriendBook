package com.friendbook1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.friendbook1.entities.PostPhoto;
import com.friendbook1.services.PostPhotoServices;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Controller
public class UserPostPhotoController {

    @Autowired
    private PostPhotoServices postPhotoService;
    

 // Endpoint to upload a new photo for a user
    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("username") String userName,
                                 @RequestParam("photo") MultipartFile photo,Model model) throws IOException {
        byte[] photoBytes = photo.getBytes();
       String msg= postPhotoService.savePostPhoto(userName, photoBytes);
        model.addAttribute("message", msg);
        return "redirect:/profile";
    }

    // Endpoint to fetch all photos for a user in reverse chronological order
    @GetMapping("/{username}/latest")
    public List<PostPhoto> getLatestPhotos(@PathVariable String userName) {
        return postPhotoService.getPhotosByUser(userName);
    }
}
