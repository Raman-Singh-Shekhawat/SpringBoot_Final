package com.inventorymanagement.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inventorymanagement.DTO.VendorDTO;
import com.inventorymanagement.entity.Vendor;
import com.inventorymanagement.service.VendorService;

@RestController
@RequestMapping("/api/vendors")
@CrossOrigin(origins = "*")
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@Autowired(required = false)
	private ModelMapper modelMapper;

	private VendorDTO convertToDto(Vendor vendor) {
		if (modelMapper != null) {
			return modelMapper.map(vendor, VendorDTO.class);
		}
		VendorDTO vendorDto = new VendorDTO();
		vendorDto.setId(vendor.getId());
		vendorDto.setName(vendor.getName());
		vendorDto.setContactPerson(vendor.getContactPerson());
		vendorDto.setEmail(vendor.getEmail());
		vendorDto.setPhone(vendor.getPhone());
		vendorDto.setAddress(vendor.getAddress());
		return vendorDto;
	}

	private Vendor convertToEntity(VendorDTO vendorDto) {
		if (modelMapper != null) {
			return modelMapper.map(vendorDto, Vendor.class);
		}
		Vendor vendor = new Vendor();
		vendor.setId(vendorDto.getId());
		vendor.setName(vendorDto.getName());
		vendor.setContactPerson(vendorDto.getContactPerson());
		vendor.setEmail(vendorDto.getEmail());
		vendor.setPhone(vendorDto.getPhone());
		vendor.setAddress(vendorDto.getAddress());
		return vendor;
	}

	@PostMapping
	public ResponseEntity<VendorDTO> createVendor(@RequestBody VendorDTO vendorDto) {
		Vendor vendorToCreate = convertToEntity(vendorDto);
		Vendor createdVendor = vendorService.createVendor(vendorToCreate);
		return new ResponseEntity<>(convertToDto(createdVendor), HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<VendorDTO> getVendorById(@PathVariable Long id) {
		Optional<Vendor> vendorOptional = vendorService.findVendorById(id);
		return vendorOptional.map(this::convertToDto).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<VendorDTO>> getAllVendors() {
		List<Vendor> vendors = vendorService.findAllVendors();
		List<VendorDTO> vendorDtos = vendors.stream().map(this::convertToDto).collect(Collectors.toList());
		return ResponseEntity.ok(vendorDtos);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VendorDTO> updateVendor(@PathVariable Long id, @RequestBody VendorDTO vendorDto) {
		Optional<Vendor> existingVendor = vendorService.findVendorById(id);
		if (existingVendor.isPresent()) {
			Vendor vendorToUpdate = convertToEntity(vendorDto);
			vendorToUpdate.setId(id);
			Optional<Vendor> updatedVendor = vendorService.updateVendor(id, vendorToUpdate);
			return updatedVendor.map(this::convertToDto).map(ResponseEntity::ok)
					.orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
		Optional<Vendor> existingVendor = vendorService.findVendorById(id);
		if (existingVendor.isPresent()) {
			boolean deleted = vendorService.deleteVendor(id);
			if (deleted) {
				return ResponseEntity.noContent().build(); // Successfully deleted
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Deletion failed
			}
		} else {
			return ResponseEntity.notFound().build(); // Vendor not found
		}
	}

}
