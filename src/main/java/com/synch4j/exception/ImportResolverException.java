package com.synch4j.exception;

public class ImportResolverException extends Exception{
	
	private String info;
	
	public ImportResolverException(String info){
		super();
		info = "导入解析器解析失败："+info;
	}

	@Override
	public String toString() {
		return "导入解析器解析失败：ImportResolverException [info=" + info + "]";
	}
}
