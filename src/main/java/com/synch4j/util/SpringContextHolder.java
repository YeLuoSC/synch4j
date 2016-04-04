package com.synch4j.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @author XieGuanNan
 * @date 2015-8-12-上午11:28:01
 * 通过该类获取IoC容器中的bean
 */
@Component
@Lazy(value=false)
public class SpringContextHolder implements ApplicationContextAware {
	
	private static ApplicationContext context;//声明一个静态变量保存
	
	@Override
	public void setApplicationContext(ApplicationContext contex)
	   throws BeansException {
		this.context=contex;
	}
	public static ApplicationContext getContext(){
	  return context;
	}
}
