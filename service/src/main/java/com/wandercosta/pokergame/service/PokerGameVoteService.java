package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGameVote;
import com.wandercosta.pokergame.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PokerGameVoteService {

    private final PokerGameVoteRepository repository;

    public PokerGameVoteService(final PokerGameVoteRepository repository) {
        this.repository = repository;
    }

    public PokerGameVote save(final PokerGameVote game) {
        return repository.save(game);
    }

    public Optional<PokerGameVote> find(final String gameUUID,
                                        final String voterUUID) {
        return repository.findByGame_UuidAndVoterUuid(gameUUID, voterUUID);
    }

    public PokerGameVote get(final String gameUUID,
                             final String voterUUID) {
        return repository.findByGame_UuidAndVoterUuid(gameUUID, voterUUID)
                .orElseThrow(() -> new EntityNotFoundException("Vote not found"));
    }
}
