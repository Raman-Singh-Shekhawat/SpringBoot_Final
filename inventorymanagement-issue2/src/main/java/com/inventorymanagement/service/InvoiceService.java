package com.inventorymanagement.service;

import com.inventorymanagement.DTO.InvoiceDTO;
import com.inventorymanagement.DTO.InvoiceItemDTO;
import com.inventorymanagement.entity.Invoice;
import com.inventorymanagement.entity.InvoiceItem;
import com.inventorymanagement.entity.Item;
import com.inventorymanagement.entity.Vendor;
import com.inventorymanagement.repository.InvoiceRepository;
import com.inventorymanagement.repository.ItemRepository;
import com.inventorymanagement.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public InvoiceDTO createInvoice(List<InvoiceItemDTO> itemDTOs) {
        if (itemDTOs == null || itemDTOs.isEmpty()) {
            throw new RuntimeException("No items provided");
        }

        Invoice invoice = new Invoice();
        
        // Get vendor from first item (assuming all items are from same vendor)
        Long vendorId = itemDTOs.get(0).getVendorId();
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        invoice.setVendor(vendor);

        // Set initial values
        invoice.setStatus("PENDING");
        invoice.setCreatedDate(LocalDateTime.now());
        
        double totalAmount = 0.0;

        // Process each item
        for (InvoiceItemDTO itemDTO : itemDTOs) {
            Item item = itemRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found: " + itemDTO.getItemId()));

            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setItem(item);
            invoiceItem.setQuantity(itemDTO.getQuantity());  // Make sure this line exists
            invoiceItem.setUnitPrice(itemDTO.getUnitPrice());
            invoiceItem.setTotalPrice(itemDTO.getQuantity() * itemDTO.getUnitPrice());
            
            invoice.addItem(invoiceItem);
            totalAmount += invoiceItem.getTotalPrice();
        }

        invoice.setTotalAmount(totalAmount);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(savedInvoice);
    }

    @Transactional
    public InvoiceDTO confirmPayment(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setStatus("PAID");
        invoice.setPaymentDate(LocalDateTime.now());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return convertToDTO(updatedInvoice);
    }

    private InvoiceDTO convertToDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setVendorId(invoice.getVendor().getId());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setStatus(invoice.getStatus());
        dto.setCreatedDate(invoice.getCreatedDate());
        dto.setPaymentDate(invoice.getPaymentDate());

        // Convert invoice items to DTOs
        List<InvoiceItemDTO> itemDTOs = invoice.getItems().stream()
            .map(this::convertToItemDTO)
            .collect(Collectors.toList());
        dto.setItems(itemDTOs);

        return dto;
    }

    private InvoiceItemDTO convertToItemDTO(InvoiceItem invoiceItem) {
        InvoiceItemDTO dto = new InvoiceItemDTO();
        dto.setId(invoiceItem.getId());
        dto.setItemId(invoiceItem.getItem().getId());
        dto.setItemName(invoiceItem.getItem().getName());
        dto.setItemDescription(invoiceItem.getItem().getDescription());
        dto.setQuantity(invoiceItem.getQuantity());
        dto.setUnitPrice(invoiceItem.getUnitPrice());
        dto.setTotalPrice(invoiceItem.getTotalPrice());
        return dto;
    }

    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        return convertToDTO(invoice);
    }
}