package com.example.organization.controller;

import com.example.organization.dto.OrganizationDto;
import com.example.organization.model.OrganizationType;
import com.example.organization.service.OrganizationService;
import com.example.organization.service.LocationService;
import com.example.organization.service.CoordinatesService;
import com.example.organization.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/organizations")
public class RestOrganizationController {
    
    private final OrganizationService organizationService;
    private final LocationService locationService;
    private final CoordinatesService coordinatesService;
    private final AddressService addressService;
    
    @Autowired
    public RestOrganizationController(
            OrganizationService organizationService,
            LocationService locationService,
            CoordinatesService coordinatesService,
            AddressService addressService) {
        this.organizationService = organizationService;
        this.locationService = locationService;
        this.coordinatesService = coordinatesService;
        this.addressService = addressService;
    }
    
    @GetMapping
    public ResponseEntity<Page<OrganizationDto>> listOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(required = false) String search) {
        
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        
        Page<OrganizationDto> organizations;
        if (search != null && !search.trim().isEmpty()) {
            organizations = organizationService.findBySearchTerm(search, pageable);
        } else {
            organizations = organizationService.findAll(pageable);
        }
        
        return ResponseEntity.ok(organizations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDto> getOrganization(@PathVariable Long id) {
        try {
            OrganizationDto organization = organizationService.findById(id);
            return ResponseEntity.ok(organization);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createOrganization(@Valid @RequestBody OrganizationDto organization) {
        try {
            OrganizationDto created = organizationService.create(organization);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationDto organization) {
        try {
            OrganizationDto updated = organizationService.update(id, organization);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        try {
            organizationService.delete(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Организация успешно удалена");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/coordinates")
    public ResponseEntity<?> getCoordinates() {
        return ResponseEntity.ok(coordinatesService.findAll());
    }
    
    @GetMapping("/addresses")
    public ResponseEntity<?> getAddresses() {
        return ResponseEntity.ok(addressService.findAll());
    }
    
    @GetMapping("/locations")
    public ResponseEntity<?> getLocations() {
        return ResponseEntity.ok(locationService.findAll());
    }
    
    @GetMapping("/types")
    public ResponseEntity<OrganizationType[]> getTypes() {
        return ResponseEntity.ok(OrganizationType.values());
    }
}

