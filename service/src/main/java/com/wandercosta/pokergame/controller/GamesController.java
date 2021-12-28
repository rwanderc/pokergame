package com.wandercosta.pokergame.controller;

import com.wandercosta.pokergame.config.CookiesProperties;
import com.wandercosta.pokergame.domain.PokerGame;
import com.wandercosta.pokergame.domain.PokerGameVote;
import com.wandercosta.pokergame.dto.ErrorResponse;
import com.wandercosta.pokergame.dto.PokerGameCreateRequest;
import com.wandercosta.pokergame.dto.PokerGameVoteRequest;
import com.wandercosta.pokergame.exception.EntityNotFoundException;
import com.wandercosta.pokergame.exception.GameClosedException;
import com.wandercosta.pokergame.service.PokerGameFactory;
import com.wandercosta.pokergame.service.PokerGameService;
import com.wandercosta.pokergame.service.PokerGameVoteFactory;
import com.wandercosta.pokergame.service.PokerGameVoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.wandercosta.pokergame.config.CookiesProperties.VOTER_UUID_COOKIE_NAME;

@RestController
@RequestMapping("/games")
public class GamesController {

    private final PokerGameService gameService;
    private final PokerGameFactory gameFactory;
    private final PokerGameVoteService voteService;
    private final PokerGameVoteFactory voteFactory;
    private final CookiesProperties cookiesProperties;

    public GamesController(final PokerGameService gameService,
                           final PokerGameFactory gameFactory,
                           final PokerGameVoteService voteService,
                           final PokerGameVoteFactory voteFactory,
                           final CookiesProperties cookiesProperties) {
        this.gameService = gameService;
        this.gameFactory = gameFactory;
        this.voteService = voteService;
        this.voteFactory = voteFactory;
        this.cookiesProperties = cookiesProperties;
    }

    @PostMapping(produces = "application/json")
    public PokerGame createGame(@RequestBody final PokerGameCreateRequest dto) {
        final PokerGame pokerGame = gameFactory.build(dto);
        return gameService.save(pokerGame);
    }

    @GetMapping(path = "{uuid}", produces = "application/json")
    public PokerGame getGame(@PathVariable final String uuid) {
        return gameService.get(uuid);
    }

    @DeleteMapping(path = "{uuid}", produces = "application/json")
    public PokerGame closeGame(@PathVariable final String uuid) {
        return gameService.close(uuid);
    }

    @PostMapping(path = "{gameUUID}/votes", produces = "application/json")
    public ResponseEntity<PokerGameVote> voteGame(@PathVariable final String gameUUID,
                                                  @RequestBody final PokerGameVoteRequest dto,
                                                  @CookieValue(value = VOTER_UUID_COOKIE_NAME, required = false) final String tmpVoterUUID,
                                                  final HttpServletResponse response) {
        final String voterUUID = tmpVoterUUID != null && !tmpVoterUUID.isEmpty()
                ? tmpVoterUUID : UUID.randomUUID().toString();
        response.addCookie(createCookie(voterUUID));

        final PokerGame game = gameService.getOpen(gameUUID);
        final String voteUUID = voteService.find(gameUUID, voterUUID)
                .map(PokerGameVote::getUuid)
                .orElse(UUID.randomUUID().toString());

        final PokerGameVote vote = voteFactory.build(dto, voteUUID, voterUUID, game);
        final PokerGameVote savedVote = voteService.save(vote);
        return ResponseEntity.ok(savedVote);
    }

    private Cookie createCookie(final String voterUUID) {
        final Cookie cookie = new Cookie(VOTER_UUID_COOKIE_NAME, voterUUID);
        cookie.setHttpOnly(cookiesProperties.isHttpOnly());
        cookie.setMaxAge((int) cookiesProperties.getMaxAge().get(ChronoUnit.SECONDS));
        cookie.setDomain(cookiesProperties.getDomain());
        return cookie;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(final EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(GameClosedException.class)
    public ResponseEntity<ErrorResponse> handleGameClosedException(final GameClosedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
