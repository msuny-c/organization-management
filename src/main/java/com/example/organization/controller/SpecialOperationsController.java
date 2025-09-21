package com.example.organization.controller;

import com.example.organization.dto.OrganizationDto;
import com.example.organization.model.OrganizationType;
import com.example.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/operations")
public class SpecialOperationsController {
    
    private final OrganizationService organizationService;
    
    @Autowired
    public SpecialOperationsController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
    
    @GetMapping
    public String operationsPage(Model model) {
        model.addAttribute("organizationTypes", OrganizationType.values());
        return "operations/index";
    }
    
    @PostMapping("/minimal-coordinates")
    public String findMinimalCoordinates(Model model, RedirectAttributes redirectAttributes) {
        try {
            OrganizationDto organization = organizationService.findOneWithMinimalCoordinates();
            model.addAttribute("minimalOrganization", organization);
            model.addAttribute("organizationTypes", OrganizationType.values());
            return "operations/index";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
            return "redirect:/operations";
        }
    }
    
    @PostMapping("/group-by-rating")
    public String groupByRating(Model model, RedirectAttributes redirectAttributes) {
        try {
            Map<Integer, Long> ratingGroups = organizationService.groupByRating();
            model.addAttribute("ratingGroups", ratingGroups);
            model.addAttribute("organizationTypes", OrganizationType.values());
            return "operations/index";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
            return "redirect:/operations";
        }
    }
    
    @PostMapping("/count-by-type")
    public String countByType(
            @RequestParam OrganizationType type,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            long count = organizationService.countByType(type);
            model.addAttribute("typeCount", count);
            model.addAttribute("selectedType", type);
            model.addAttribute("organizationTypes", OrganizationType.values());
            return "operations/index";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
            return "redirect:/operations";
        }
    }
    
    @PostMapping("/dismiss-employees")
    public String dismissAllEmployees(
            @RequestParam Long organizationId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            OrganizationDto updated = organizationService.dismissAllEmployees(organizationId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Все сотрудники организации \"" + updated.getName() + "\" уволены");
            return "redirect:/operations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
            return "redirect:/operations";
        }
    }
    
    @PostMapping("/absorb")
    public String absorb(
            @RequestParam Long absorbingId,
            @RequestParam Long absorbedId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            OrganizationDto absorbing = organizationService.absorb(absorbingId, absorbedId);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Организация успешно поглощена. Новое количество сотрудников: " + absorbing.getEmployeesCount());
            return "redirect:/operations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: " + e.getMessage());
            return "redirect:/operations";
        }
    }
}
