package com.agora.debate.member.repository;

import com.agora.debate.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByName(String name);
    boolean existsByUsername(String username);
    boolean existsByEmail(String username);
    Optional<Member> findByUsername(String username);
    void deleteByUsername(String username);

}
