package com.nikhil.dreamshops.service.order;

import java.util.List;

import com.nikhil.dreamshops.dto.OrderDto;
import com.nikhil.dreamshops.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto convertToDto(Order order);
}