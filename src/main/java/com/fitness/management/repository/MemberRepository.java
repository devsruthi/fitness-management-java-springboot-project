package com.fitness.management.repository;

import com.fitness.management.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByEmailId(String emailId);
}
