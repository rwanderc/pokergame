package com.wandercosta.pokergame.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "poker_game_votes", uniqueConstraints = @UniqueConstraint(columnNames = {"game_uuid", "voterUuid"}))
public class PokerGameVote {

    @Id
    @Column(nullable = false, length = 64)
    private String uuid;

    @Column(nullable = false, length = 64)
    private String voterUuid;

    @Column(nullable = false)
    private String voterName;

    @Column(nullable = false)
    private Instant votedAt;

    @Column(nullable = false)
    private Float points;

    @JsonIgnore
    @ManyToOne(optional = false, targetEntity = PokerGame.class)
    private PokerGame game;
}
