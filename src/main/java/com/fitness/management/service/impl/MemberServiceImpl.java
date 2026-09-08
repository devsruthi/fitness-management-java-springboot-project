package com.fitness.management.service.impl;

import com.fitness.management.dto.request.MemberLoginRequest;
import com.fitness.management.dto.request.MemberRegistrationRequest;
import com.fitness.management.dto.response.MemberAuthResponse;
import com.fitness.management.dto.response.MemberResponse;
import com.fitness.management.entity.Member;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.MemberRepository;
import com.fitness.management.repository.StoredProcedureRepository;
import com.fitness.management.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final StoredProcedureRepository storedProcedureRepository;
    private final MemberRepository memberRepository;

    public MemberServiceImpl(
            StoredProcedureRepository storedProcedureRepository,
            MemberRepository memberRepository) {
        this.storedProcedureRepository = storedProcedureRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberAuthResponse register(MemberRegistrationRequest request) {
        storedProcedureRepository.registerMember(
                request.firstName(),
                request.lastName(),
                request.emailId(),
                request.password(),
                request.phoneNo(),
                request.dateOfBirth());

        Member member = memberRepository.findByEmailId(request.emailId())
                .orElseThrow(() -> new ResourceNotFoundException("Member was registered but could not be loaded"));
        return new MemberAuthResponse(
                member.getMemberId(),
                member.getFirstName() + " " + member.getLastName(),
                "Registration successful");
    }

    @Override
    public MemberAuthResponse login(MemberLoginRequest request) {
        Integer memberId = storedProcedureRepository.login(request.emailId(), request.password());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        return new MemberAuthResponse(
                member.getMemberId(),
                member.getFirstName() + " " + member.getLastName(),
                "Login successful, Welcome back");
    }

    @Override
    public MemberResponse getMember(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        return MemberResponse.from(member);
    }
}
