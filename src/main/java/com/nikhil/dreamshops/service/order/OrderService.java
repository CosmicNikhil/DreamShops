package com.nikhil.dreamshops.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nikhil.dreamshops.dto.OrderDto;
import com.nikhil.dreamshops.enums.OrderStatus;
import com.nikhil.dreamshops.exceptions.ResourceNotFoundException;
import com.nikhil.dreamshops.model.Cart;
import com.nikhil.dreamshops.model.Order;
import com.nikhil.dreamshops.model.OrderItem;
import com.nikhil.dreamshops.model.Product;
import com.nikhil.dreamshops.repository.OrderRepository;
import com.nikhil.dreamshops.repository.ProductRepository;
import com.nikhil.dreamshops.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final CartService cartService;
	private final ModelMapper modelMapper;


	@Transactional
	@Override
	public Order placeOrder(Long userId) {
		Cart cart   = cartService.getCartByUserId(userId);
		Order order = createOrder(cart);
		List<OrderItem> orderItemList = createOrderItems(order, cart);
		order.setOrderItems(new HashSet<>(orderItemList));
		order.setTotalAmount(calculateTotalAmount(orderItemList));
		Order savedOrder = orderRepository.save(order);
		cartService.clearCart(cart.getId());
		return savedOrder;
	}

	private Order createOrder(Cart cart) {
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderStatus(OrderStatus.PENDING);
		order.setOrderDate(LocalDate.now());
		return  order;
	}

	private List<OrderItem> createOrderItems(Order order, Cart cart) {
		return  cart.getItems().stream().map(cartItem -> {
			Product product = cartItem.getProduct();
			product.setInventory(product.getInventory() - cartItem.getQuantity());
			productRepository.save(product);
			return  new OrderItem(
					order,
					product,
					cartItem.getQuantity(),
					cartItem.getUnitPrice());
		}).toList();
	}

	private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
		return  orderItemList
				.stream()
				.map(item -> item.getPrice()
						.multiply(new BigDecimal(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	public OrderDto getOrder(Long orderId) {
		return orderRepository.findById(orderId)
				.map(this :: convertToDto)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
	}

	@Override
	public List<OrderDto> getUserOrders(Long userId) {
		List<Order> orders = orderRepository.findByUserId(userId);
		return  orders.stream().map(this :: convertToDto).toList();
	}


	 public OrderDto convertToDto(Order order) {
	        return modelMapper.map(order, OrderDto.class);
	  }

	


}

/*
 * modelMapper will not properly map userId because the Order entity contains a
 * User object, but OrderDto expects a userId (a Long value, not the full User
 * object).
 */

/*
 * ModelMapper won't automatically know that it needs to extract the id from the
 * User entity for mapping it to userId in the OrderDto.
 */