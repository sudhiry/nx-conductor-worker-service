package com.netflix.worker.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.worker.models.JourneyRequest;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journey")
public class JourneyController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final WorkflowClient workflowClient;

    private final TaskClient taskClient;

    public JourneyController(WorkflowClient workflowClient, TaskClient taskClient) {
        super();
        this.workflowClient = workflowClient;
        this.taskClient = taskClient;
    }

    @PostMapping(value = "/v1/start")
    public String startJourney(@RequestBody JourneyRequest journeyRequest) {
        StartWorkflowRequest startWorkflowRequest = new StartWorkflowRequest();
        startWorkflowRequest.setName("workflow_calculator");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> inputData = objectMapper.convertValue(journeyRequest, new TypeReference<>() {});
        startWorkflowRequest.setInput(inputData);
        String workflowId = workflowClient.startWorkflow(startWorkflowRequest);
        logger.info("Workflow id: {}", workflowId);
        return workflowId;
    }

    @GetMapping(value = "/v1/{journeyId}/next-tasks")
    public List<String> getNextHumanTasks(@PathVariable String journeyId) {

        return Collections.emptyList();
    }
}
