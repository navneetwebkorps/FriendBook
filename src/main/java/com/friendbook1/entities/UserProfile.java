package com.friendbook1.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "UserProfile")

public class UserProfile {

	@Id
	private String username;

	@Lob
	@Column(name = "profilePhoto", columnDefinition = "LONGBLOB")
	private byte[] profilePhoto;
	private List<String> favSongs;
	private List<String> favBooks;
	private List<String> favPlaces;
	@Transient
	private String base64Photo;

	public String getBase64Photo() {
		return base64Photo;
	}

	public void setBase64Photo(String base64Photo) {
		this.base64Photo = base64Photo;
	}

	public UserProfile() {

	}

	public UserProfile(String username, byte[] profilePhoto, List<String> favSongs, List<String> favBooks,
			List<String> favPlaces) {
		super();
		this.username = username;
		this.profilePhoto = profilePhoto;
		this.favSongs = favSongs;
		this.favBooks = favBooks;
		this.favPlaces = favPlaces;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(byte[] profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public List<String> getFavSongs() {
		return favSongs;
	}

	public void setFavSongs(List<String> favSongs) {
		this.favSongs = favSongs;
	}

	public List<String> getFavBooks() {
		return favBooks;
	}

	public void setFavBooks(List<String> favBooks) {
		this.favBooks = favBooks;
	}

	public List<String> getFavPlaces() {
		return favPlaces;
	}

	public void setFavPlaces(List<String> favPlaces) {
		this.favPlaces = favPlaces;
	}

}