package com.netflix.worker.tasks;

import com.netflix.conductor.sdk.workflow.task.InputParam;
import com.netflix.conductor.sdk.workflow.task.OutputParam;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTaskWorker {

    @WorkerTask(value = "task_add", threadCount = 5, pollingInterval = 10)
    public @OutputParam("result") Integer addTwoNumbers(@InputParam("one") int one, @InputParam("two") int two) {
//        TaskContext context = TaskContext.get();
//        System.out.println(">> Task Context: " + context);
        System.out.println("task_add executed");
        return one + two;
    }

    @WorkerTask(value = "task_subtract", threadCount = 5, pollingInterval = 10)
    public @OutputParam("result") Integer subtractTwoNumbers(@InputParam("one") int one, @InputParam("two") int two) {
//        TaskContext context = TaskContext.get();
//        System.out.println(">> Task Context: " + context);
        System.out.println("task_subtract executed");
        return one - two;
    }

    @WorkerTask(value = "task_multiply", threadCount = 5, pollingInterval = 10)
    public @OutputParam("result") Integer multiplyTwoNumbers(@InputParam("one") int one, @InputParam("two") int two) {
//        TaskContext context = TaskContext.get();
//        System.out.println(">> Task Context: " + context);
        System.out.println("task_multiply executed");
        return one * two;
    }
}
