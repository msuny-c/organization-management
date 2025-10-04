package com.example.organization.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "organization")
public class Organization {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;
    
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_address_id")
    private Address officialAddress;
    
    @Positive
    @Column(name = "annual_turnover")
    private Integer annualTurnover;
    
    @Min(0)
    @Column(name = "employees_count", nullable = false)
    private int employeesCount;
    
    @Positive
    @Column(nullable = false)
    private long rating;
    
    @Column(name = "full_name", unique = true)
    private String fullName;
    
    @Enumerated(EnumType.STRING)
    private OrganizationType type;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postal_address_id", nullable = false)
    private Address postalAddress;
    
    @Version
    private Long version;
    
    public Organization() {}
    
    @PrePersist
    public void onCreate() {
        this.creationDate = LocalDate.now();
    }
    
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
    
    public Coordinates getCoordinates() {
        return coordinates;
    }
    
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
    public LocalDate getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
    
    public Address getOfficialAddress() {
        return officialAddress;
    }
    
    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }
    
    public Integer getAnnualTurnover() {
        return annualTurnover;
    }
    
    public void setAnnualTurnover(Integer annualTurnover) {
        this.annualTurnover = annualTurnover;
    }
    
    public int getEmployeesCount() {
        return employeesCount;
    }
    
    public void setEmployeesCount(int employeesCount) {
        this.employeesCount = employeesCount;
    }
    
    public long getRating() {
        return rating;
    }
    
    public void setRating(long rating) {
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
    
    public Address getPostalAddress() {
        return postalAddress;
    }
    
    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
}
