package com.wandercosta.pokergame.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Data
public class PokerGameCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private Instant expiresAt = Instant.now().plus(30, ChronoUnit.DAYS);

    @NotEmpty
    private Set<Float> allowedPoints;

    private RoundingMode roundingMode = RoundingMode.UP;
}
