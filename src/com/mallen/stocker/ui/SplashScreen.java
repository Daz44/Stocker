package com.mallen.stocker.ui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class SplashScreen extends JPanel {
	public void paint(Graphics g){
		
	super.paint(g);
		ImageIcon ii = new ImageIcon("res/splash.png");
		Image icon = ii.getImage();
		g.drawImage(icon, 0, 0, Boot.jfSplash.getWidth(), Boot.jfSplash.getHeight(), this);
	}
}
