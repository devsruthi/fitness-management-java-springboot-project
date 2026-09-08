package com.fitness.management.entity;

import com.fitness.management.entity.enums.PlanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Subscription_Plans")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;

    @Column(name = "plan_name", nullable = false, unique = true, length = 170)
    private String planName;

    @Column(name = "duration_in_months", nullable = false)
    private Integer durationInMonths;

    @Column(name = "plan_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal planPrice;

    @Column(name = "plan_description", length = 250)
    private String planDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_status", nullable = false)
    private PlanStatus planStatus;

    @Column(name = "group_classes_access", nullable = false)
    private Boolean groupClassesAccess;

    @Column(name = "personal_training_access", nullable = false)
    private Boolean personalTrainingAccess;

    @Column(name = "exclusive_services", nullable = false)
    private Boolean exclusiveServices;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getDurationInMonths() {
        return durationInMonths;
    }

    public void setDurationInMonths(Integer durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public BigDecimal getPlanPrice() {
        return planPrice;
    }

    public void setPlanPrice(BigDecimal planPrice) {
        this.planPrice = planPrice;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public PlanStatus getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(PlanStatus planStatus) {
        this.planStatus = planStatus;
    }

    public Boolean getGroupClassesAccess() {
        return groupClassesAccess;
    }

    public void setGroupClassesAccess(Boolean groupClassesAccess) {
        this.groupClassesAccess = groupClassesAccess;
    }

    public Boolean getPersonalTrainingAccess() {
        return personalTrainingAccess;
    }

    public void setPersonalTrainingAccess(Boolean personalTrainingAccess) {
        this.personalTrainingAccess = personalTrainingAccess;
    }

    public Boolean getExclusiveServices() {
        return exclusiveServices;
    }

    public void setExclusiveServices(Boolean exclusiveServices) {
        this.exclusiveServices = exclusiveServices;
    }
}
