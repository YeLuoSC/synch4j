package com.synch4j.test;

import java.util.UUID;

public class Test3 {
	public static void main(String[] args){
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString().replaceAll("-", ""));
	}
}
