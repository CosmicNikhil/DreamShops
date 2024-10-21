package com.nikhil.dreamshops.security.config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nikhil.dreamshops.security.jwt.AuthTokenFilter;
import com.nikhil.dreamshops.security.jwt.JwtAuthEntryPoint;
import com.nikhil.dreamshops.security.jwt.JwtUtils;
import com.nikhil.dreamshops.security.user.ShopUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class ShopConfig {

	private final JwtUtils jwtUtils;  
	private final ShopUserDetailsService userDetailsService;
	private final JwtAuthEntryPoint authEntryPoint;
	private static final List<String> SECURED_URLS = List.of("/api/v1/carts/**","/api/v1/cartItems/**");

	@Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);  // Inject dependencies via constructor
    }

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception 
	{
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider()
	{
		var authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}


	/*
	 * Key Points: CSRF Protection: Disabled, likely because you're using stateless
	 * sessions (like with JWT). Exception Handling: Configures a custom
	 * authentication entry point for handling unauthorized access. Session
	 * Management: Set to STATELESS for token-based authentication (like JWT).
	 * Authorization: Secure specific URLs and permit all other requests. Custom
	 * Filters: Adds a custom authentication filter (authTokenFilter()).
	 * Authentication Provider: Custom daoAuthenticationProvider() for handling
	 * authentication.
	 */

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
		.exceptionHandling(exception -> exception
				.authenticationEntryPoint(authEntryPoint)) // Set authentication entry point
		.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session management to stateless
		.authorizeHttpRequests(auth -> auth
				.requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()// Secure specific URLs
				.anyRequest().permitAll()); // Allow all other requests
		http.authenticationProvider(daoAuthenticationProvider()); // Set the custom authentication provider
		http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Add custom auth token filter before the default one

		return http.build(); // Build the security chain
	}



}