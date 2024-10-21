package com.nikhil.dreamshops.service.user;

import com.nikhil.dreamshops.dto.UserDto;
import com.nikhil.dreamshops.model.User;
import com.nikhil.dreamshops.request.CreateUserRequest;
import com.nikhil.dreamshops.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
	User getAuthenticatedUser();
}