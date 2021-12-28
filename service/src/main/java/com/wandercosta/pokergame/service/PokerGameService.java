package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
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
                .orElseThrow(() -> new EntityNotFoundException("Poker Game not found"));
    }

    public PokerGame getOpen(final String uuid) {
        return repository.findOpenById(uuid, Instant.now())
                .orElseThrow(() -> new EntityNotFoundException("Poker Game not found"));
    }

    public PokerGame save(final PokerGame game) {
        return repository.save(game);
    }

    public PokerGame close(final String uuid) {
        final PokerGame game = repository.findOpenById(uuid, Instant.now())
                .orElseThrow(() -> new EntityNotFoundException("Poker Game not found"));
        game.setClosedAt(Instant.now());
        return repository.save(game);
    }
}
