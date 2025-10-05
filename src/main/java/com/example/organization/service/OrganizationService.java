package com.example.organization.service;

import com.example.organization.dto.OrganizationDto;
import com.example.organization.exception.ResourceNotFoundException;
import com.example.organization.mapper.OrganizationMapper;
import com.example.organization.model.*;
import com.example.organization.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganizationService {
    
    private final OrganizationRepository organizationRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final AddressRepository addressRepository;
    private final LocationRepository locationRepository;
    private final OrganizationMapper mapper;
    private final WebSocketEventService webSocketEventService;
    
    @Autowired
    public OrganizationService(
            OrganizationRepository organizationRepository,
            CoordinatesRepository coordinatesRepository,
            AddressRepository addressRepository,
            LocationRepository locationRepository,
            OrganizationMapper mapper,
            WebSocketEventService webSocketEventService) {
        this.organizationRepository = organizationRepository;
        this.coordinatesRepository = coordinatesRepository;
        this.addressRepository = addressRepository;
        this.locationRepository = locationRepository;
        this.mapper = mapper;
        this.webSocketEventService = webSocketEventService;
    }
    
    @Transactional(readOnly = true)
    public Page<OrganizationDto> findAll(Pageable pageable) {
        return organizationRepository.findAllWithDetails(pageable)
                .map(mapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Page<OrganizationDto> findBySearchTerm(String searchTerm, Pageable pageable) {
        return organizationRepository.findBySearchTerm(searchTerm, pageable)
                .map(mapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public OrganizationDto findById(Long id) {
        Organization organization = organizationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Организация с ID " + id + " не найдена"));
        return mapper.toDto(organization);
    }
    
    public OrganizationDto create(OrganizationDto dto) {
        Organization organization = mapper.toEntity(dto);
        organization.setCreationDate(LocalDate.now());
        
        organization.setCoordinates(getOrCreateCoordinates(dto));
        
        Address postalAddress = getOrCreateAddress(dto.getPostalAddressId(), dto.getPostalAddress());
        organization.setPostalAddress(postalAddress);
        
        if (Boolean.TRUE.equals(dto.getReusePostalAddressAsOfficial())) {
            organization.setOfficialAddress(postalAddress);
        } else if (dto.getOfficialAddressId() != null || dto.getOfficialAddress() != null) {
            organization.setOfficialAddress(getOrCreateAddress(dto.getOfficialAddressId(), dto.getOfficialAddress()));
        }
        
        Organization saved = organizationRepository.save(organization);
        OrganizationDto result = mapper.toDto(saved);
        webSocketEventService.notifyOrganizationCreated(result);
        return result;
    }
    
    public OrganizationDto update(Long id, OrganizationDto dto) {
        Organization existing = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Организация с ID " + id + " не найдена"));
        
        existing.setName(dto.getName());
        existing.setAnnualTurnover(dto.getAnnualTurnover());
        existing.setEmployeesCount(dto.getEmployeesCount());
        existing.setRating(dto.getRating());
        existing.setFullName(dto.getFullName());
        existing.setType(dto.getType());
        
        if (dto.getCoordinatesId() != null || dto.getCoordinates() != null) {
            existing.setCoordinates(getOrCreateCoordinates(dto));
        }
        
        Address postalAddress = null;
        if (dto.getPostalAddressId() != null || dto.getPostalAddress() != null) {
            postalAddress = getOrCreateAddress(dto.getPostalAddressId(), dto.getPostalAddress());
            existing.setPostalAddress(postalAddress);
        }
        
        if (Boolean.TRUE.equals(dto.getReusePostalAddressAsOfficial()) && postalAddress != null) {
            existing.setOfficialAddress(postalAddress);
        } else if (dto.getOfficialAddressId() != null || dto.getOfficialAddress() != null) {
            existing.setOfficialAddress(getOrCreateAddress(dto.getOfficialAddressId(), dto.getOfficialAddress()));
        }
        
        Organization updated = organizationRepository.save(existing);
        OrganizationDto result = mapper.toDto(updated);
        webSocketEventService.notifyOrganizationUpdated(result);
        return result;
    }
    
    public void delete(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Организация с ID " + id + " не найдена"));
        
        Coordinates coordinates = organization.getCoordinates();
        Address officialAddress = organization.getOfficialAddress();
        Address postalAddress = organization.getPostalAddress();
        
        organizationRepository.delete(organization);
        webSocketEventService.notifyOrganizationDeleted(id);
        
        cleanupOrphanedObjects(coordinates, officialAddress, postalAddress);
    }
    
    @Transactional(readOnly = true)
    public OrganizationDto findOneWithMinimalCoordinates() {
        List<Organization> organizations = organizationRepository.findAllOrderedByCoordinatesWithDetails();
        if (organizations.isEmpty()) {
            throw new ResourceNotFoundException("Организации не найдены");
        }
        return mapper.toDto(organizations.get(0));
    }
    
    @Transactional(readOnly = true)
    public Map<Long, Long> groupByRating() {
        List<Object[]> results = organizationRepository.countByRatingGrouped();
        return results.stream()
                .collect(Collectors.toMap(
                    result -> (Long) result[0],
                    result -> (Long) result[1]
                ));
    }
    
    @Transactional(readOnly = true)
    public long countByType(OrganizationType type) {
        return organizationRepository.countByType(type);
    }
    
    public OrganizationDto dismissAllEmployees(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Организация с ID " + id + " не найдена"));
        
        organization.setEmployeesCount(0);
        Organization updated = organizationRepository.save(organization);
        OrganizationDto result = mapper.toDto(updated);
        webSocketEventService.notifyEmployeesDismissed(result);
        return result;
    }
    
    public OrganizationDto absorb(Long absorbingId, Long absorbedId) {
        if (absorbingId.equals(absorbedId)) {
            throw new IllegalArgumentException("Организация не может поглотить саму себя");
        }
        
        Organization absorbing = organizationRepository.findById(absorbingId)
                .orElseThrow(() -> new ResourceNotFoundException("Поглощающая организация с ID " + absorbingId + " не найдена"));
        
        Organization absorbed = organizationRepository.findById(absorbedId)
                .orElseThrow(() -> new ResourceNotFoundException("Поглощаемая организация с ID " + absorbedId + " не найдена"));
        
        absorbing.setEmployeesCount(absorbing.getEmployeesCount() + absorbed.getEmployeesCount());
        
        Coordinates coordinates = absorbed.getCoordinates();
        Address officialAddress = absorbed.getOfficialAddress();
        Address postalAddress = absorbed.getPostalAddress();
        
        organizationRepository.delete(absorbed);
        cleanupOrphanedObjects(coordinates, officialAddress, postalAddress);
        
        Organization updated = organizationRepository.save(absorbing);
        OrganizationDto result = mapper.toDto(updated);
        webSocketEventService.notifyOrganizationAbsorbed(result, absorbedId);
        return result;
    }
    
    private Coordinates getOrCreateCoordinates(OrganizationDto dto) {
        if (dto.getCoordinatesId() != null) {
            return coordinatesRepository.findById(dto.getCoordinatesId())
                    .orElseThrow(() -> new ResourceNotFoundException("Координаты с ID " + dto.getCoordinatesId() + " не найдены"));
        } else if (dto.getCoordinates() != null) {
            Coordinates coordinates = mapper.toEntity(dto.getCoordinates());
            return coordinatesRepository.save(coordinates);
        } else {
            throw new IllegalArgumentException("Необходимо указать координаты");
        }
    }
    
    private Address getOrCreateAddress(Long addressId, com.example.organization.dto.AddressDto addressDto) {
        if (addressId != null) {
            return addressRepository.findById(addressId)
                    .orElseThrow(() -> new ResourceNotFoundException("Адрес с ID " + addressId + " не найден"));
        } else if (addressDto != null && addressDto.getZipCode() != null && addressDto.getZipCode().length() >= 7) {
            Address address = mapper.toEntity(addressDto);
            
            if (addressDto.getTownId() != null) {
                Location town = locationRepository.findById(addressDto.getTownId())
                        .orElseThrow(() -> new ResourceNotFoundException("Локация с ID " + addressDto.getTownId() + " не найдена"));
                address.setTown(town);
            } else if (addressDto.getTown() != null && isLocationValid(addressDto.getTown())) {
                Location town = mapper.toEntity(addressDto.getTown());
                town = locationRepository.save(town);
                address.setTown(town);
            } else {
                throw new IllegalArgumentException("Необходимо указать город для адреса");
            }
            
            return addressRepository.save(address);
        }
        return null;
    }
    
    private boolean isLocationValid(com.example.organization.dto.LocationDto locationDto) {
        return locationDto.getX() != null && 
               locationDto.getY() != null && 
               locationDto.getZ() != null && 
               locationDto.getName() != null && !locationDto.getName().trim().isEmpty();
    }
    
    private void cleanupOrphanedObjects(Coordinates coordinates, Address officialAddress, Address postalAddress) {
        if (coordinates != null) {
            List<Coordinates> orphanedCoordinates = coordinatesRepository.findOrphaned();
            if (orphanedCoordinates.contains(coordinates)) {
                coordinatesRepository.delete(coordinates);
            }
        }
        
        if (officialAddress != null) {
            List<Address> orphanedAddresses = addressRepository.findOrphaned();
            if (orphanedAddresses.contains(officialAddress)) {
                Location town = officialAddress.getTown();
                addressRepository.delete(officialAddress);
                
                if (town != null) {
                    List<Location> orphanedLocations = locationRepository.findOrphaned();
                    if (orphanedLocations.contains(town)) {
                        locationRepository.delete(town);
                    }
                }
            }
        }
        
        if (postalAddress != null && !postalAddress.equals(officialAddress)) {
            List<Address> orphanedAddresses = addressRepository.findOrphaned();
            if (orphanedAddresses.contains(postalAddress)) {
                Location town = postalAddress.getTown();
                addressRepository.delete(postalAddress);
                
                if (town != null) {
                    List<Location> orphanedLocations = locationRepository.findOrphaned();
                    if (orphanedLocations.contains(town)) {
                        locationRepository.delete(town);
                    }
                }
            }
        }
    }
}
