package com.pos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;
    private Double totalPrice;
    private Double discountAmount;
    private Double cgstAmount;
    private Double sgstAmount;

    // Default constructor
    public OrderItem() {}

    // Constructor
    public OrderItem(Product product, int quantity, Order order) {
        this.product = product;
        this.quantity = quantity;
        this.order = order;
        calculateTotal();
    }

    @PrePersist
    @PreUpdate
    private void calculateTotal() {
        if (this.product != null && this.quantity > 0) {
            double basePrice = getNonNullValue(this.product.getBasePrice());
            double discount = getNonNullValue(this.product.getDiscount());
            double cgst = getNonNullValue(this.product.getCgst());
            double sgst = getNonNullValue(this.product.getSgst());

            double priceBeforeDiscount = basePrice * this.quantity;
            this.discountAmount = round(priceBeforeDiscount * (discount / 100));
            this.cgstAmount = round(discountAmount * (cgst / 100));
            this.sgstAmount = round(discountAmount * (sgst / 100));

            this.totalPrice = round(priceBeforeDiscount - this.discountAmount + this.cgstAmount + this.sgstAmount);
        } else {
            this.totalPrice = 0.0;
            this.discountAmount = 0.0;
            this.cgstAmount = 0.0;
            this.sgstAmount = 0.0;
        }
    }

    // Utility methods
    private double getNonNullValue(Double value) {
        return value != null ? value : 0.0;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; calculateTotal(); }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; calculateTotal(); }
    public Double getTotalPrice() { return totalPrice; }
    public Double getDiscountAmount() { return discountAmount; }
    public Double getCgstAmount() { return cgstAmount; }
    public Double getSgstAmount() { return sgstAmount; }

    @Override
    public String toString() {
        return "OrderItem [id=" + id + ", product=" + (product != null ? product.getProductName() : "No Product") +
               ", quantity=" + quantity + ", totalPrice=" + totalPrice + ", discountAmount=" + discountAmount +
               ", cgstAmount=" + cgstAmount + ", sgstAmount=" + sgstAmount + "]";
    }
}
