package com.mallen.stocker.ui;

import java.awt.Color;

public class ThemeManager {
	Theme theme_light, theme_dark, theme_return;
	public static boolean night = false;
	public ThemeManager(){
		Color[] colorPallete = new Color[5];
		colorPallete[0] = new Color(255, 255, 255); //PRIMARY
		colorPallete[1] = new Color(20, 20, 20);    //SECONDARY
		colorPallete[3] = new Color(0, 160, 0);       //DOWN
		colorPallete[4] = new Color(160, 0, 0);       //UP
		colorPallete[2] = new Color(45, 45, 45);       //ALT	
		
		theme_dark = new Theme(colorPallete[1], colorPallete[0], colorPallete[2], colorPallete[3], colorPallete[4]);
		theme_light = new Theme(colorPallete[0], colorPallete[1], new Color(240, 240, 240), colorPallete[3], colorPallete[4]);
	}
	public Theme loadTheme(String theme){
		if(night) theme_return = theme_dark;
		if(!night) theme_return = theme_light;
		return theme_return;
	}
	public static void toggleNight(){ if(night){night = false;}else{night = true;}} //Toggles the night varaible
}
