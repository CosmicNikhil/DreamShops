package com.nikhil.dreamshops.service.cart;

import java.math.BigDecimal;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nikhil.dreamshops.dto.CartDto;
import com.nikhil.dreamshops.exceptions.ResourceNotFoundException;
import com.nikhil.dreamshops.model.Cart;
import com.nikhil.dreamshops.model.User;
import com.nikhil.dreamshops.repository.CartItemRepository;
import com.nikhil.dreamshops.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

	private final CartItemRepository cartItemRepository;
	private final CartRepository cartRepository;
	private final ModelMapper modelMapper;




	@Override
	public Cart getCart(Long id) {
		return cartRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found with id: " + id));
	}

	@Transactional
	public void clearCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		cartItemRepository.deleteAllByCartId(cartId);

		// Now delete the cart
		cartRepository.deleteCartById(cartId);
	}



	@Override
	public BigDecimal getTotalPrice(Long id) {
		Cart cart = getCart(id);
		return cart.getTotalAmount();
	}

	@Override
	public Cart initializeNewCart(User user) {
		return Optional.ofNullable(getCartByUserId(user.getId()))
				.orElseGet(() -> {
					Cart cart = new Cart();
					cart.setUser(user);
					return cartRepository.save(cart);
				});
	}

	@Override
	public Cart getCartByUserId(Long userId) {
		return cartRepository.findByUserId(userId);
	}

	@Override
	public CartDto convertToDto(Cart cart) {
		return modelMapper.map(cart, CartDto.class);
	}
}
