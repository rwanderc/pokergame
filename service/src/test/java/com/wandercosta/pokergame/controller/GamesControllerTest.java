package com.wandercosta.pokergame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wandercosta.pokergame.domain.PokerGame;
import com.wandercosta.pokergame.domain.PokerGameVote;
import com.wandercosta.pokergame.dto.PokerGameCreateRequest;
import com.wandercosta.pokergame.dto.PokerGameVoteRequest;
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;

import static com.wandercosta.pokergame.config.CookiesProperties.VOTER_UUID_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class GamesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("should create game")
    void shouldCreateGame() throws Exception {
        final PokerGameCreateRequest dto = newPokerGameRequest();
        final PokerGame gameFromPost = createPokerGame(newPokerGameRequest());

        assertThat(gameFromPost.getName()).isEqualTo(dto.getName());
        assertThat(gameFromPost.getDescription()).isEqualTo(dto.getDescription());
        assertThat(gameFromPost.getAllowedPoints()).isEqualTo(dto.getAllowedPoints());
        assertThat(gameFromPost.getRoundingMode()).isEqualTo(dto.getRoundingMode());
        assertThat(gameFromPost.getExpiresAt()).isCloseTo(dto.getExpiresAt(),
                new TemporalUnitWithinOffset(2, ChronoUnit.SECONDS));
        assertThat(gameFromPost.isClosed()).isFalse();
    }

    @Test
    @DisplayName("should create and retrieve game")
    void shouldRetrieveGame() throws Exception {
        final PokerGame gameFromPost = createPokerGame(newPokerGameRequest());
        final PokerGame gameFromGet = getPokerGame(gameFromPost.getUuid());
        assertThat(gameFromGet).isEqualTo(gameFromPost);
    }

    @Test
    @DisplayName("should create and close game")
    void shouldCloseGame() throws Exception {
        final PokerGame gameFromPost = createPokerGame(newPokerGameRequest());
        assertThat(gameFromPost.isClosed()).isFalse();

        final Instant beforeClosing = Instant.now();
        final PokerGame gameFromClose = closePokerGame(gameFromPost.getUuid());
        final Instant afterClosing = Instant.now();
        assertThat(gameFromClose.getClosedAt()).isBetween(beforeClosing, afterClosing);
    }

    @Test
    @DisplayName("should user vote in game")
    void shouldUserVoteInGame() throws Exception {
        final PokerGame gameFromPost = createPokerGame(newPokerGameRequest());

        final PokerGameVoteRequest voteDto = newVoteRequest();
        final Instant beforeVoting = Instant.now();
        final PokerGameVote actualVote = vote(gameFromPost.getUuid(), voteDto);
        final Instant afterVoting = Instant.now();

        assertThat(actualVote.getGame()).isNull();
        assertThat(actualVote.getUuid()).isNotNull();
        assertThat(actualVote.getVoterUuid()).isNotNull();
        assertThat(actualVote.getVoterName()).isEqualTo(voteDto.getVoterName());
        assertThat(actualVote.getPoints()).isEqualTo(voteDto.getPoints());
        assertThat(actualVote.getVotedAt()).isBetween(beforeVoting, afterVoting);
    }

    @Test
    @DisplayName("should voting return cookie")
    void shouldVotingInGameReturnCookie() throws Exception {
        final PokerGame gameFromPost = createPokerGame(newPokerGameRequest());

        final PokerGameVoteRequest voteDto = newVoteRequest();
        final MvcResult mvcResult = voteResult(gameFromPost.getUuid(), voteDto);
        final Cookie voterUuidCookie = mvcResult.getResponse().getCookie(VOTER_UUID_COOKIE_NAME);

        assertThat(voterUuidCookie).isNotNull();
        assertThat(voterUuidCookie.getValue()).isNotBlank();
    }

    @Test
    @DisplayName("should same user update vote in same game")
    void shouldUpdateVoteInGame() throws Exception {
        final PokerGame gameFromPost = createPokerGame(newPokerGameRequest());

        final Cookie cookie = new Cookie(VOTER_UUID_COOKIE_NAME, "dummy-voter-uuid");
        final PokerGameVoteRequest voteDto1 = newVoteRequest(5f);
        final PokerGameVote vote1 = vote(gameFromPost.getUuid(), voteDto1, cookie);
        final PokerGameVoteRequest voteDto2 = newVoteRequest(10f);
        final PokerGameVote vote2 = vote(gameFromPost.getUuid(), voteDto2, cookie);

        assertThat(vote1.getUuid()).isEqualTo(vote2.getUuid());
        assertThat(vote1.getPoints()).isEqualTo(voteDto1.getPoints());
        assertThat(vote2.getPoints()).isEqualTo(voteDto2.getPoints());
    }

    private PokerGameCreateRequest newPokerGameRequest() {
        final PokerGameCreateRequest dto = new PokerGameCreateRequest();
        dto.setName("some-name");
        dto.setDescription("some-description");
        dto.setAllowedPoints(new HashSet<>(Arrays.asList(1f, 2f, 3f)));
        dto.setRoundingMode(RoundingMode.UP);
        dto.setExpiresAt(Instant.now().plusSeconds(60));
        return dto;
    }

    private PokerGameVoteRequest newVoteRequest() {
        return newVoteRequest(10f);
    }

    private PokerGameVoteRequest newVoteRequest(final float points) {
        final PokerGameVoteRequest vote = new PokerGameVoteRequest();
        vote.setVoterName("some-name");
        vote.setPoints(points);
        return vote;
    }

    private PokerGame createPokerGame(final PokerGameCreateRequest dto) throws Exception {
        final MvcResult postResult = mockMvc
                .perform(post("/games")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        final String postResultJson = postResult.getResponse().getContentAsString();
        return mapper.readValue(postResultJson, PokerGame.class);
    }

    private PokerGame getPokerGame(final String uuid) throws Exception {
        final MvcResult getResult = mockMvc
                .perform(get("/games/{uuid}", uuid))
                .andExpect(status().isOk())
                .andReturn();
        final String getResultJson = getResult.getResponse().getContentAsString();
        return mapper.readValue(getResultJson, PokerGame.class);
    }

    private PokerGame closePokerGame(final String uuid) throws Exception {
        final MvcResult getResult = mockMvc
                .perform(delete("/games/{uuid}", uuid))
                .andExpect(status().isOk())
                .andReturn();
        final String getResultJson = getResult.getResponse().getContentAsString();
        return mapper.readValue(getResultJson, PokerGame.class);
    }

    private MvcResult voteResult(final String gameUuid,
                                 final PokerGameVoteRequest voteDto,
                                 final Cookie... cookies) throws Exception {
        MockHttpServletRequestBuilder request = post("/games/{uuid}/votes", gameUuid)
                .content(mapper.writeValueAsString(voteDto))
                .contentType(MediaType.APPLICATION_JSON);
        if (cookies.length > 0) {
            request = request.cookie(cookies);
        }
        return mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    private PokerGameVote vote(final String gameUuid,
                               final PokerGameVoteRequest voteDto,
                               final Cookie... cookies) throws Exception {
        final String getResultJson = voteResult(gameUuid, voteDto, cookies)
                .getResponse().getContentAsString();
        return mapper.readValue(getResultJson, PokerGameVote.class);
    }
}
