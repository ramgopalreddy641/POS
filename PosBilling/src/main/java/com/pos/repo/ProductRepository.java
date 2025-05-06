package com.pos.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.model.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
