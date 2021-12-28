package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PokerGameRepository extends JpaRepository<PokerGame, String> {

    @Query("FROM PokerGame pg WHERE pg.uuid = :uuid" +
            " AND (pg.closedAt IS NULL OR pg.closedAt >= :minClosedAt)")
    Optional<PokerGame> findOpenById(final String uuid,
                                     final Instant minClosedAt);
}
