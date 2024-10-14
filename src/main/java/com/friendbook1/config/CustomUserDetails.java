
package com.friendbook1.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.friendbook1.entities.User;

public class CustomUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1003461298143067958L;
	/**
	 * 
	 */

	private User user;

	public CustomUserDetails(User user) {
		System.out.println("custorm user details"+user.getEmail());
		this.user = user;
	}

	@Override
	public String getPassword() {
		System.out.println("get p" + user.getPassword());
		return user.getPassword(); // Return
	}

	@Override
	public String getUsername() {
		System.out.println("get U" + user.getEmail());
		return user.getEmail(); // Using
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>(); // Return an empty list if no authorities are needed
	}
}
