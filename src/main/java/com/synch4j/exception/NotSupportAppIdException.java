package com.synch4j.exception;

public class NotSupportAppIdException extends Exception{
	
	private String info;
	
	public NotSupportAppIdException(String appId){
		super();
		info = "当前不支持该appId的同步！需要扩展,当前appId为："+appId;
	}

	@Override
	public String toString() {
		return "NotSupportAppIdException [info=" + info + "]";
	}
	
	
}
