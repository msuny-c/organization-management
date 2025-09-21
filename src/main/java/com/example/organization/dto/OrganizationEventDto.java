package com.example.organization.dto;

public class OrganizationEventDto {
    
    public enum EventType {
        CREATED, UPDATED, DELETED, ABSORBED, DISMISSED
    }
    
    private EventType eventType;
    private OrganizationDto organization;
    private Long organizationId;
    
    public OrganizationEventDto() {}
    
    public OrganizationEventDto(EventType eventType, OrganizationDto organization) {
        this.eventType = eventType;
        this.organization = organization;
        this.organizationId = organization != null ? organization.getId() : null;
    }
    
    public OrganizationEventDto(EventType eventType, Long organizationId) {
        this.eventType = eventType;
        this.organizationId = organizationId;
    }
    
    public EventType getEventType() {
        return eventType;
    }
    
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    
    public OrganizationDto getOrganization() {
        return organization;
    }
    
    public void setOrganization(OrganizationDto organization) {
        this.organization = organization;
    }
    
    public Long getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
