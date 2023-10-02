package com.ftn.owpProject2022.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class SecondConfiguration {

	@Bean(name= {"applicationMemory"},
			initMethod="init", destroyMethod="destroy")
	public ApplicationMemory getApplicationMemory() {
		return new ApplicationMemory();
	}
	
	public class ApplicationMemory extends HashMap {
		
		@Override
		public String toString() {
			return "ApplicationMemory"+this.hashCode();
		}
		public void destroy() {
			System.out.println("destroy method called");
		}

		public void init() {
			System.out.println("init method called");
		}
	}
}
