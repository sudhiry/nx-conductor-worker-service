package com.netflix.worker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.sdk.workflow.executor.task.AnnotatedWorkerExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ConductorWorkerAutoConfiguration implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger =  LoggerFactory.getLogger(ConductorWorkerAutoConfiguration.class);

    private final ObjectMapper objectMapper;

    private final AnnotatedWorkerExecutor annotatedWorkerExecutor;

    private final MetadataClient metadataClient;

    public ConductorWorkerAutoConfiguration(
        ObjectMapper objectMapper,
        AnnotatedWorkerExecutor annotatedWorkerExecutor,
        MetadataClient metadataClient
    ) {
        super();
        this.objectMapper = objectMapper;
        this.annotatedWorkerExecutor = annotatedWorkerExecutor;
        this.metadataClient = metadataClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent refreshedEvent)  {
        ApplicationContext applicationContext = refreshedEvent.getApplicationContext();
        try {
            registerTaskDefs(applicationContext);
            registerWorkflowDefs(applicationContext);
            Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Component.class);
            beans.values().forEach(annotatedWorkerExecutor::addBean);
            annotatedWorkerExecutor.startPolling();
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }

    private void registerTaskDefs(ApplicationContext applicationContext) throws IOException {
        String taskDefsResourcePath = "classpath:task_defs/*.json";
        Resource[] resources = applicationContext.getResources(taskDefsResourcePath);
        List<TaskDef> taskDefs = new ArrayList<>();
        for (Resource resource : resources) {
            TaskDef taskDef = objectMapper.readValue(resource.getInputStream(), TaskDef.class);
            taskDefs.add(taskDef);
        }
        if (taskDefs.size() > 0) {
            logger.info("Registering Task Definitions: " + taskDefs);
            metadataClient.registerTaskDefs(taskDefs);
        }
    }

    private void registerWorkflowDefs(ApplicationContext applicationContext) throws IOException {
        String workflowDefsResourcePath = "classpath:workflow_defs/*.json";
        Resource[] resources = applicationContext.getResources(workflowDefsResourcePath);
        List<WorkflowDef> workflowDefs = new ArrayList<>();
        for (Resource resource : resources) {
            WorkflowDef workflowDef = objectMapper.readValue(resource.getInputStream(), WorkflowDef.class);
            workflowDefs.add(workflowDef);
        }
        if (workflowDefs.size() > 0) {
            logger.info("Registering Workflow Definitions: " + workflowDefs);
            metadataClient.updateWorkflowDefs(workflowDefs);
        }
    }
}
