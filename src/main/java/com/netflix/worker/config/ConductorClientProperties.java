package com.netflix.worker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties("conductor.client")
public class ConductorClientProperties {

    private String serverUri;

    private String workerNamePrefix = "workflow-worker-%d";

    private Duration sleepWhenRetryDuration = Duration.ofMillis(500);

    private int updateRetryCount = 3;

    private Map<String, String> taskToDomain = new HashMap<>();

    private Map<String, Integer> taskThreadCount = new HashMap<>();

    private int shutdownGracePeriodSeconds = 10;

}
