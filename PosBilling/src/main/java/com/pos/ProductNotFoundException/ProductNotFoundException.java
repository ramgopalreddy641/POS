package com.pos.ProductNotFoundException;


public class ProductNotFoundException extends RuntimeException {

   
    public ProductNotFoundException(String message) {
        super(message); 
    }
}