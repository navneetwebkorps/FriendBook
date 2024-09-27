package com.friendbook1.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.friendbook1.entities.User;
import com.friendbook1.entities.UserProfile;
import com.friendbook1.repository.UserFollowingRepository;
import com.friendbook1.repository.UserProfileRepository;
import com.friendbook1.repository.UserRepository;

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
    private UserProfileServices userProfileServices;

    @Override
    public String registerUser(User user) {
        try {
            if (user.getEmail() == null || user.getFname() == null || user.getLname() == null || user.getPassword() == null) {
                logger.warning("Required fields (Email, First Name, Last Name, Password) are missing.");
                return "Please fill all fields";
            }

            // Check if email exists
            Optional<User> emailExist = userRepository.findByEmail(user.getEmail());
            if (emailExist.isPresent()) {
                logger.warning("Email already exists: " + user.getEmail());
                return "Mail already exist";
            }

            // Generate a unique username
            user.setUsername(generateUniqueUsername(user.getFname()));

            // Check if username already exists
            Optional<User> isUsernameExist = userRepository.findByUsername(user.getUsername());
            if (isUsernameExist.isPresent()) {
                logger.warning("Generated username already exists: " + user.getUsername());
                return null;
            }
            List<String> songs=new ArrayList<>();
            List<String> books=new ArrayList<>();
            List<String> places=new ArrayList<>();
            UserProfile obj=new UserProfile(user.getUsername(), null, songs, books, places);
            userProfileRepository.save(obj);
            
           
            return "user register successfully";
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

    private static final int RANDOM_BOUND = 10000; // To generate a 4-digit number

    public String generateUniqueUsername(String fname) {
        Random random = new Random();
        String username;
        do {
            int randomNumber = random.nextInt(RANDOM_BOUND); // Generate a random 4-digit number
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
        List<String> alreadyRequestOfFollow = userFollowingRepository.findFollowingUserNameByUserName(username);

        Map<String, byte[]> followSuggestions = new HashMap<>();
        for (User user : users) {
            if (!alreadyRequestOfFollow.contains(user.getUsername())) {
                UserProfile up = userProfileServices.userProfile(user.getUsername());
                if (up != null) {
                    followSuggestions.put(user.getUsername(), up.getProfilePhoto());
                } else {
                    followSuggestions.put(user.getUsername(), null);  // Put null if up is null
                }
            }
        }
        
        return followSuggestions;
    }
}