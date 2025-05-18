package com.inventorymanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventorymanagement.entity.Vendor;
import com.inventorymanagement.repository.VendorRepository;

@Service
public class VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public Optional<Vendor> findVendorById(Long id) {
        return vendorRepository.findById(id);
    }

    public List<Vendor> findAllVendors() {
        return vendorRepository.findAll();
    }

    public Optional<Vendor> updateVendor(Long id, Vendor vendorDetails) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()) {
            Vendor existingVendor = vendorOptional.get();
            existingVendor.setName(vendorDetails.getName());
            existingVendor.setContactPerson(vendorDetails.getContactPerson());
            existingVendor.setEmail(vendorDetails.getEmail());
            existingVendor.setPhone(vendorDetails.getPhone());
            existingVendor.setAddress(vendorDetails.getAddress());

            return Optional.of(vendorRepository.save(existingVendor));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteVendor(Long id) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isPresent()) {
            vendorRepository.deleteById(id); // Delete the vendor from DB
            return true;
        } else {
            return false;
        }
    }

}
