package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
import com.wandercosta.pokergame.exception.GameClosedException;
import com.wandercosta.pokergame.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PokerGameService {

    private final PokerGameRepository repository;

    public PokerGameService(final PokerGameRepository repository) {
        this.repository = repository;
    }

    public PokerGame get(final String uuid) {
        return repository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("aaa"));
    }

    public PokerGame getOpen(final String uuid) {
        final PokerGame game = repository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Poker Game not found"));

        if (game.getClosedAt() != null && game.getClosedAt().isBefore(Instant.now())) {
            throw new GameClosedException("Poker Game is already closed.");
        }
        return game;
    }

    public PokerGame save(final PokerGame game) {
        return repository.save(game);
    }

    public PokerGame close(final String uuid) {
        return null;
    }
}
