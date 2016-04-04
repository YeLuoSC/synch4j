package com.synch4j.exception;

/**
 * @author XieGuanNan
 * @date 2015-8-12-上午11:14:13
 * 当回调接口时，出现错误，将抛出该异常
 */
public class CallbackException extends Exception{
	
	private String info;
	
	public CallbackException(String info){
		super();
		this.info = "实现的接口类出错！请检查。"+info+"\r\n";
		this.info += super.getMessage();
	}
	
	

	@Override
	public String toString() {
		return "CallbackException [info=" + info + "]";
	}



	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
