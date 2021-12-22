package com.wandercosta.pokergame.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "poker_games")
public class PokerGame {

    public static final int ID_LENGTH = 64;

    @Id
    @Column(length = ID_LENGTH)
    private String uuid;

    @Column(length = 64)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private Instant createdAt;

    @Column
    private Instant closedAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false, length = 128)
    @Convert(converter = CommaSeparatedSetAttributeConverter.class)
    private Set<Float> allowedPoints;

    @Column(nullable = false)
    private RoundingMode roundingMode;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<PokerGameVote> votes;

    public boolean isClosed() {
        return closedAt != null && closedAt.isBefore(Instant.now());
    }
}
