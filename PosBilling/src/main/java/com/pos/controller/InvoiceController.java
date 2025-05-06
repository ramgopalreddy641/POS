package com.pos.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pos.model.Invoice;
import com.pos.service.InvoiceService;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin("*")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/{orderId}")
    public Invoice generateInvoice(@PathVariable String orderId) {
        return invoiceService.generateInvoice(orderId);
    }
    
    @GetMapping(value = "/{invoiceNumber}", produces = "application/json")
    public Invoice getInvoiceByInvoiceNumber(@PathVariable String invoiceNumber) {
        return invoiceService.getInvoiceByInvoiceNumber(invoiceNumber);
    }


}
