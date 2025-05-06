package com.pos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pos.model.Invoice;
import com.pos.model.Order;
import com.pos.model.OrderItem;
import com.pos.repo.InvoiceRepository;
import com.pos.repo.OrderRepository;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Invoice generateInvoice(String orderId) {
        // Fetch the order by ID
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new RuntimeException("Order not found with id: " + orderId);
        }

        // Initialize variables to calculate totals
        double totalBasePrice = 0, totalDiscount = 0, totalCGST = 0, totalSGST = 0;
        StringBuilder productNames = new StringBuilder(); 

        // Loop through order items and calculate totals
        for (OrderItem item : order.getOrderItems()) {
            if (item.getProduct() == null || item.getProduct().getProductId() == null) {
                throw new RuntimeException("Invalid product in order item with orderId: " + orderId);
            }

            // Safely retrieve values
            double basePrice = item.getProduct().getBasePrice() != null ? item.getProduct().getBasePrice() : 0;
            double discountAmount = item.getDiscountAmount() != null ? item.getDiscountAmount() : 0;
            double cgstAmount = item.getCgstAmount() != null ? item.getCgstAmount() : 0;
            double sgstAmount = item.getSgstAmount() != null ? item.getSgstAmount() : 0;

            // Accumulate totals
            totalBasePrice += basePrice * item.getQuantity();
            totalDiscount += discountAmount;
            totalCGST += cgstAmount;
            totalSGST += sgstAmount;

            // Build the product names list
            if (productNames.length() > 0) {
                productNames.append(", ");
            }
            productNames.append(item.getProduct().getProductName());
        }

        // Calculate the grand total
        double grandTotal = totalBasePrice - totalDiscount + totalCGST + totalSGST;

        // Create the invoice object without the lastInvoiceId, using only the required constructor
        Invoice invoice = new Invoice(order, totalBasePrice, totalDiscount, totalCGST, totalSGST, grandTotal);

        // Set the product names in the invoice
        invoice.setProductNames(productNames.toString());

        // Save and return the invoice
        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceByInvoiceNumber(String invoiceNumber) {
        // Retrieve an invoice by its invoice number
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }
}
