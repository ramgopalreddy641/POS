package com.pos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;  // Change from Long to String for formatted ID

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();  

    public Order() {}

    public Order(List<OrderItem> orderItems) {
        if (orderItems != null) {
            this.orderItems = orderItems;
            
            for (OrderItem item : orderItems) {
                item.setOrder(this);
            }
        }
    }

    @PrePersist
    private void generateOrderId() {
        this.orderId = generateOrderNumber();
    }

    public static String generateOrderNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "ORD-" + LocalDateTime.now().format(formatter);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        if (orderItems != null) {
            this.orderItems = orderItems;
            for (OrderItem item : orderItems) {
                item.setOrder(this);
            }
        }
    }

    public void addOrderItem(OrderItem orderItem) {
        if (orderItem != null && !this.orderItems.contains(orderItem)) {
            this.orderItems.add(orderItem);
            orderItem.setOrder(this);
        }
    }

    public void removeOrderItem(OrderItem orderItem) {
        if (orderItem != null && this.orderItems.remove(orderItem)) {
            orderItem.setOrder(null);
        }
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", orderItems=" + orderItems + "]";
    }

    public void setTotalValue(double totalOrderValue) {
        // Placeholder for setting the total value
    }
}
