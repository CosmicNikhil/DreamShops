package com.nikhil.dreamshops.service.cart;

import java.math.BigDecimal;

import com.nikhil.dreamshops.dto.CartDto;
import com.nikhil.dreamshops.model.Cart;
import com.nikhil.dreamshops.model.User;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
	CartDto convertToDto(Cart cart);
}