package com.friendbook1.entities;

import java.util.List;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PhotoLikeComment")
public class PhotoLikeComment {

    @Id
    private int randomId;

    private String userName;

    private List<String> liked;

    @ElementCollection
    private List<Comment> comments;  // List of embedded Comment objects

    public PhotoLikeComment()
    {
    	
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
	/*
	 * public List<Map<String, String>> getComment() { return comment; } public void
	 * setComment(List<Map<String, String>> comment) { this.comment = comment; }
	 */
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
    

}