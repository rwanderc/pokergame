CREATE TABLE poker_game_votes
  (
     uuid       VARCHAR(64) NOT NULL,
     points     FLOAT NOT NULL,
     voted_at   TIMESTAMP NOT NULL,
     voter_name VARCHAR(255) NOT NULL,
     voter_uuid VARCHAR(64) NOT NULL,
     game_uuid  VARCHAR(64) NOT NULL,
     PRIMARY KEY (uuid)
  );

CREATE TABLE poker_games
  (
     uuid           VARCHAR(64) NOT NULL,
     allowed_points VARCHAR(128) NOT NULL,
     closed_at      TIMESTAMP,
     created_at     TIMESTAMP NOT NULL,
     description    VARCHAR(255),
     expires_at     TIMESTAMP NOT NULL,
     NAME           VARCHAR(64),
     rounding_mode  INTEGER NOT NULL,
     PRIMARY KEY (uuid)
  );

ALTER TABLE poker_game_votes
    ADD CONSTRAINT ukgy0y083et4nyv1qo45jv7a4ra
    UNIQUE (game_uuid, voter_uuid);

ALTER TABLE poker_game_votes
    ADD CONSTRAINT fkhcuixepptn1nl395qpivjwn6o
    FOREIGN KEY (game_uuid)
    REFERENCES poker_games;
