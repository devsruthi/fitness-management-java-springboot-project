package com.fitness.management.dto.response;

import com.fitness.management.entity.Member;
import com.fitness.management.entity.enums.AccountStatus;
import java.time.LocalDate;

public record MemberResponse(
        Integer memberId,
        String firstName,
        String lastName,
        String emailId,
        String phoneNo,
        LocalDate dateOfBirth,
        AccountStatus accountStatus,
        LocalDate joiningDate
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getMemberId(),
                member.getFirstName(),
                member.getLastName(),
                member.getEmailId(),
                member.getPhoneNo(),
                member.getDateOfBirth(),
                member.getAccountStatus(),
                member.getJoiningDate());
    }
}
