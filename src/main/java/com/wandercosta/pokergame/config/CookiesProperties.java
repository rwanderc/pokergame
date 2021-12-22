package com.wandercosta.pokergame.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "app.cookies")
public class CookiesProperties {

    public static final String VOTER_UUID_COOKIE_NAME = "voterUUID";

    @NotBlank
    private String domain;
    private boolean httpOnly;
    @NotNull
    private Duration maxAge;
}
