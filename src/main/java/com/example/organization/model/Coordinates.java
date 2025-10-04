package com.example.organization.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "coordinates")
public class Coordinates {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Max(882)
    @Column(nullable = false)
    private Double x;
    
    @NotNull
    @Min(-539)
    @Column(nullable = false)
    private long y;
    
    public Coordinates() {}
    
    public Coordinates(Double x, long y) {
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
    
    public long getY() {
        return y;
    }
    
    public void setY(long y) {
        this.y = y;
    }
}
