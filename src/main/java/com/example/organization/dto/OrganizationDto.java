package com.example.organization.dto;

import com.example.organization.model.OrganizationType;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class OrganizationDto {
    
    private Long id;
    
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    
    private Long coordinatesId;
    private CoordinatesDto coordinates;
    
    private LocalDateTime creationDate;
    
    private Long officialAddressId;
    private AddressDto officialAddress;
    
    @Positive(message = "Годовой оборот должен быть положительным")
    private Long annualTurnover;
    
    @Min(value = 0, message = "Количество сотрудников не может быть отрицательным")
    private Long employeesCount;
    
    @Positive(message = "Рейтинг должен быть положительным")
    @NotNull(message = "Рейтинг не может быть null")
    private Integer rating;
    
    private String fullName;
    
    private OrganizationType type;
    
    private Long postalAddressId;
    private AddressDto postalAddress;
    
    private Long version;
    
    public OrganizationDto() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Long getCoordinatesId() {
        return coordinatesId;
    }
    
    public void setCoordinatesId(Long coordinatesId) {
        this.coordinatesId = coordinatesId;
    }
    
    public CoordinatesDto getCoordinates() {
        return coordinates;
    }
    
    public void setCoordinates(CoordinatesDto coordinates) {
        this.coordinates = coordinates;
    }
    
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    
    public Long getOfficialAddressId() {
        return officialAddressId;
    }
    
    public void setOfficialAddressId(Long officialAddressId) {
        this.officialAddressId = officialAddressId;
    }
    
    public AddressDto getOfficialAddress() {
        return officialAddress;
    }
    
    public void setOfficialAddress(AddressDto officialAddress) {
        this.officialAddress = officialAddress;
    }
    
    public Long getAnnualTurnover() {
        return annualTurnover;
    }
    
    public void setAnnualTurnover(Long annualTurnover) {
        this.annualTurnover = annualTurnover;
    }
    
    public Long getEmployeesCount() {
        return employeesCount;
    }
    
    public void setEmployeesCount(Long employeesCount) {
        this.employeesCount = employeesCount;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public OrganizationType getType() {
        return type;
    }
    
    public void setType(OrganizationType type) {
        this.type = type;
    }
    
    public Long getPostalAddressId() {
        return postalAddressId;
    }
    
    public void setPostalAddressId(Long postalAddressId) {
        this.postalAddressId = postalAddressId;
    }
    
    public AddressDto getPostalAddress() {
        return postalAddress;
    }
    
    public void setPostalAddress(AddressDto postalAddress) {
        this.postalAddress = postalAddress;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
}
