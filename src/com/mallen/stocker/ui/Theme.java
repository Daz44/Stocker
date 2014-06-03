package com.mallen.stocker.ui;

import java.awt.Color;

public class Theme {
	public Color[] theme_array = new Color[5];
	public Theme(Color prim, Color sec, Color alt, Color up, Color down){
		theme_array[0] = prim;
		theme_array[1] = sec;
		theme_array[2] = alt;
		theme_array[3] = up;
		theme_array[4] = down;
	}
}
