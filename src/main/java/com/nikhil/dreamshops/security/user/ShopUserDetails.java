package com.nikhil.dreamshops.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nikhil.dreamshops.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShopUserDetails implements  UserDetails{

	private Long id;
	private String email;
	private String passwword;

	private Collection<GrantedAuthority> authorities;

	public static ShopUserDetails builtUserDetails(User user)
	{
		List<GrantedAuthority> authorities = 
				user.getRoles().stream()
				.map(role->new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());

		return new ShopUserDetails(
				user.getId(),
				user.getEmail(),
				user.getPassword(),
				authorities
				);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return passwword;
	}

	@Override
	public String getUsername() {
		return email;
	}

}