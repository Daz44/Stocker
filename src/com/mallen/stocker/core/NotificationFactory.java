package com.mallen.stocker.core;

import com.mallen.notify.DeskNotify;

public class NotificationFactory {
	public static boolean targNotify, changeNotify, volNotify;
	public static DeskNotify dn = new DeskNotify();
	public static String text, url;
	
	public static void notify(String url1, String text1){
		text = text1;
		url = url1;
		
		// SHOWING A NOTIFICATION ON A SEPERATE THREAD
		Runnable thread = new Runnable(){
			public void run(){
				NotificationFactory.dn.setUrl(NotificationFactory.url);
				NotificationFactory.dn.notify(NotificationFactory.text);
			}
		};
		new Thread(thread).start();
		////////////////////////////////////////////////////////
	}
}
