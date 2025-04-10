package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c WHERE (:id IS NULL OR c.id < :id) ORDER BY c.date DESC LIMIT :limit")
    List<Chat> findAllChatsOrderByIdDesc(Long id, Integer limit);

    @Query("SELECT c FROM Chat c JOIN c.users p " +
            "GROUP BY c.id " +
            "HAVING COUNT(DISTINCT p.id) = :size AND " +
            "SUM(CASE WHEN p.id IN :userIds THEN 1 ELSE 0 END) = :size")
    Optional<Chat> findChatByExactParticipants(List<Long> userIds, long size);
}
