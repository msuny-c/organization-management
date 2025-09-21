package com.example.organization.dto;

import jakarta.validation.constraints.NotNull;

public class LocationDto {
    
    private Long id;
    
    @NotNull(message = "Координата X не может быть null")
    private Integer x;
    
    @NotNull(message = "Координата Y не может быть null")
    private Float y;
    
    @NotNull(message = "Координата Z не может быть null")
    private Long z;
    
    @NotNull(message = "Имя не может быть null")
    private String name;
    
    public LocationDto() {}
    
    public LocationDto(Long id, Integer x, Float y, Long z, String name) {
        this.id = id;
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
