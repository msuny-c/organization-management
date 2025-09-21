package com.example.organization.controller;

import com.example.organization.dto.*;
import com.example.organization.model.OrganizationType;
import com.example.organization.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/organizations")
public class OrganizationController {
    
    private final OrganizationService organizationService;
    private final LocationService locationService;
    private final CoordinatesService coordinatesService;
    private final AddressService addressService;
    
    @Autowired
    public OrganizationController(
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
    public String listOrganizations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(required = false) String search,
            Model model) {
        
        Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        
        Page<OrganizationDto> organizations;
        if (search != null && !search.trim().isEmpty()) {
            organizations = organizationService.findBySearchTerm(search, pageable);
            model.addAttribute("search", search);
        } else {
            organizations = organizationService.findAll(pageable);
        }
        
        model.addAttribute("organizations", organizations);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sort);
        model.addAttribute("sortDir", dir);
        model.addAttribute("reverseSortDir", dir.equals("asc") ? "desc" : "asc");
        
        return "organizations/list";
    }
    
    @GetMapping("/{id}")
    public String viewOrganization(@PathVariable Long id, Model model) {
        OrganizationDto organization = organizationService.findById(id);
        model.addAttribute("organization", organization);
        return "organizations/view";
    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("organization", new OrganizationDto());
        addFormAttributes(model);
        return "organizations/create";
    }
    
    @PostMapping("/create")
    public String createOrganization(
            @Valid @ModelAttribute("organization") OrganizationDto organizationDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(err -> {
                System.err.println("VALIDATION ERROR: " + err.toString());
            });
            addFormAttributes(model);
            return "organizations/create";
        }
        
        try {
            OrganizationDto created = organizationService.create(organizationDto);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Организация \"" + created.getName() + "\" успешно создана");
            return "redirect:/organizations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при создании организации: " + e.getMessage());
            addFormAttributes(model);
            return "organizations/create";
        }
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        OrganizationDto organization = organizationService.findById(id);
        model.addAttribute("organization", organization);
        addFormAttributes(model);
        return "organizations/edit";
    }
    
    @PostMapping("/{id}/edit")
    public String updateOrganization(
            @PathVariable Long id,
            @Valid @ModelAttribute("organization") OrganizationDto organizationDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            addFormAttributes(model);
            return "organizations/edit";
        }
        
        try {
            OrganizationDto updated = organizationService.update(id, organizationDto);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Организация \"" + updated.getName() + "\" успешно обновлена");
            return "redirect:/organizations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при обновлении организации: " + e.getMessage());
            addFormAttributes(model);
            return "organizations/edit";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteOrganization(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            organizationService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Организация успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении организации: " + e.getMessage());
        }
        return "redirect:/organizations";
    }
    
    private void addFormAttributes(Model model) {
        List<LocationDto> locations = locationService.findAll();
        List<CoordinatesDto> coordinates = coordinatesService.findAll();
        List<AddressDto> addresses = addressService.findAll();
        
        model.addAttribute("locations", locations);
        model.addAttribute("coordinates", coordinates);
        model.addAttribute("addresses", addresses);
        model.addAttribute("organizationTypes", OrganizationType.values());
    }
}
