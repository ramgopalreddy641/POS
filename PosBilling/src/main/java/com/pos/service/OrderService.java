package com.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.model.Order;
import com.pos.model.OrderItem;
import com.pos.model.Product;
import com.pos.repo.OrderRepository;
import com.pos.repo.ProductRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(Order order) {
        // Validate the order: Ensure the order has at least one item
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Order must have at least one order item.");
        }

        double totalOrderValue = 0;  // To calculate the total value of the order

        // Loop through order items to validate and calculate total value
        for (OrderItem item : order.getOrderItems()) {
            if (item.getProduct() == null || item.getProduct().getProductId() == null) {
                throw new RuntimeException("Invalid product in order item.");
            }

            // Fetch product from DB
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found for ID: " + item.getProduct().getProductId()));

            // Check stock availability
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
            }

            // Deduct stock
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);  // Save updated stock in the database

            // Attach the managed product entity to the order item
            item.setProduct(product);

            // Calculate the total value of the order
            totalOrderValue += product.getBasePrice() * item.getQuantity();
        }

        // Set the total value of the order (you may add any additional logic to calculate discounts, taxes, etc.)
        order.setTotalValue(totalOrderValue);  // Assuming you have a totalValue field in the Order class

        // Save the order
        return orderRepository.save(order);
    }

	public Order getOrderById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Order> getAllOrders() {
		// TODO Auto-generated method stub
		return null;
	}
}
