package com.friendbook1.entities;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PhotoLikeComment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhotoLikeComment {

	@Id
	private int randomId;

	private String userName;

	private List<String> liked;

	@ElementCollection
	private List<Comment> comments;

	public PhotoLikeComment() {

	}

	public int getRandomId() {
		return randomId;
	}

	public void setRandomId(int randomId) {
		this.randomId = randomId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<String> getLiked() {
		return liked;
	}

	public void setLiked(List<String> liked) {
		this.liked = liked;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

}