package com.nikhil.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nikhil.dreamshops.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	@Modifying
	@Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
	void deleteAllByCartId(@Param("cartId") Long cartId);
}