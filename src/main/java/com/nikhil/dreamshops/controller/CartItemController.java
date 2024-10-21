package com.nikhil.dreamshops.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nikhil.dreamshops.exceptions.ResourceNotFoundException;
import com.nikhil.dreamshops.model.Cart;
import com.nikhil.dreamshops.model.User;
import com.nikhil.dreamshops.response.ApiResponse;
import com.nikhil.dreamshops.service.cart.ICartItemService;
import com.nikhil.dreamshops.service.cart.ICartService;
import com.nikhil.dreamshops.service.user.IUserService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
	private final ICartItemService cartItemService;
	private final ICartService cartService;
	private final IUserService userService;


	@PostMapping("/item/add")
	public ResponseEntity<ApiResponse> addItemToCart(
			@RequestParam Long productId,
			@RequestParam Integer quantity) {
		try {
			User user = userService.getAuthenticatedUser();
			Cart cart= cartService.initializeNewCart(user);

			cartItemService.addItemToCart(cart.getId(), productId, quantity);
			return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
		catch(JwtException e)
		{
			return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
	public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable("itemId") Long productId) {
		try {
			cartItemService.removeItemFromCart(cartId, productId);
			return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}
	}

	@PutMapping("/cart/{cartId}/item/{itemId}/update")
	public  ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
			@PathVariable("itemId") Long productId,
			@RequestParam Integer quantity) {
		try {
			cartItemService.updateItemQuantity(cartId, productId, quantity);
			return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
		}

	}
}
