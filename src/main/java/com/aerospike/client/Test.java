package com.aerospike.client;

import java.util.Random;

public class Test {

	
	public static void main(String[] args) {
		
		Random random = new Random();
		int x = random.nextInt(900) + 100;
		System.out.println(x);
	}
}
