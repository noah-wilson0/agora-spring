package com.agora.debate.debate.repository;

import com.agora.debate.debate.entity.DebateRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebateRoomRepository extends JpaRepository<DebateRoom, Long> {
    // 기본 findById 등을 제공하므로 추가 코드가 없어도 됩니다.
}