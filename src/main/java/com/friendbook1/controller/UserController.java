package com.friendbook1.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import com.friendbook1.entities.UserProfile;
import com.friendbook1.repository.PhotoLikeCommentRepository;
import com.friendbook1.repository.PostPhotoRepository;
import com.friendbook1.repository.UserFollowingRepository;
import com.friendbook1.repository.UserProfileRepository;
import com.friendbook1.repository.UserRepository;
import com.friendbook1.entities.PhotoLikeComment;
import com.friendbook1.entities.PostPhoto;
import com.friendbook1.entities.User;
import com.friendbook1.entities.UserFollowing;
import com.friendbook1.services.PostPhotoServices;
import com.friendbook1.services.UserFollowService;
import com.friendbook1.services.UserService;
import com.friendbook1.services.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class UserController {
	@Autowired
	private UserService userService;
@Autowired
private UserServices userServices;
	@Autowired
	private PostPhotoServices postPhotoService;
	@Autowired
	private PostPhotoRepository postPhotoRepository;
	@Autowired
	private UserFollowingRepository userFollowingRepository;
	@Autowired
	private UserProfileRepository userProfileRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserFollowingRepository followingRepository;
	@Autowired
	private UserFollowService userFollowService;
	@Autowired
	private PhotoLikeCommentRepository photoLikeCommentRepository;

	@RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
	public String home(Model model, HttpSession session) {
	//	userServices.sessionCheck(session);
		String username = (String) session.getAttribute("username");
		User user1 = userRepository.findByUsername(username).orElseThrow();
		// UserProfile userProfile =
		// userProfileRepository.fi1ndByUsername(user1.getUsername());

		model.addAttribute("user", user1);
		// model.addAttribute("userProfile", userProfile);
		// long following = userFollowService.userFollowing(user1.getUsername());
		// long followers = userFollowService.userFollowers(user1.getUsername());
		// model.addAttribute("followers", followers);
		// model.addAttribute("following", following);

		List<String> followersUsername = followingRepository.findFollowingUserNameByUserName(username);
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

		// Flatten the 2D list and convert photos to Base64
		for (List<PostPhoto> pp : postPhotoList) {
			for (PostPhoto singlePP : pp) {
				// Convert the byte[] to Base64 string for each post photo
				String base64Image = Base64.getEncoder().encodeToString(singlePP.getPhoto());
				singlePP.setBase64Photo(base64Image); // Assuming there's a 'base64Photo' field
				finalPostPhotoList.add(singlePP);
			}
		}

		// Sort by upload time (latest first)
		finalPostPhotoList.sort((p1, p2) -> p2.getUploadTime().compareTo(p1.getUploadTime()));
		
		for (PostPhoto p : finalPostPhotoList) {
			Optional<PhotoLikeComment> PLC = photoLikeCommentRepository.findById(p.getRandomId());
			if (!PLC.isEmpty()) {
				try {
					List<String> LikedUsername = (PLC.get().getLiked());
					if (!LikedUsername.isEmpty()) {
						if (LikedUsername.contains(user1.getUsername())) {
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

		// Add to model
		model.addAttribute("postPhotoList", finalPostPhotoList);

		model.addAttribute("username", user1.getUsername());
		Map<String, byte[]> userSuggestions = userService.getUserSuggestions(user1.getUsername());
		model.addAttribute("userSuggestions", userSuggestions);
		// Get or create a session
		session.setAttribute("username", user1.getUsername()); // Set the attribute

		System.out.println("sj" + user1.getUsername());
		System.out.println("list" + finalPostPhotoList);
		// System.out.println("sj" + userProfile.getFavSongs());

		return "Home";
	}

	@GetMapping("/Login")
	public String login() {
		return "Login";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.setAttribute("username", null);
	//	session.invalidate();		
		return "Login";
	}

	@PostMapping("/updateProfile")
	public String updateProfile(HttpSession session, Model model) {
		UserProfile userProfile =userProfileRepository.findByUsername(session.getAttribute("username").toString());
		System.out.println("tt"+ userProfile.getFavBooks());
		model.addAttribute("userProfile", userProfile);
		return "UpdateProfile";
	}

	@GetMapping("/Navbar")
	public String Navbar() {
		return "Navbar";
	}

	@GetMapping("/RegisterPage")
	public String RegisterPage() {
		return "Register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("user") User user, Model model) {
		System.out.println("controller call");

		UserProfile userName = new UserProfile(user.getUsername(), null, null, null, null);
		String msg=userService.registerUser(user);
		model.addAttribute("message", msg);
		return "login";
	}

	@PostMapping("/userLogin")
	public String userLogin(@ModelAttribute("user") User user, Model model, HttpSession session) {
		System.out.println("in userLogin Controller");
		boolean ans = userService.userLogin(user);
		if (ans == true) {

			User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow();
			// UserProfile userProfile =
			// userProfileRepository.findByUsername(user1.getUsername());

			model.addAttribute("user", user1);
			// model.addAttribute("userProfile", userProfile);
			long following = userFollowService.userFollowing(user1.getUsername());
			long followers = userFollowService.userFollowers(user1.getUsername());
			model.addAttribute("followers", followers);
			model.addAttribute("following", following);
			model.addAttribute("username", user1.getUsername());
			Map<String, byte[]> userSuggestions = userService.getUserSuggestions(user1.getUsername());
			model.addAttribute("userSuggestions", userSuggestions);
			// Get or create a session
			session.setAttribute("username", user1.getUsername()); // Set the attribute

			System.out.println("sj" + user1.getUsername());
			// System.out.println("sj" + userProfile.getFavSongs());

			List<String> followersUsername = followingRepository.findFollowingUserNameByUserName(user1.getUsername());
			List<List<PostPhoto>> postPhotoList = new ArrayList<>();

			System.out.println("njnjn" + followersUsername);
			for (String follower : followersUsername) {
				List<PostPhoto> postphototemp = postPhotoRepository.findAllByUserNameOrderByUploadTimeDesc(follower);
				if (!postphototemp.isEmpty()) {
					postPhotoList.add(postphototemp);
				}
				System.out.println("sdfajf0" + follower);
			}

			List<PostPhoto> finalPostPhotoList = new ArrayList<>();

			// Flatten the 2D list and convert photos to Base64
			for (List<PostPhoto> pp : postPhotoList) {
				for (PostPhoto singlePP : pp) {
					// Convert the byte[] to Base64 string for each post photo
					String base64Image = Base64.getEncoder().encodeToString(singlePP.getPhoto());
					singlePP.setBase64Photo(base64Image); // Assuming there's a 'base64Photo' field
					finalPostPhotoList.add(singlePP);
				}
			}

			// Sort by upload time (latest first)
			finalPostPhotoList.sort((p1, p2) -> p2.getUploadTime().compareTo(p1.getUploadTime()));

			// already like or not

			for (PostPhoto p : finalPostPhotoList) {
				Optional<PhotoLikeComment> PLC = photoLikeCommentRepository.findById(p.getRandomId());
				if (!PLC.isEmpty()) {
					try {
						List<String> LikedUsername = (PLC.get().getLiked());
						if (!LikedUsername.isEmpty()) {
							if (LikedUsername.contains(user1.getUsername())) {
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

			// Add to model
			model.addAttribute("postPhotoList", finalPostPhotoList);
			 model.addAttribute("message", "Your action was successful!");

			return "Home";
		} else {
			return "LogIn";
		}
	}

	@GetMapping("/profile")
	public String userProfile(Model model, HttpSession session) {
	    // Get the username from session
	    String username = (String) session.getAttribute("username");
	    User user1 = userRepository.findByUsername(username).orElseThrow();
	    UserProfile userProfile = userProfileRepository.findByUsername(user1.getUsername());

	    // Add user and profile to the model
	    model.addAttribute("user", user1);
	    model.addAttribute("userProfile", userProfile);

	    // Get followers and following count
	    long following = userFollowService.userFollowing(user1.getUsername());
	    long followers = userFollowService.userFollowers(user1.getUsername());
	    model.addAttribute("followers", followers);
	    model.addAttribute("following", following);
	    model.addAttribute("username", user1.getUsername());

	    // Fetch the list of PostPhoto objects for the user
	    List<PostPhoto> postPhotos = postPhotoService.getPhotosByUser(username);

	    // Iterate over the list to set base64Photo and alreadyLiked fields
	    for (PostPhoto postPhoto : postPhotos) {
	        // Convert photo byte array to base64 string
	        String base64Photo = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(postPhoto.getPhoto());
	        postPhoto.setBase64Photo(base64Photo);
	        
	        // Check if the current user has already liked the post (you can implement this logic in a service)
	        Optional<PhotoLikeComment> plc=photoLikeCommentRepository.findById(postPhoto.getRandomId());
	        if(!plc.isEmpty())
	        {
	        	List<String> listOfLikes=plc.get().getLiked();
	        	if(listOfLikes.contains(username))
	        	{
	        		postPhoto.setAlreadyLiked(true);
	        	}
	        	else {
	        		postPhoto.setAlreadyLiked(false);
				}
	        }
	    }
		
		  if (userProfile.getProfilePhoto() != null) { String base64ProfilePhoto =
		  "data:image/jpeg;base64," +
		  Base64.getEncoder().encodeToString(userProfile.getProfilePhoto());
		  userProfile.setBase64Photo(base64ProfilePhoto); // Set Base64 string with
		
		  }
	    // Add the list of post photos to the model
	    model.addAttribute("postPhotos", postPhotos);

	    return "Profile";
	}


	@PostMapping("/updateProfile1")
	public String updateProfile(@RequestParam("profilePhoto") MultipartFile profilePhoto,
			@RequestParam("favSongs") String favSongs, @RequestParam("favBooks") String favBooks,
			@RequestParam("favPlaces") String favPlaces, HttpSession session, Model model) {
		String username = (String) session.getAttribute("username");
		UserProfile userProfile = userProfileRepository.findByUsername(username);

		if (!profilePhoto.isEmpty()) {
			try {
				userProfile.setProfilePhoto(profilePhoto.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Convert comma-separated strings to lists
		List<String> Songs = new ArrayList<>();
		List<String> Books =  new ArrayList<>();
		List<String> Places = new ArrayList<>();
		if(!favSongs.isEmpty())
		Songs.addAll(Arrays.asList(favSongs.split(",")));
		if(!favBooks.isEmpty())
		Books.addAll(Arrays.asList(favBooks.split(",")));
		if(!favPlaces.isEmpty())
		Places.addAll(Arrays.asList(favPlaces.split(",")));
		System.out.println("ffffff"+favBooks+"fvfv"+favPlaces+"f vk"+favSongs);

		userProfile.setFavSongs(Songs);
		userProfile.setFavBooks(Books);
		userProfile.setFavPlaces(Places);
		userProfileRepository.save(userProfile);
		 model.addAttribute("message", "Your action was successful!");
		 System.out.println("ffffff"+favBooks+"fvfv"+favPlaces+"f vk"+favSongs);

		return "redirect:/profile"; // This will redirect to the userProfile controller
	}

	@GetMapping("/userSuggestion")
	public String userSuggestion(Model model, HttpSession session) {
		String username = (String) session.getAttribute("username");
		User user1 = userRepository.findByUsername(username).orElseThrow();
		Map<String, byte[]> userSuggestions = userService.getUserSuggestions(user1.getUsername());
		model.addAttribute("userSuggestions", userSuggestions);
		return "Suggestion";
	}

	@GetMapping("/getUserSuggestion")
	public String getuserSuggestion(Model model, HttpSession session) {
		String username = (String) session.getAttribute("username");
		User user1 = userRepository.findByUsername(username).orElseThrow();
		Map<String, byte[]> userSuggestions = userService.getUserSuggestions(user1.getUsername());
		model.addAttribute("userSuggestions", userSuggestions);
		return "Suggestion";
	}

	@GetMapping("/searchUser")
	public String searchUser(Model model, String searchData, HttpSession session) {
		List<User> searchUsername = userService.searchUser(searchData);
		Map<String, Boolean> searchUsernameStatus = new HashMap<>();
		List<String> followerUsername = userFollowingRepository
				.findFollowingUserNameByUserName(session.getAttribute("username").toString());
		for (User u : searchUsername) {
			if (followerUsername.contains(u.getUsername())) {
				searchUsernameStatus.put(u.getUsername(), true);
			} else {
				searchUsernameStatus.put(u.getUsername(), false);
			}
		}
		model.addAttribute("searchUsernameStatus", searchUsernameStatus);
		model.addAttribute("searchData", searchData);

		return "SearchedUser";
	}

	@GetMapping("/fViewProfile")
	public String fViewProfile(Model model, String fUsername, HttpSession session)
	{
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
		Map<Integer, String> photoMap = postPhoto.stream().collect(Collectors.toMap(PostPhoto::getRandomId, // Key:
																											// randomId
				photo -> "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo.getPhoto()) // Value:
																											// base64
																											// image
																											// string
		));
		if (userProfile.getProfilePhoto() != null) {
		    String base64ProfilePhoto = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(userProfile.getProfilePhoto());
		    userProfile.setBase64Photo(base64ProfilePhoto); // Set Base64 string with MIME type
		}



		model.addAttribute("postPhoto", photoMap);
		return "fViewProfile";
	}
	
	@GetMapping("/notifications")
	public String notifications(Model model,HttpSession session)
	{
		String username = (String) session.getAttribute("username");
		List<UserFollowing> requestList=userFollowingRepository.findByFollowingUserNameAndStatusIsNull(username);
		/*
		 * Map<String , Boolean> requestNewList=new HashMap<>(); for(String l
		 * :requestList) { List<String>
		 * ans=userFollowingRepository.findRequestByUserNameandFollowerUsername(l,
		 * username); List<String>
		 * ans2=userFollowingRepository.findRequestByUserNameandFollowerUsername(
		 * username, l); if(!ans.isEmpty() || !ans2.isEmpty()) { requestNewList.put(l,
		 * true); } else { requestNewList.put(l, false);
		 * 
		 * } } System.out.println("bbb"+requestList); model.addAttribute("requestList",
		 * requestList); model.addAttribute("requestNewList", requestNewList);
		 */
		model.addAttribute("requestList", requestList);
		return "Notification";
	}
}

