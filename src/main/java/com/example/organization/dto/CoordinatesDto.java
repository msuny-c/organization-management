package com.example.organization.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CoordinatesDto {
    
    private Long id;
    
    @NotNull(message = "Координата X не может быть null")
    @Max(value = 882, message = "Координата X не может быть больше 882")
    private Double x;
    
    @NotNull(message = "Координата Y не может быть null")
    @Min(value = -539, message = "Координата Y должна быть больше -540")
    private Long y;
    
    public CoordinatesDto() {}
    
    public CoordinatesDto(Long id, Double x, Long y) {
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
    
    public Double getX() {
        return x;
    }
    
    public void setX(Double x) {
        this.x = x;
    }
    
    public Long getY() {
        return y;
    }
    
    public void setY(Long y) {
        this.y = y;
    }
}
