package com.synch4j.exception;

/**
 * @author XieGuanNan
 * @date 2015-8-12-上午10:42:52
 * 此异常暂时不需要，因为在日志的枚举中已经包含了各个类型，应该不会出现错误日志类型的异常
 */
public class ErrorLogTypeException extends Exception{

	private String info;
	
	public ErrorLogTypeException(String message){
		super();
		info = message;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
