package com.inventorymanagement.service;

import com.inventorymanagement.DTO.ItemDTO;
import com.inventorymanagement.entity.Item;
import com.inventorymanagement.entity.Vendor;
import com.inventorymanagement.repository.ItemRepository;
import com.inventorymanagement.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllItemDTOs_Success() {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName("Test Vendor");

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setVendor(vendor);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setVendor(vendor);

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        List<ItemDTO> items = itemService.findAllItemDTOs();

        assertEquals(2, items.size());
        assertEquals("Item 1", items.get(0).getName());
        assertEquals("Item 2", items.get(1).getName());
    }

    @Test
    void findItemByIdDTOs_Success() {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName("Test Vendor");

        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setVendor(vendor);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Optional<ItemDTO> foundItem = itemService.findItemByIdDTOs(1L);

        assertTrue(foundItem.isPresent());
        assertEquals("Test Item", foundItem.get().getName());
    }

    @Test
    void updateItem_Success() {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName("Test Vendor");

        Item existingItem = new Item();
        existingItem.setId(1L);
        existingItem.setName("Old Name");
        existingItem.setVendor(vendor);

        Item updatedItem = new Item();
        updatedItem.setId(1L);
        updatedItem.setName("New Name");
        updatedItem.setVendor(vendor);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenReturn(updatedItem);

        Item result = itemService.updateItem(updatedItem);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
    }
}