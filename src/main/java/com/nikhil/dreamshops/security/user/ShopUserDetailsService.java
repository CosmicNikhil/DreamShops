package com.nikhil.dreamshops.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nikhil.dreamshops.model.User;
import com.nikhil.dreamshops.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ShopUserDetailsService implements UserDetailsService{

	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = Optional.ofNullable(userRepository.findByEmail(email))
				.orElseThrow(()->new UsernameNotFoundException("User not found!"));
		return ShopUserDetails.builtUserDetails(user);
	}

}
