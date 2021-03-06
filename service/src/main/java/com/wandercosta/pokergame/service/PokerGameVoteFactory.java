package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
import com.wandercosta.pokergame.domain.PokerGameVote;
import com.wandercosta.pokergame.dto.PokerGameVoteRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PokerGameVoteFactory {

    public PokerGameVote build(final PokerGameVoteRequest dto,
                               final String uuid,
                               final String voterUUID,
                               final PokerGame game) {
        final PokerGameVote vote = new PokerGameVote();
        vote.setUuid(uuid);
        vote.setVoterUuid(voterUUID);
        vote.setVoterName(dto.getVoterName());
        vote.setPoints(dto.getPoints());
        vote.setVotedAt(Instant.now());
        vote.setGame(game);
        return vote;
    }
}
