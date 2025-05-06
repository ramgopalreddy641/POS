package com.pos.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.model.Order;


public interface OrderRepository extends JpaRepository<Order, String> {

	Optional<Order> findById(String orderId);
}
