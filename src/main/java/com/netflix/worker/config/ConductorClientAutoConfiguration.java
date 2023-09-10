package com.netflix.worker.config;

import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.sdk.workflow.executor.task.AnnotatedWorkerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ConductorClientProperties.class)
public class ConductorClientAutoConfiguration {

    private static final Logger logger =  LoggerFactory.getLogger(ConductorClientAutoConfiguration.class);

    private String getConductorServerUrl (ConductorClientProperties properties) {
        return properties.getServerUri();
    }

    @Bean
    public MetadataClient getMetadataClient(ConductorClientProperties properties) {
        String apiUrl = getConductorServerUrl(properties);
        MetadataClient metadataClient = new MetadataClient();
        metadataClient.setRootURI(apiUrl);
        return metadataClient;
    }

    @Bean
    public WorkflowClient getWorkflowClient(ConductorClientProperties properties) {
        String apiUrl = getConductorServerUrl(properties);
        WorkflowClient workflowClient = new WorkflowClient();
        workflowClient.setRootURI(apiUrl);
        return workflowClient;
    }

    @Bean
    public TaskClient getTaskClient(ConductorClientProperties properties) {
        String apiUrl = getConductorServerUrl(properties);
        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI(apiUrl);
        return taskClient;
    }

    @Bean
    public AnnotatedWorkerExecutor getAnnotatedWorkerExecutor(TaskClient taskClient) {
        return new AnnotatedWorkerExecutor(taskClient);
    }

    // TODO: Check if we need Bean for TaskRunnerConfigurer.
    // For TaskRunnerConfigurer Refer: https://github.com/Netflix/conductor/blob/f4c95a41bd6ae8406b842b84668e215d0d1891ca/client-spring/src/main/java/com/netflix/conductor/client/spring/ConductorClientAutoConfiguration.java#L32

}
