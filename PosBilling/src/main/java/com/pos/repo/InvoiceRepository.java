package com.pos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pos.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
    @Query("SELECT i FROM Invoice i WHERE i.invoiceNumber = :invoiceNumber")
    Invoice findByInvoiceNumber(@Param("invoiceNumber") String invoiceNumber);

    @Query(value = "SELECT * FROM invoice WHERE invoice_number = :invoiceNumber", nativeQuery = true)
    Invoice findInvoiceByInvoiceNumberNative(@Param("invoiceNumber") String invoiceNumber);
}
