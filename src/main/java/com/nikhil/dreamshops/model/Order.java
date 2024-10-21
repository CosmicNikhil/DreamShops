package com.nikhil.dreamshops.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.nikhil.dreamshops.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

/*Order attributes:
orderDate: LocalDate.now() for the current date.
totalAmount: BigDecimal value representing the total price.
orderStatus: Enum representing the order's current status, such as PROCESSING or SHIPPED.
user: Linked to a User object (assuming the User entity is already created)
*/


/*
 * In simpler words: `cascade = CascadeType.ALL` means if you save, update, or
 * delete an `Order`, the same actions will happen to its `OrderItem` items.
 * `orphanRemoval = true` means if you remove an `OrderItem` from the list, it
 * will also be deleted from the database automatically.
 */