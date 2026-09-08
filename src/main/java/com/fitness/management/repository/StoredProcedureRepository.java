package com.fitness.management.repository;

import com.fitness.management.exception.BusinessRuleException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.StoredProcedureQuery;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

/**
 * Calls existing MySQL stored procedures. Business rules stay in the database.
 * SIGNAL SQLSTATE '45000' is translated into BusinessRuleException.
 */
@Repository
public class StoredProcedureRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void registerMember(
            String firstName,
            String lastName,
            String emailId,
            String password,
            String phoneNo,
            LocalDate dateOfBirth) {
        execute(HttpStatus.CONFLICT, () -> {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("MemberRegistration");
            query.registerStoredProcedureParameter("p_first_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_last_name", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_email_id", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_password", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_phone_no", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_date_of_birth", LocalDate.class, ParameterMode.IN);
            query.setParameter("p_first_name", firstName);
            query.setParameter("p_last_name", lastName);
            query.setParameter("p_email_id", emailId);
            query.setParameter("p_password", password);
            query.setParameter("p_phone_no", phoneNo);
            query.setParameter("p_date_of_birth", dateOfBirth);
            query.execute();
            return null;
        });
    }

    public Integer login(String emailId, String password) {
        return execute(HttpStatus.UNAUTHORIZED, () -> {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("MemberLogin");
            query.registerStoredProcedureParameter("p_email_id", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_password", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_member_id", Integer.class, ParameterMode.OUT);
            query.setParameter("p_email_id", emailId);
            query.setParameter("p_password", password);
            query.execute();
            return (Integer) query.getOutputParameterValue("p_member_id");
        });
    }

    public Integer addSubscription(Integer memberId, Integer planId) {
        return execute(HttpStatus.CONFLICT, () -> {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("AddSubscription");
            query.registerStoredProcedureParameter("p_member_id", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_plan_id", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_subscription_id", Integer.class, ParameterMode.OUT);
            query.setParameter("p_member_id", memberId);
            query.setParameter("p_plan_id", planId);
            query.execute();
            return (Integer) query.getOutputParameterValue("p_subscription_id");
        });
    }

    public PurchaseProcedureResult purchaseSubscription(Integer subscriptionId, String paymentMethod) {
        return execute(HttpStatus.CONFLICT, () -> {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("PurchaseSubscriptionPlan");
            query.registerStoredProcedureParameter("p_subscription_id", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_payment_method", String.class, ParameterMode.IN);
            query.setParameter("p_subscription_id", subscriptionId);
            query.setParameter("p_payment_method", paymentMethod);
            query.execute();
            return readPurchaseResult(query.getResultList());
        });
    }

    public void bookSession(Integer memberId, Integer sessionId) {
        execute(HttpStatus.CONFLICT, () -> {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("BookSession");
            query.registerStoredProcedureParameter("p_member_id", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_session_id", Integer.class, ParameterMode.IN);
            query.setParameter("p_member_id", memberId);
            query.setParameter("p_session_id", sessionId);
            query.execute();
            return null;
        });
    }

    public void cancelSession(Integer sessionId) {
        execute(HttpStatus.CONFLICT, () -> {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CancelSession");
            query.registerStoredProcedureParameter("p_session_id", Integer.class, ParameterMode.IN);
            query.setParameter("p_session_id", sessionId);
            query.execute();
            return null;
        });
    }

    private PurchaseProcedureResult readPurchaseResult(List<?> rows) {
        if (rows == null || rows.isEmpty()) {
            return new PurchaseProcedureResult(null, null, "Purchase completed");
        }
        Object first = rows.get(0);
        if (first instanceof Object[] columns && columns.length >= 3) {
            Integer paymentId = columns[0] == null ? null : ((Number) columns[0]).intValue();
            Integer subscriptionId = columns[1] == null ? null : ((Number) columns[1]).intValue();
            String message = String.valueOf(columns[2]);
            return new PurchaseProcedureResult(paymentId, subscriptionId, message);
        }
        return new PurchaseProcedureResult(null, null, "Purchase completed");
    }

    private <T> T execute(HttpStatus businessStatus, ProcedureCall<T> call) {
        try {
            return call.run();
        } catch (PersistenceException exception) {
            throw translate(exception, businessStatus);
        }
    }

    private RuntimeException translate(PersistenceException exception, HttpStatus businessStatus) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof SQLException sqlException && "45000".equals(sqlException.getSQLState())) {
                return new BusinessRuleException(sqlException.getMessage(), businessStatus);
            }
            current = current.getCause();
        }
        return exception;
    }

    @FunctionalInterface
    private interface ProcedureCall<T> {
        T run();
    }

    public record PurchaseProcedureResult(Integer paymentId, Integer subscriptionId, String message) {
    }
}
