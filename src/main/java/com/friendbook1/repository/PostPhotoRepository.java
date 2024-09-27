package com.friendbook1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.friendbook1.entities.PostPhoto;

public interface PostPhotoRepository extends JpaRepository<PostPhoto, String> {
	List<PostPhoto> findAllByUserNameOrderByUploadTimeDesc(String userName);
	  Optional<PostPhoto> findByRandomId(int randomId);

}
