package com.aerospike.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.aerospike.client.policy.ClientPolicy;

@SpringBootApplication
@EnableConfigurationProperties
@EnableAutoConfiguration
public class AerospikeExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AerospikeExampleApplication.class, args);
		System.out.println("hellow spring boot aerospike client test");
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
