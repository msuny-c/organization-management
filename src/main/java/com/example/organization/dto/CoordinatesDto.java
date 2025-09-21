package com.example.organization.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public class CoordinatesDto {
    
    private Long id;
    
    @NotNull(message = "Координата X не может быть null")
    @Max(value = 882, message = "Координата X не может быть больше 882")
    private Integer x;
    
    @NotNull(message = "Координата Y не может быть null")
    private Double y;
    
    public CoordinatesDto() {}
    
    public CoordinatesDto(Long id, Integer x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
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
    
    public Double getY() {
        return y;
    }
    
    public void setY(Double y) {
        this.y = y;
    }
}
