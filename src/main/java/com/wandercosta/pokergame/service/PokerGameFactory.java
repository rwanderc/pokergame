package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
import com.wandercosta.pokergame.dto.PokerGameCreateRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class PokerGameFactory {

    public PokerGame build(final PokerGameCreateRequest dto) {
        final PokerGame pokerGame = new PokerGame();
        pokerGame.setUuid(UUID.randomUUID().toString());
        pokerGame.setName(dto.getName());
        pokerGame.setDescription(dto.getDescription());
        pokerGame.setCreatedAt(Instant.now());
        pokerGame.setExpiresAt(dto.getExpiresAt());
        pokerGame.setAllowedPoints(dto.getAllowedPoints());
        pokerGame.setRoundingMode(dto.getRoundingMode());
        return pokerGame;
    }
}
