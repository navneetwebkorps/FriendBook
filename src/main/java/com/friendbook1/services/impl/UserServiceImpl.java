package com.friendbook1.services.impl;

import java.util.ArrayList;
import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.friendbook1.entities.PhotoLikeComment;
import com.friendbook1.entities.PostPhoto;
import com.friendbook1.entities.User;
import com.friendbook1.entities.UserProfile;
import com.friendbook1.repository.PhotoLikeCommentRepository;
import com.friendbook1.repository.PostPhotoRepository;
import com.friendbook1.repository.UserFollowingRepository;
import com.friendbook1.repository.UserProfileRepository;
import com.friendbook1.repository.UserRepository;
import com.friendbook1.services.PostPhotoServices;
import com.friendbook1.services.UserFollowService;
import com.friendbook1.services.UserProfileService;
import com.friendbook1.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
	@Autowired
	private UserProfileRepository userProfileRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserFollowingRepository userFollowingRepository;
	@Autowired
	private UserProfileServiceImpl userProfileServices;
	@Autowired
	private PostPhotoRepository postPhotoRepository;
	@Autowired
	private PhotoLikeCommentRepository photoLikeCommentRepository;
	@Autowired
	private UserProfileService userProfileService;
	@Autowired
	private UserFollowService userFollowService;
	@Autowired
	private PostPhotoServices postPhotoService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserProfileRepository userProfileRepository, UserRepository userRepository,
			UserFollowingRepository userFollowingRepository, UserProfileServiceImpl userProfileServices,
			PasswordEncoder passwordEncoder) {
		this.userProfileRepository = userProfileRepository;
		this.userRepository = userRepository;
		this.userFollowingRepository = userFollowingRepository;
		this.userProfileServices = userProfileServices;
		this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;
	}

	@Override
	public String registerUser(User user) {
		try {

			if (user.getEmail() == null || user.getFname() == null || user.getLname() == null
					|| user.getPassword() == null) {
				logger.warning("Required fields (Email, First Name, Last Name, Password) are missing.");
				return "Please fill all fields";
			}

			System.out.println("Raw password before encoding: " + user.getPassword());

			Optional<User> emailExist = userRepository.findByEmail(user.getEmail());
			if (emailExist.isPresent()) {
				logger.warning("Email already exists: " + user.getEmail());
				return "Email already exists";
			}

			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword); // Store the encoded password

			System.out.println("Encoded password: " + encodedPassword);

			user.setUsername(generateUniqueUsername(user.getFname()));

			Optional<User> isUsernameExist = userRepository.findByUsername(user.getUsername());
			if (isUsernameExist.isPresent()) {
				logger.warning("Generated username already exists: " + user.getUsername());
				return null;
			}

			List<String> songs = new ArrayList<>();
			List<String> books = new ArrayList<>();
			List<String> places = new ArrayList<>();
			UserProfile obj = new UserProfile(user.getUsername(), null, songs, books, places);
			userProfileRepository.save(obj);

			userRepository.save(user);
			return "User registered successfully";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while registering user: " + user.getEmail(), e);
			return "Try another time";
		}
	}

	@Override
	public User findUserById(Integer userId) {
		try {
			Optional<User> optionalUser = userRepository.findById(userId);
			if (optionalUser.isPresent()) {
				return optionalUser.get();
			} else {
				logger.warning("User not found by ID: " + userId);
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error finding user by ID: " + userId, e);
			return null;
		}
	}

	@Override
	public User findUserByUsername(String username) {
		try {
			Optional<User> optionalUser = userRepository.findByUsername(username);
			if (optionalUser.isPresent()) {
				return optionalUser.get();
			} else {
				logger.warning("User not found by username: " + username);
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error finding user by username: " + username, e);
			return null;
		}
	}

	@Override
	public List<User> searchUser(String name) {
		try {
			List<User> searchedUsers = userRepository.searchByUsernameOrName(name);
			if (searchedUsers.isEmpty()) {
				logger.info("No users found for search term: " + name);
				return null;
			}
			return searchedUsers;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error searching for users by name: " + name, e);
			return null;
		}
	}

	@Override
	public User updateUser(User user) {
		try {
			User updateUser = findUserByUsername(user.getUsername());
			if (updateUser != null) {
				updateUser.setFname(user.getFname());
				updateUser.setLname(user.getLname());
				return userRepository.save(updateUser);
			} else {
				logger.warning("Cannot update: User not found for username: " + user.getUsername());
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error updating user: " + user.getUsername(), e);
			return null;
		}
	}

	@Override
	public Boolean userLogin(User user) {
		try {
			logger.info("Attempting user login for email: " + user.getEmail());

			Optional<User> loginUser = userRepository.findByEmail(user.getEmail());
			if (loginUser.isEmpty()) {
				logger.warning("User login failed: Email not found - " + user.getEmail());
				return false;
			}

			if (loginUser.get().getPassword().equals(user.getPassword())) {
				logger.info("User login successful for email: " + user.getEmail());
				return true;
			} else {
				logger.warning("User login failed: Incorrect password for email: " + user.getEmail());
				return false;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error during user login for email: " + user.getEmail(), e);
			return false;
		}
	}

	private static final int RANDOM_BOUND = 10000;

	public String generateUniqueUsername(String fname) {
		Random random = new Random();
		String username;
		do {
			int randomNumber = random.nextInt(RANDOM_BOUND);
			username = fname + randomNumber;
		} while (isUsernameTaken(username));
		return username;
	}

	private boolean isUsernameTaken(String username) {
		Optional<User> existingUser = userRepository.findByUsername(username);
		return existingUser.isPresent();
	}

	@Override
	public Map<String, byte[]> getUserSuggestions(String username) {
		List<User> users = userRepository.findByUsernameNot(username);
		List<String> alreadyRequestOfFollow = userFollowingRepository.findFollowingUserNameByUserName1(username);

		Map<String, byte[]> followSuggestions = new HashMap<>();
		for (User user : users) {
			System.out.println("in get user Suggetion method " + user.getUsername());
			if (!alreadyRequestOfFollow.contains(user.getUsername())) {
				UserProfile up = userProfileServices.userProfile(user.getUsername());
				if (up != null) {
					followSuggestions.put(user.getUsername(), up.getProfilePhoto());
				} else {
					followSuggestions.put(user.getUsername(), null);
				}
			}
		}

		return followSuggestions;
	}

	@Override
	public Optional<User> authenticate(String email) {

		return Optional.empty();
	}

	@Override
	public Model userHome(String username, Model model) {

		List<String> followersUsername = userFollowingRepository.findFollowingUserNameByUserName(username);
		System.out.println("folower" + followersUsername);
		List<List<PostPhoto>> postPhotoList = new ArrayList<>();
		for (String follower : followersUsername) {
			List<PostPhoto> postphototemp = postPhotoRepository.findAllByUserNameOrderByUploadTimeDesc(follower);
			if (!postphototemp.isEmpty()) {
				postPhotoList.add(postphototemp);
				System.out.println("sdbfbsfhbsdgbjwsbdfjwbeik");
			}
		}
		List<PostPhoto> finalPostPhotoList = new ArrayList<>();

		for (List<PostPhoto> pp : postPhotoList) {
			for (PostPhoto singlePP : pp) {

				String base64Image = Base64.getEncoder().encodeToString(singlePP.getPhoto());
				singlePP.setBase64Photo(base64Image);
				finalPostPhotoList.add(singlePP);
			}
		}

		finalPostPhotoList.sort((p1, p2) -> p2.getUploadTime().compareTo(p1.getUploadTime()));

		for (PostPhoto p : finalPostPhotoList) {
			Optional<PhotoLikeComment> PLC = photoLikeCommentRepository.findById(p.getRandomId());
			if (!PLC.isEmpty()) {
				try {
					List<String> LikedUsername = (PLC.get().getLiked());
					if (!LikedUsername.isEmpty()) {
						if (LikedUsername.contains(username)) {
							p.setAlreadyLiked(true);
							System.out.println("ytttttytytyty");

						} else {
							p.setAlreadyLiked(false);
						}
					}
				}

				catch (Exception e) {
					p.setAlreadyLiked(false);
					System.out.println("get Liked is null");
				}

			}
		}

		model.addAttribute("postPhotoList", finalPostPhotoList);

		model.addAttribute("username", username);

		return model;
	}

	@Override
	public Model userProfile(String username, Model model) {
		User user1 = userRepository.findByUsername(username).orElseThrow();
		UserProfile userProfile = userProfileRepository.findByUsername(user1.getUsername());
		model.addAttribute("user", user1);
		model.addAttribute("userProfile", userProfile);

		long following = userFollowService.userFollowing(user1.getUsername());
		long followers = userFollowService.userFollowers(user1.getUsername());
		model.addAttribute("followers", followers);
		model.addAttribute("following", following);
		model.addAttribute("username", user1.getUsername());

		List<PostPhoto> postPhotos = postPhotoService.getPhotosByUser(username);
		postPhotos = userProfileService.userPhotosFilter(postPhotos, username, photoLikeCommentRepository, userProfile);
		model.addAttribute("postPhotos", postPhotos);

		return model;
	}

	@Override
	public Model userNotification(String username, Model model) {
		List<String> requestList = userFollowingRepository.findUserNameByFollowingUserNameAndStatusIsNull(username);

		Map<String, Boolean> requestNewList = new HashMap<>();
		for (String l : requestList) {
			List<String> ans = userFollowingRepository.findRequestByUserNameandFollowerUsername(l, username);
			List<String> ans2 = userFollowingRepository.findRequestByUserNameandFollowerUsername(username, l);
			if (!ans.isEmpty() && !ans2.isEmpty()) {
				requestNewList.put(l, true);
				System.out.println("in if " + ans + "  l" + ans2);
			} else {
				requestNewList.put(l, false);
				System.out.println("in else " + ans + "  l" + ans2);
			}
		}
		System.out.println("bbb" + requestList);

		model.addAttribute("requestList", requestNewList);
		return model;
	}

	@Override
	public Model fViewProfile(String fUsername, Model model) {
		User user1 = userRepository.findByUsername(fUsername).orElseThrow();
		UserProfile userProfile = userProfileRepository.findByUsername(user1.getUsername());

		model.addAttribute("fUser", user1);
		model.addAttribute("fUserProfile", userProfile);
		long following = userFollowService.userFollowing(user1.getUsername());
		long followers = userFollowService.userFollowers(user1.getUsername());
		model.addAttribute("followers", followers);
		model.addAttribute("following", following);
		model.addAttribute("fUsername", user1.getUsername());
		List<PostPhoto> postPhoto = postPhotoService.getPhotosByUser(fUsername);
		Map<Integer, String> photoMap = userProfileService.fUserPhotosFilter(postPhoto, userProfile);

		model.addAttribute("postPhoto", photoMap);

		return model;
	}

}