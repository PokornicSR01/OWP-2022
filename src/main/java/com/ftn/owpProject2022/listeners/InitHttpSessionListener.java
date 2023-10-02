package com.ftn.owpProject2022.listeners;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class InitHttpSessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("Initialize session HttpSessionListener...");
		HttpSession session  = arg0.getSession();
		System.out.println("session id of user is " + session.getId());
		System.out.println("SUCCESS HttpSessionListener!");
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("DELETING SESSION HttpSessionListener...");
	}

}


