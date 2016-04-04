package com.synch4j.test;

import java.util.ArrayList;
import java.util.List;

import com.synch4j.test.SignAnnotation.TYPE;

@SignAnnotation(TypeName = TYPE.HANDLER)
public class Test1 implements SignInterface{
	
	
	@Override
	public String returnStr() {
		
		return "The String was returned by Test1";
	}
	
	public static void main(String[] args){
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		System.out.println(list.toString());
		
		String a ="1,2,3,";
		String[] arr = a.split(","); 
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i]);
		}
		System.out.println(arr.length);
	}

}
