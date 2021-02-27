package com.zeebee.demo;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.DeploymentEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
		run();
	}

	public static void run() {
		final String gatewayAddress = "192.168.0.111:26500";

		final ZeebeClient client =
				ZeebeClient.newClientBuilder()
						.gatewayAddress(gatewayAddress)
						.build();

		System.out.println("Connected");
		final DeploymentEvent deployment = client.newDeployCommand()
				.addResourceFile(".//order-process.bpmn")
				.send()
				.join();

		final int version = deployment.getWorkflows().get(0).getVersion();
		System.out.println("Workflow deployed. Version: " + version);
		client.close();
		System.out.println("Closed.");
	}

}
