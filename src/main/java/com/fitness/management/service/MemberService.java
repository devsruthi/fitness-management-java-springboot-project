package com.fitness.management.service;

import com.fitness.management.dto.request.MemberLoginRequest;
import com.fitness.management.dto.request.MemberRegistrationRequest;
import com.fitness.management.dto.response.MemberAuthResponse;
import com.fitness.management.dto.response.MemberResponse;

public interface MemberService {

    MemberAuthResponse register(MemberRegistrationRequest request);

    MemberAuthResponse login(MemberLoginRequest request);

    MemberResponse getMember(Integer memberId);
}
