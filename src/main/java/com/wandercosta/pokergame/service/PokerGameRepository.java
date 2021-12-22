package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokerGameRepository extends JpaRepository<PokerGame, String> {
}
