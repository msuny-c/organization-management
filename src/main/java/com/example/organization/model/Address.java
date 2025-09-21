package com.example.organization.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "address")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(min = 7)
    private String zipCode;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id", nullable = false)
    private Location town;
    
    public Address() {}
    
    public Address(String zipCode, Location town) {
        this.zipCode = zipCode;
        this.town = town;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    
    public Location getTown() {
        return town;
    }
    
    public void setTown(Location town) {
        this.town = town;
    }
}
