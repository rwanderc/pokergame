package com.wandercosta.pokergame.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Data
public class PokerGameCreateRequest {

    @NotBlank
    @Length(min = 3, max = 64)
    private String name;

    @NotBlank
    @Length(max = 255)
    private String description;

    @Future
    private Instant expiresAt = Instant.now().plus(30, ChronoUnit.DAYS);

    @NotEmpty
    @Size(max = 10)
    private Set<Float> allowedPoints;

    private RoundingMode roundingMode = RoundingMode.UP;
}
