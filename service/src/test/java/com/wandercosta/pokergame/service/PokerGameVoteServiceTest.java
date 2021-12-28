package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGameVote;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokerGameVoteServiceTest {

    private static final EnhancedRandom RANDOM = EnhancedRandomBuilder.aNewEnhancedRandom();

    @Mock
    private PokerGameVoteRepository repository;

    @InjectMocks
    private PokerGameVoteService service;

    @Test
    void shouldSave() {
        final PokerGameVote vote = RANDOM.nextObject(PokerGameVote.class);
        when(repository.save(vote)).thenReturn(vote);

        final PokerGameVote actual = service.save(vote);

        assertThat(actual).isSameAs(vote);
    }

    @Test
    void shouldFind() {
        final PokerGameVote vote = RANDOM.nextObject(PokerGameVote.class);
        final String gameUuid = "dummy-game-uuid";
        final String voterUuid = "dummy-voter-uuid";
        when(repository.findByGame_UuidAndVoterUuid(gameUuid, voterUuid)).thenReturn(Optional.of(vote));

        final Optional<PokerGameVote> actual = service.find(gameUuid, voterUuid);

        assertThat(actual).contains(vote);
    }
}
