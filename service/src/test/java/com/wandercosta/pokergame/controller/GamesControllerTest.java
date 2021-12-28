package com.wandercosta.pokergame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wandercosta.pokergame.PokerGameApplication;
import com.wandercosta.pokergame.domain.PokerGame;
import com.wandercosta.pokergame.dto.PokerGameCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = PokerGameApplication.class)
class GamesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldCreateAndRetrieveGame() throws Exception {
        final PokerGameCreateRequest dto = new PokerGameCreateRequest();
        dto.setName("some-name");
        dto.setDescription("some-description");
        dto.setAllowedPoints(new HashSet<>(Arrays.asList(1f, 2f, 3f)));
        dto.setRoundingMode(RoundingMode.UP);
        dto.setExpiresAt(Instant.now().plusSeconds(60));

        final MvcResult postResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/games")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        final String postResultJson = postResult.getResponse().getContentAsString();
        final PokerGame gameFromPost = mapper.readValue(postResultJson, PokerGame.class);

        final MvcResult getResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/games/{uuid}", gameFromPost.getUuid()))
                .andExpect(status().isOk())
                .andReturn();
        final String getResultJson = getResult.getResponse().getContentAsString();
        final PokerGame gameFromGet = mapper.readValue(getResultJson, PokerGame.class);

        assertThat(gameFromGet).isEqualTo(gameFromPost);
    }
}
