package com.friendbook1.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PostPhoto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPhoto {
	@Id
	private int randomId;
	private String userName;
	@Lob
	@Column(name = "photo", columnDefinition = "LONGBLOB")
	private byte[] photo;
	private LocalDateTime uploadTime;
	@Transient
	private String base64Photo;

	@Transient
	private Boolean alreadyLiked;

	public Boolean getAlreadyLiked() {
		return alreadyLiked;
	}

	public void setAlreadyLiked(Boolean alreadyLiked) {
		this.alreadyLiked = alreadyLiked;
	}

	public String getBase64Photo() {
		return base64Photo;
	}

	public void setBase64Photo(String base64Photo) {
		this.base64Photo = base64Photo;
	}

	public PostPhoto() {

	}

	public int getRandomId() {
		return randomId;
	}

	public void setRandomId(int randomId) {
		this.randomId = randomId;
	}

	public PostPhoto(String userName, byte[] photo, LocalDateTime uploadTime, int randomId) {
		super();
		this.userName = userName;
		this.photo = photo;
		this.uploadTime = uploadTime;
		this.randomId = randomId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public LocalDateTime getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(LocalDateTime uploadTime) {
		this.uploadTime = uploadTime;
	}
}
