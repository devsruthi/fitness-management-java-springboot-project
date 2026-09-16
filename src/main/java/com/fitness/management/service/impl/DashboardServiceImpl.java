package com.fitness.management.service.impl;

import com.fitness.management.dto.response.CancelledSessionStatsResponse;
import com.fitness.management.dto.response.MonthlyRevenueResponse;
import com.fitness.management.dto.response.ServiceTypePopularityResponse;
import com.fitness.management.dto.response.SessionResponse;
import com.fitness.management.dto.response.YearlyRevenueResponse;
import com.fitness.management.entity.enums.BookingStatus;
import com.fitness.management.entity.enums.ServiceMode;
import com.fitness.management.entity.enums.ServiceTypeStatus;
import com.fitness.management.exception.BusinessRuleException;
import com.fitness.management.repository.BookingRepository;
import com.fitness.management.repository.PaymentRepository;
import com.fitness.management.repository.SessionRepository;
import com.fitness.management.service.DashboardService;
import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final BookingRepository bookingRepository;
    private final SessionRepository sessionRepository;
    private final PaymentRepository paymentRepository;

    public DashboardServiceImpl(
            BookingRepository bookingRepository,
            SessionRepository sessionRepository,
            PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.sessionRepository = sessionRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceTypePopularityResponse> listMostBookedServiceTypes() {
        List<ServiceTypePopularityResponse> results = new ArrayList<>();
        for (Object[] row : bookingRepository.countBookingsByServiceType()) {
            results.add(new ServiceTypePopularityResponse(
                    toInt(row[0]),
                    (String) row[1],
                    (String) row[2],
                    (ServiceMode) row[3],
                    toInt(row[4]),
                    (ServiceTypeStatus) row[5],
                    toLong(row[6])));
        }
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CancelledSessionStatsResponse> listMostCancelledSessions() {
        List<CancelledSessionStatsResponse> results = new ArrayList<>();
        for (Object[] row : bookingRepository.countBookingsBySessionAndStatus(BookingStatus.CANCELLED)) {
            Integer sessionId = toInt(row[0]);
            sessionRepository.findWithDetailsBySessionId(sessionId).ifPresent(session -> results.add(
                    new CancelledSessionStatsResponse(SessionResponse.from(session), toLong(row[1]))));
        }
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyRevenueResponse> listMonthlyRevenue(Integer year, Integer month) {
        if (month != null && (month < 1 || month > 12)) {
            throw new BusinessRuleException("month must be between 1 and 12", HttpStatus.BAD_REQUEST);
        }

        List<MonthlyRevenueResponse> results = new ArrayList<>();
        for (Object[] row : paymentRepository.sumSuccessfulPaymentsByMonth()) {
            int revenueYear = toInt(row[0]);
            int revenueMonth = toInt(row[1]);
            if (year != null && revenueYear != year) {
                continue;
            }
            if (month != null && revenueMonth != month) {
                continue;
            }
            results.add(new MonthlyRevenueResponse(
                    revenueYear,
                    revenueMonth,
                    Month.of(revenueMonth).getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                    toMoney(row[2]),
                    toLong(row[3])));
        }
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<YearlyRevenueResponse> listYearlyRevenue(Integer year) {
        List<YearlyRevenueResponse> results = new ArrayList<>();
        for (Object[] row : paymentRepository.sumSuccessfulPaymentsByYear()) {
            int revenueYear = toInt(row[0]);
            if (year != null && revenueYear != year) {
                continue;
            }
            results.add(new YearlyRevenueResponse(revenueYear, toMoney(row[1]), toLong(row[2])));
        }
        return results;
    }

    private static int toInt(Object value) {
        return ((Number) value).intValue();
    }

    private static long toLong(Object value) {
        return ((Number) value).longValue();
    }

    private static BigDecimal toMoney(Object value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(2);
        }
        if (value instanceof BigDecimal amount) {
            return amount;
        }
        return new BigDecimal(value.toString());
    }
}
