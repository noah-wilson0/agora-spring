package com.agora.debate.member.repository;

import com.agora.debate.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByName(String name);
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);

}
