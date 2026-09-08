package com.fitness.management.dto.response;

public record MemberAuthResponse(
        Integer memberId,
        String memberName,
        String message
) {
}
