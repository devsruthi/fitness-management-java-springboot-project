package com.fitness.management.entity;

import com.fitness.management.entity.enums.ServiceMode;
import com.fitness.management.entity.enums.ServiceTypeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Service_Types")
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_type_id")
    private Integer serviceTypeId;

    @Column(name = "service_type_name", nullable = false, unique = true, length = 100)
    private String serviceTypeName;

    @Column(name = "service_type_description", nullable = false, length = 250)
    private String serviceTypeDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_mode", nullable = false)
    private ServiceMode serviceMode;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type_status", nullable = false)
    private ServiceTypeStatus serviceTypeStatus;

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getServiceTypeName() {
        return serviceTypeName;
    }

    public void setServiceTypeName(String serviceTypeName) {
        this.serviceTypeName = serviceTypeName;
    }

    public String getServiceTypeDescription() {
        return serviceTypeDescription;
    }

    public void setServiceTypeDescription(String serviceTypeDescription) {
        this.serviceTypeDescription = serviceTypeDescription;
    }

    public ServiceMode getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(ServiceMode serviceMode) {
        this.serviceMode = serviceMode;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public ServiceTypeStatus getServiceTypeStatus() {
        return serviceTypeStatus;
    }

    public void setServiceTypeStatus(ServiceTypeStatus serviceTypeStatus) {
        this.serviceTypeStatus = serviceTypeStatus;
    }
}
