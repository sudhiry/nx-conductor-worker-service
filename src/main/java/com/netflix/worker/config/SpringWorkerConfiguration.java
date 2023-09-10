package com.netflix.worker.config;

import com.netflix.conductor.sdk.workflow.executor.task.WorkerConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringWorkerConfiguration extends WorkerConfiguration {

    private final Environment environment;

    public SpringWorkerConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getPollingInterval(String taskName) {
        return getProperty(taskName, "pollingInterval", Integer.class, 0);
    }

    @Override
    public int getThreadCount(String taskName) {
        return getProperty(taskName, "threadCount", Integer.class, 0);
    }

    @Override
    public String getDomain(String taskName) {
        return getProperty(taskName, "domain", String.class, "DEFAULT");
    }

    private <T>T getProperty(String taskName, String property, Class<T> type, T defaultValue) {
        String key = "conductor.worker." + taskName + "." + property;
        T value = environment.getProperty(key, type, defaultValue);
        if(null == value  || value == defaultValue) {
            key = "conductor.worker.all." + property;
            value = environment.getProperty(key, type, defaultValue);
        }
        return value;
    }

}
