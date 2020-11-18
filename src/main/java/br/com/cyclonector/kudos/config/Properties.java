package br.com.cyclonector.kudos.config;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "strava")
@Getter
@Setter
public class Properties {

    private String login;
    private String password;

    @ConfigProperty(name = "headless-browser")
    private boolean headlessBrowser = false;
}
