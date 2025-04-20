package com.aerospike.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.aerospike.client.policy.ClientPolicy;

@SpringBootApplication
public class AerospikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AerospikeApplication.class, args);
		System.out.println("latest build somnath");
	}

	@Bean
	public AerospikeClient aerospikeClient() {
		ClientPolicy clientPolicy = new ClientPolicy();
		Host[] hosts = new Host[1];
		hosts[0] = new Host("localhost", 3000);
		AerospikeClient client = new AerospikeClient(clientPolicy, hosts);
		return client;
	}

}
