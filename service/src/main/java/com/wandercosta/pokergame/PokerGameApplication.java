package com.wandercosta.pokergame;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PokerGameApplication {

    public static void main(final String... args) {
        SpringApplication.run(PokerGameApplication.class, args);
    }
}
