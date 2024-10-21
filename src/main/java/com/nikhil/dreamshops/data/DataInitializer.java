package com.nikhil.dreamshops.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nikhil.dreamshops.model.Role;
import com.nikhil.dreamshops.model.User;
import com.nikhil.dreamshops.repository.RoleRepository;
import com.nikhil.dreamshops.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent>{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Set<String> defaultRoles = Set.of("ROLE_ADMIN","ROLE_USER");
		createDefaultUserIfNotExists();
		createDefaultRoleIfNotExists(defaultRoles);
		createDefaultAdmiIfNotExists();
	}

	private void createDefaultUserIfNotExists() {
		Role userRole = roleRepository.findByName("ROLE_USER").get();
		for(int i=1;i<=5;i++)
		{
			String defaultEmail = "user"+i+"@email.com";
			if(userRepository.existsByEmail(defaultEmail))
			{
				continue;
			}
			User user = new User();
			user.setFirstName("The User");
			user.setLastName("User"+i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(userRole));
			userRepository.save(user);
			System.out.println("Default user "+i+" created successfully.");
		}
	}

	private void createDefaultAdmiIfNotExists() {
		Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
		for(int i=1;i<=2;i++)
		{
			String defaultEmail = "admin"+i+"@email.com";
			if(userRepository.existsByEmail(defaultEmail))
			{
				continue;
			}
			User user = new User();
			user.setFirstName("Admin");
			user.setLastName("Admin"+i);
			user.setEmail(defaultEmail);
			user.setPassword(passwordEncoder.encode("123456"));
			user.setRoles(Set.of(adminRole));
			userRepository.save(user);
			System.out.println("Default Admin user "+i+" created successfully.");
		}
	}

	private void createDefaultRoleIfNotExists(Set<String> roles) {
		roles.stream()
		.filter(role -> roleRepository.findByName(role).isEmpty()) // Check if the role does not exist
		.map(Role::new) // Create a new Role object
		.forEach(roleRepository::save); // Save the new Role object
	}




}
