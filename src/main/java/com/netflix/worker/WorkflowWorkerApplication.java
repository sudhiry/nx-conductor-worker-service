package com.netflix.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//TODO Refer this for Conductor client https://github.com/Netflix/conductor/blob/main/client/src/test/java/com/netflix/conductor/client/sample/SampleWorker.java

@SpringBootApplication
public class WorkflowWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkflowWorkerApplication.class, args);
	}

}
