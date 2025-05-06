package com.pos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    
      
    @Id
    @Column(nullable = false, unique = true)
    private Long productId;  

    @Column(nullable = false)
    private String productName; 
    
    @Column(nullable = false)
    private Double basePrice;  

    private Integer quantity; 
    
    private Double discount; 

    private Double cgst;  

    private Double sgst; 

    private Double finalPrice; 

   
    public Product() {}

   
    public Product( Long productId, String productName, Double basePrice, Integer quantity,
                   Double discount, Double cgst, Double sgst, Double finalPrice) {
        
        this.productId = productId;  
        this.productName = productName;
        this.basePrice = basePrice;
        this.quantity = quantity;
        this.discount = discount;
        this.cgst = cgst;
        this.sgst = sgst;
        this.finalPrice = finalPrice;
    }

   
 

    public Long getProductId() {
        return productId;  
    }

    public void setProductId(Long productId) {  
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getCgst() {
        return cgst;
    }

    public void setCgst(Double cgst) {
        this.cgst = cgst;
    }

    public Double getSgst() {
        return sgst;
    }

    public void setSgst(Double sgst) {
        this.sgst = sgst;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    
    @Override
    public String toString() {
        return "Product [ productId=" + productId + ", productName=" + productName + ", basePrice=" + basePrice +
                ", quantity=" + quantity + ", discount=" + discount + ", cgst=" + cgst + ", sgst=" + sgst + ", finalPrice=" + finalPrice + "]";
    }
}
