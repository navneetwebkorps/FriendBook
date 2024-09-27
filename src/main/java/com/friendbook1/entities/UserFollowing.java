package com.friendbook1.entities;

import java.util.List;
import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="UserFollwing")
public class UserFollowing {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	int ID;
	private String userName;
	private String followingUserName;
	private Boolean status;
	
	public UserFollowing()
	{
		
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFollowingUserName() {
		return followingUserName;
	}
	public void setFollowingUserName(String followingUserName) {
		this.followingUserName = followingUserName;
	}
	
}