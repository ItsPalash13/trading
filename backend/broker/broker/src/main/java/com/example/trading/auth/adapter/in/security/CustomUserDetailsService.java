package com.example.trading.auth.adapter.in.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.trading.user.application.port.in.UserQuery;
import com.example.trading.user.application.result.UserCredentials;

/**
 * Adapter in (security) — Spring Security drives the app.
 * <p>
 * Lives under {@code adapter/in/security}: login asks “load user by email”,
 * this adapter turns that into a call on user's {@link UserQuery} (port in).
 * Contrast {@code adapter/out/security}: app calls out to BCrypt.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserQuery userQuery;

	public CustomUserDetailsService(UserQuery userQuery) {
		this.userQuery = userQuery;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserCredentials credentials = userQuery.findCredentialsByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		return User.withUsername(credentials.email())
				.password(credentials.passwordHash())
				.roles("USER")
				.build();
	}
}
