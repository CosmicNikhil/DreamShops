package com.nikhil.dreamshops.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private BigDecimal totalAmount = BigDecimal.ZERO;

	//CascadeType.ALL:When a cart is deleted,all cartItems are deleted
	//orphanRemoval:Whenever cartItem is removed from set,it is removed from database
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CartItem> items = new HashSet<>();

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;


	/*
	 * orphanRemoval = true (JPA): ->Automatically deletes child entities when
	 * removed from the parent’s collection. ->Used in one-to-many or one-to-one
	 * relationships. ->Key: Deletes orphans without needing explicit delete
	 * operations.
	 */



	public void addItem(CartItem item) {
		this.items.add(item);
		item.setCart(this);
		updateTotalAmount();
	}

	public void removeItem(CartItem item) {
		this.items.remove(item);
		item.setCart(null);
		updateTotalAmount();
	}

	private void updateTotalAmount() {
		this.totalAmount = items.stream().map(item -> {
			BigDecimal unitPrice = item.getUnitPrice();
			if (unitPrice == null) {
				return  BigDecimal.ZERO;
			}
			return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
		}).reduce(BigDecimal.ZERO, BigDecimal::add);
	}



}