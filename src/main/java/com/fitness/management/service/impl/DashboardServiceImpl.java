package com.fitness.management.service.impl;

import com.fitness.management.dto.response.CancelledSessionStatsResponse;
import com.fitness.management.dto.response.MonthlyRevenueResponse;
import com.fitness.management.dto.response.ServiceTypePopularityResponse;
import com.fitness.management.dto.response.SessionResponse;
import com.fitness.management.dto.response.YearlyRevenueResponse;
import com.fitness.management.entity.Booking;
import com.fitness.management.entity.Payment;
import com.fitness.management.entity.ServiceType;
import com.fitness.management.entity.enums.BookingStatus;
import com.fitness.management.entity.enums.PaymentStatus;
import com.fitness.management.exception.BusinessRuleException;
import com.fitness.management.repository.BookingRepository;
import com.fitness.management.repository.PaymentRepository;
import com.fitness.management.service.DashboardService;
import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    public DashboardServiceImpl(BookingRepository bookingRepository, PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceTypePopularityResponse> listMostBookedServiceTypes() {
        Map<Integer, ServiceTypePopularityResponse> byType = new LinkedHashMap<>();
        for (Booking booking : bookingRepository.findAllWithDetails()) {
            ServiceType serviceType = booking.getSession().getServiceType();
            ServiceTypePopularityResponse current = byType.get(serviceType.getServiceTypeId());
            long count = current == null ? 1L : current.bookingCount() + 1L;
            byType.put(serviceType.getServiceTypeId(), new ServiceTypePopularityResponse(
                    serviceType.getServiceTypeId(),
                    serviceType.getServiceTypeName(),
                    serviceType.getServiceTypeDescription(),
                    serviceType.getServiceMode(),
                    serviceType.getMaxParticipants(),
                    serviceType.getServiceTypeStatus(),
                    count));
        }
        return byType.values().stream()
                .sorted(Comparator.comparingLong(ServiceTypePopularityResponse::bookingCount).reversed())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancelledSessionStatsResponse> listMostCancelledSessions() {
        Map<Integer, Long> cancelledCounts = new LinkedHashMap<>();
        Map<Integer, SessionResponse> sessions = new LinkedHashMap<>();
        for (Booking booking : bookingRepository.findAllWithDetails()) {
            if (booking.getBookingStatus() != BookingStatus.CANCELLED) {
                continue;
            }
            Integer sessionId = booking.getSession().getSessionId();
            cancelledCounts.merge(sessionId, 1L, Long::sum);
            sessions.putIfAbsent(sessionId, SessionResponse.from(booking.getSession()));
        }
        return cancelledCounts.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(entry -> new CancelledSessionStatsResponse(sessions.get(entry.getKey()), entry.getValue()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyRevenueResponse> listMonthlyRevenue(Integer year, Integer month) {
        if (month != null && (month < 1 || month > 12)) {
            throw new BusinessRuleException("month must be between 1 and 12", HttpStatus.BAD_REQUEST);
        }

        Map<String, MonthlyRevenueResponse> totals = new LinkedHashMap<>();
        for (Payment payment : paymentRepository.findByPaymentStatus(PaymentStatus.SUCCESS)) {
            int paymentYear = payment.getPaymentDate().getYear();
            int paymentMonth = payment.getPaymentDate().getMonthValue();
            if (year != null && paymentYear != year) {
                continue;
            }
            if (month != null && paymentMonth != month) {
                continue;
            }
            String key = paymentYear + "-" + paymentMonth;
            BigDecimal amount = payment.getPaymentAmount() == null ? BigDecimal.ZERO : payment.getPaymentAmount();
            MonthlyRevenueResponse current = totals.get(key);
            if (current == null) {
                totals.put(key, new MonthlyRevenueResponse(
                        paymentYear,
                        paymentMonth,
                        Month.of(paymentMonth).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        amount,
                        1L));
            } else {
                totals.put(key, new MonthlyRevenueResponse(
                        current.year(),
                        current.month(),
                        current.monthName(),
                        current.totalRevenue().add(amount),
                        current.paymentCount() + 1L));
            }
        }
        return new ArrayList<>(totals.values());
    }

    @Override
    @Transactional(readOnly = true)
    public List<YearlyRevenueResponse> listYearlyRevenue(Integer year) {
        Map<Integer, YearlyRevenueResponse> totals = new LinkedHashMap<>();
        for (Payment payment : paymentRepository.findByPaymentStatus(PaymentStatus.SUCCESS)) {
            int paymentYear = payment.getPaymentDate().getYear();
            if (year != null && paymentYear != year) {
                continue;
            }
            BigDecimal amount = payment.getPaymentAmount() == null ? BigDecimal.ZERO : payment.getPaymentAmount();
            YearlyRevenueResponse current = totals.get(paymentYear);
            if (current == null) {
                totals.put(paymentYear, new YearlyRevenueResponse(paymentYear, amount, 1L));
            } else {
                totals.put(paymentYear, new YearlyRevenueResponse(
                        paymentYear,
                        current.totalRevenue().add(amount),
                        current.paymentCount() + 1L));
            }
        }
        return new ArrayList<>(totals.values());
    }
}
