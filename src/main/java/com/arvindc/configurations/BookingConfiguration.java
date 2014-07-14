package com.arvindc.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
public class BookingConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();
}
