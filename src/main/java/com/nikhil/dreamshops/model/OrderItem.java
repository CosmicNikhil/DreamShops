package com.nikhil.dreamshops.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int quantity;
	private BigDecimal price;

	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public OrderItem(Order order, Product product, int quantity, BigDecimal price) {
		this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.price = price;

	}
}

/*
 * OrderItem attributes:
 * 
 *  order: Set to the Order object. 
 *  product: A Product object like "Laptop" or "Mouse."
 *  quantity: The number of units ordered.
 *  price: The individual or total price for that item.
 * 
 * 
 */