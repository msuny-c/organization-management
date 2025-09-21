package com.example.organization.service;

import com.example.organization.dto.OrganizationDto;
import com.example.organization.dto.OrganizationEventDto;
import com.example.organization.dto.OrganizationEventDto.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketEventService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    public WebSocketEventService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    
    public void notifyOrganizationCreated(OrganizationDto organization) {
        OrganizationEventDto event = new OrganizationEventDto(EventType.CREATED, organization);
        messagingTemplate.convertAndSend("/topic/organizations", event);
    }
    
    public void notifyOrganizationUpdated(OrganizationDto organization) {
        OrganizationEventDto event = new OrganizationEventDto(EventType.UPDATED, organization);
        messagingTemplate.convertAndSend("/topic/organizations", event);
    }
    
    public void notifyOrganizationDeleted(Long organizationId) {
        OrganizationEventDto event = new OrganizationEventDto(EventType.DELETED, organizationId);
        messagingTemplate.convertAndSend("/topic/organizations", event);
    }
    
    public void notifyOrganizationAbsorbed(OrganizationDto absorbingOrganization, Long absorbedId) {
        OrganizationEventDto absorbedEvent = new OrganizationEventDto(EventType.DELETED, absorbedId);
        OrganizationEventDto updatedEvent = new OrganizationEventDto(EventType.UPDATED, absorbingOrganization);
        
        messagingTemplate.convertAndSend("/topic/organizations", absorbedEvent);
        messagingTemplate.convertAndSend("/topic/organizations", updatedEvent);
    }
    
    public void notifyEmployeesDismissed(OrganizationDto organization) {
        OrganizationEventDto event = new OrganizationEventDto(EventType.DISMISSED, organization);
        messagingTemplate.convertAndSend("/topic/organizations", event);
    }
}
