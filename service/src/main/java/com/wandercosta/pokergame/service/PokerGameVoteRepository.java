package com.wandercosta.pokergame.service;

import com.wandercosta.pokergame.domain.PokerGameVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PokerGameVoteRepository extends JpaRepository<PokerGameVote, String> {

    Optional<PokerGameVote> findByGame_UuidAndVoterUuid(final String gameUuid, final String voterUuid);
}
