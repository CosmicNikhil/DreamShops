package com.nikhil.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nikhil.dreamshops.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Method to find a cart by user ID
    Cart findByUserId(Long userId);

    // Custom query to delete a cart by its ID
    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.id = :id")
    void deleteCartById(@Param("id") Long id);
}
