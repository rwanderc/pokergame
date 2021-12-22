package com.wandercosta.pokergame.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PokerGameVoteRequest {

    @NotBlank
    private String voterName;

    @NotNull
    private Float points;
}
