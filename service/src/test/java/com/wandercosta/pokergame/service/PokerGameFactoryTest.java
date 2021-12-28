package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGame;
import com.wandercosta.pokergame.dto.PokerGameCreateRequest;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class PokerGameFactoryTest {

    private static final EnhancedRandom RANDOM = EnhancedRandomBuilder.aNewEnhancedRandom();

    private final PokerGameFactory factory = new PokerGameFactory();

    @Test
    void shouldBuild() {
        final PokerGameCreateRequest dto = RANDOM.nextObject(PokerGameCreateRequest.class);

        final Instant beforeExecution = Instant.now();
        final PokerGame actual = factory.build(dto);
        final Instant afterExecution = Instant.now();

        assertThat(actual.getUuid()).isNotNull();
        assertThat(actual.getClosedAt()).isNull();
        assertThat(actual.getCreatedAt()).isBetween(beforeExecution, afterExecution);
        assertThat(actual.getName()).isEqualTo(dto.getName());
        assertThat(actual.getDescription()).isEqualTo(dto.getDescription());
        assertThat(actual.getAllowedPoints()).isEqualTo(dto.getAllowedPoints());
        assertThat(actual.getRoundingMode()).isEqualTo(dto.getRoundingMode());
        assertThat(actual.getVotes()).isEmpty();
    }
}