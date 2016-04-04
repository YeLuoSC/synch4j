package com.synch4j.exception;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * @author XieGuanNan
 * @date 2015-8-13-下午1:05:20
 * 当开发人员实现了某个接口，但是又没有将其注入到IoC容器中时，抛出此异常。
 * 抛出此异常时，检查是否将该实现类扫描进入IoC容器中！
 * 或者是回调时，代码中固定回调了某个接口，但是在IoC中实际没有这个接口的实现，这种问题是代码逻辑错误。需要检查当没有实现时，不需要回调接口！
 * 机制：启动时，会通过Java反射获取callback.impl包下所有的类class，并初始化到一个Map中
 * 当需要调用某个接口时，通过启动时扫描的Map中的class对象去IoC容器中提取该bean，提取不到说明没有将
 * 该bean注入至IoC容器中。
 */
public class CallbackClassNotExistIoCException extends NoSuchBeanDefinitionException{

	private String info;
	
	public CallbackClassNotExistIoCException(String message){
		super(message);
		info = message;
	}

	@Override
	public String toString() {
		return "CallbackClassNotExistIoCException [错误信息：" + info + "]";
	}
	
}
