package com.example.organization.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "location")
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(nullable = false)
    private Integer x;
    
    @NotNull
    @Column(nullable = false)
    private Float y;
    
    @NotNull
    @Column(nullable = false)
    private Long z;
    
    @NotNull
    @Column(nullable = false)
    private String name;
    
    public Location() {}
    
    public Location(Integer x, Float y, Long z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getX() {
        return x;
    }
    
    public void setX(Integer x) {
        this.x = x;
    }
    
    public Float getY() {
        return y;
    }
    
    public void setY(Float y) {
        this.y = y;
    }
    
    public Long getZ() {
        return z;
    }
    
    public void setZ(Long z) {
        this.z = z;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
