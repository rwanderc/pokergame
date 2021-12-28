package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokerGameServiceTest {

    private static final EnhancedRandom RANDOM = EnhancedRandomBuilder.aNewEnhancedRandom();

    @Mock
    private PokerGameRepository repository;

    @InjectMocks
    private PokerGameService service;

    @Test
    void shouldGet() {
        final PokerGame game = RANDOM.nextObject(PokerGame.class);
        final String dummyUuid = "dummy-uuid";
        when(repository.findById(dummyUuid)).thenReturn(Optional.of(game));

        final PokerGame actual = service.get(dummyUuid);

        assertThat(actual).isSameAs(game);
    }

    @Test
    void shouldGetOpen() {
        final PokerGame game = RANDOM.nextObject(PokerGame.class);
        final String dummyUuid = "dummy-uuid";
        when(repository.findOpenById(eq(dummyUuid), any(Instant.class))).thenReturn(Optional.of(game));

        final PokerGame actual = service.getOpen(dummyUuid);

        assertThat(actual).isSameAs(game);
    }

    @Test
    void shouldSave() {
        final PokerGame game = RANDOM.nextObject(PokerGame.class);
        when(repository.save(game)).thenReturn(game);

        final PokerGame actual = service.save(game);

        assertThat(actual).isSameAs(game);
    }

    @Test
    void shouldClose() {
        final PokerGame game = RANDOM.nextObject(PokerGame.class, "closedAt");
        final String dummyUuid = "dummy-uuid";
        when(repository.findOpenById(eq(dummyUuid), any(Instant.class))).thenReturn(Optional.of(game));
        when(repository.save(game)).thenReturn(game);

        assertThat(game.getClosedAt()).isNull();

        final PokerGame actual = service.close(dummyUuid);

        assertThat(actual).isSameAs(game);
        assertThat(actual.getClosedAt()).isNotNull();
    }
}
