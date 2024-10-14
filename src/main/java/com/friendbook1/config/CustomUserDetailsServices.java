
package com.friendbook1.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.friendbook1.entities.User;
import com.friendbook1.repository.UserRepository;

@Service
public class CustomUserDetailsServices implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Fetching user with email: " + username);
		Optional<User> user = userRepository.findByEmail(username);

		if (!user.isPresent()) {
			System.out.print("User not found with email: " + username);
			throw new UsernameNotFoundException("User not found");
		}

		System.out
				.println("Found user: " + user.get().getEmail() + " with hashed password: " + user.get().getPassword());

			System.out.println("Raw password being checked: [HIDDEN FOR SECURITY]");

		return new CustomUserDetails(user.get());
	}
	
	public boolean validatePassword(String password, String encryptedPasswordFromDb) {
	    return passwordEncoder.matches(password, encryptedPasswordFromDb); // Correct comparison
	}

}
