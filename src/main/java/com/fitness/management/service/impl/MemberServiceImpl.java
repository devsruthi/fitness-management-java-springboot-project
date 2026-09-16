package com.fitness.management.service.impl;

import com.fitness.management.dto.request.MemberLoginRequest;
import com.fitness.management.dto.request.MemberRegistrationRequest;
import com.fitness.management.dto.response.MemberAuthResponse;
import com.fitness.management.dto.response.MemberResponse;
import com.fitness.management.dto.response.MessageResponse;
import com.fitness.management.entity.Member;
import com.fitness.management.entity.enums.AccountStatus;
import com.fitness.management.exception.BusinessRuleException;
import com.fitness.management.exception.ResourceNotFoundException;
import com.fitness.management.repository.BookingRepository;
import com.fitness.management.repository.MemberRepository;
import com.fitness.management.repository.MemberSubscriptionRepository;
import com.fitness.management.repository.PaymentRepository;
import com.fitness.management.service.MemberService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final MemberSubscriptionRepository memberSubscriptionRepository;
    private final BookingRepository bookingRepository;

    public MemberServiceImpl(
            MemberRepository memberRepository,
            PaymentRepository paymentRepository,
            MemberSubscriptionRepository memberSubscriptionRepository,
            BookingRepository bookingRepository) {
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
        this.memberSubscriptionRepository = memberSubscriptionRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<MemberResponse> listMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public MemberAuthResponse register(MemberRegistrationRequest request) {
        if (memberRepository.existsByEmailId(request.emailId())) {
            throw new BusinessRuleException("Email ID already exists!");
        }

        Member member = new Member();
        member.setFirstName(request.firstName());
        member.setLastName(request.lastName());
        member.setEmailId(request.emailId());
        member.setPassword(request.password());
        member.setPhoneNo(request.phoneNo());
        member.setDateOfBirth(request.dateOfBirth());
        member.setAccountStatus(AccountStatus.ACTIVE);
        member.setJoiningDate(LocalDate.now());

        Member saved = memberRepository.save(member);
        return new MemberAuthResponse(
                saved.getMemberId(),
                saved.getFirstName() + " " + saved.getLastName(),
                "Registration successful");
    }

    @Override
    public MemberAuthResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmailId(request.emailId())
                .filter(found -> found.getPassword().equals(request.password()))
                .orElseThrow(() -> new BusinessRuleException(
                        "Invalid email ID or password!", HttpStatus.UNAUTHORIZED));

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

    @Override
    @Transactional
    public MessageResponse deleteMember(Integer memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException("Member not found");
        }

        paymentRepository.deleteBySubscription_Member_MemberId(memberId);
        memberSubscriptionRepository.deleteByMember_MemberId(memberId);
        bookingRepository.deleteByMember_MemberId(memberId);
        memberRepository.deleteById(memberId);

        return new MessageResponse("Member and related bookings, subscriptions, and payments deleted");
    }
}
