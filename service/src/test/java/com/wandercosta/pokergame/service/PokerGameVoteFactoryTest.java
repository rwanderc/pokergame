package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
import com.wandercosta.pokergame.domain.PokerGameVote;
import com.wandercosta.pokergame.dto.PokerGameVoteRequest;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PokerGameVoteFactoryTest {

    private static final EnhancedRandom RANDOM = EnhancedRandomBuilder.aNewEnhancedRandom();

    private final PokerGameVoteFactory factory = new PokerGameVoteFactory();

    @Test
    void shouldBuild() {
        final PokerGameVoteRequest dto = RANDOM.nextObject(PokerGameVoteRequest.class);

        final String uuid = UUID.randomUUID().toString();
        final String voterUuid = UUID.randomUUID().toString();
        final PokerGame game = RANDOM.nextObject(PokerGame.class);

        final Instant beforeExecution = Instant.now();
        final PokerGameVote actual = factory.build(dto, uuid, voterUuid, game);
        final Instant afterExecution = Instant.now();

        assertThat(actual.getUuid()).isEqualTo(uuid);
        assertThat(actual.getVoterUuid()).isEqualTo(voterUuid);
        assertThat(actual.getVotedAt()).isBetween(beforeExecution, afterExecution);
        assertThat(actual.getVoterName()).isEqualTo(dto.getVoterName());
        assertThat(actual.getPoints()).isEqualTo(dto.getPoints());
        assertThat(actual.getGame()).isEqualTo(game);
    }
}
