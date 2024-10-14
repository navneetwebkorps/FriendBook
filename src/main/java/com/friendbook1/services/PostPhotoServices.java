package com.friendbook1.services;

import com.friendbook1.entities.PostPhoto;

import java.util.List;

public interface PostPhotoServices {

	String savePostPhoto(String userName, byte[] photo);

	int generateUniqueRandomId();

	List<PostPhoto> getPhotosByUser(String userName);
}
