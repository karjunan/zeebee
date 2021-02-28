package com.zeebee.demo;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.DeploymentEvent;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import io.zeebe.client.api.worker.JobWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
		run();
	}

	public static void run() {
		final String gatewayAddress = "192.168.225.53:26500";
//		final String gatewayAddress = "localhost:26500";

		final ZeebeClient client =
				ZeebeClient.newClientBuilder()
						.usePlaintext()
						.gatewayAddress(gatewayAddress)
						.build();

		System.out.println("Connected");
		final DeploymentEvent deployment = client.newDeployCommand()
				.addResourceFile(".\\bpmn\\order-process.bpmn")
				.send()
				.join();

		final int version = deployment.getWorkflows().get(0).getVersion();
		System.out.println("Workflow deployed. Version: " + version);

		final WorkflowInstanceEvent wfInstance = client.newCreateInstanceCommand()
				.bpmnProcessId("Order_Placed")
				.latestVersion()
				.send()
				.join();
		System.out.println("Work flow instance ID :: ==> "+ wfInstance.getWorkflowInstanceKey());

//		final JobWorker jobWorker = client.newWorker()
//				.jobType("payment-service")
//				.handler(((jobClient, job) -> {
//					System.out.println("I am collecting the money");
//
//					jobClient.newCompleteCommand(job.getKey())
//							.send()
//							.join();
//				})).open();
//		jobWorker.close();
//		client.close();
//		System.out.println("Closed.");
	}

}
