package com.pos.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;  

    private String invoiceNumber;  
    private double totalBasePrice; 
    private double totalDiscount; 
    private double totalCGST;       
    private double totalSGST;      
    private double grandTotal;     

    private String productNames;

    @CreationTimestamp
    private LocalDateTime createdAt;  

    @UpdateTimestamp
    private LocalDateTime updatedAt;  

    // Default constructor
    public Invoice() {}

    // Constructor with automatic invoice number generation
    public Invoice(Order order, double totalBasePrice, double totalDiscount, 
                   double totalCGST, double totalSGST, double grandTotal) {
        this.order = order;
        this.invoiceNumber = generateInvoiceNumber(); // Generate the formatted invoice number
        this.totalBasePrice = totalBasePrice;
        this.totalDiscount = totalDiscount;
        this.totalCGST = totalCGST;
        this.totalSGST = totalSGST;
        this.grandTotal = grandTotal;
    }

    // Generate invoice number in the format INV-YYYYMMDDHHMMSS
    public static String generateInvoiceNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "INV-" + LocalDateTime.now().format(formatter);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public double getTotalBasePrice() {
        return totalBasePrice;
    }

    public void setTotalBasePrice(double totalBasePrice) {
        this.totalBasePrice = totalBasePrice;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotalCGST() {
        return totalCGST;
    }

    public void setTotalCGST(double totalCGST) {
        this.totalCGST = totalCGST;
    }

    public double getTotalSGST() {
        return totalSGST;
    }

    public void setTotalSGST(double totalSGST) {
        this.totalSGST = totalSGST;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Calculate invoice details based on order items
    public void calculateInvoiceDetails() {
        if (this.order != null && !this.order.getOrderItems().isEmpty()) {
            double totalBasePrice = 0, totalDiscount = 0, totalCGST = 0, totalSGST = 0;

            for (OrderItem item : this.order.getOrderItems()) {
                totalBasePrice += item.getProduct().getBasePrice() * item.getQuantity();
                totalDiscount += item.getDiscountAmount();
                totalCGST += item.getCgstAmount();
                totalSGST += item.getSgstAmount();
            }

            this.grandTotal = totalBasePrice - totalDiscount + totalCGST + totalSGST;
            this.totalBasePrice = totalBasePrice;
            this.totalDiscount = totalDiscount;
            this.totalCGST = totalCGST;
            this.totalSGST = totalSGST;
        } else {
            throw new IllegalStateException("Order has no items or is not properly set.");
        }
    }

    @Override
    public String toString() {
        return "Invoice [id=" + id + ", invoiceNumber=" + invoiceNumber + ", order=" + 
               (order != null ? order.getOrderId() : "No Order") + ", totalBasePrice=" + totalBasePrice + 
               ", totalDiscount=" + totalDiscount + ", totalCGST=" + totalCGST + 
               ", totalSGST=" + totalSGST + ", grandTotal=" + grandTotal + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
