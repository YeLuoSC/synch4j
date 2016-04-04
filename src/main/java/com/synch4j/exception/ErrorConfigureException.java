package com.synch4j.exception;

public class ErrorConfigureException extends Exception{
private String info;
	
	public ErrorConfigureException(String info){
		super();
		this.info = "配置出错！请检查。"+info+"\r\n";
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
