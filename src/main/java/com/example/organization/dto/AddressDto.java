package com.example.organization.dto;

import jakarta.validation.constraints.Size;

public class AddressDto {
    
    private Long id;
    
    @Size(min = 7, message = "Почтовый индекс должен содержать минимум 7 символов")
    private String zipCode;
    
    private Long townId;
    
    private LocationDto town;
    
    public AddressDto() {}
    
    public AddressDto(Long id, String zipCode, Long townId, LocationDto town) {
        this.id = id;
        this.zipCode = zipCode;
        this.townId = townId;
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
    
    public Long getTownId() {
        return townId;
    }
    
    public void setTownId(Long townId) {
        this.townId = townId;
    }
    
    public LocationDto getTown() {
        return town;
    }
    
    public void setTown(LocationDto town) {
        this.town = town;
    }
}
