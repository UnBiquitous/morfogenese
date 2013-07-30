package org.unbiquitous.morfogenese;

import processing.core.PApplet;

public class Color {

	private int color;
	private PApplet p;
	
	public Color(PApplet parent, int color) {
		this.p = parent;
		this.color = color;
	}
	
	public int color(){
		return color;
	}
	
	public float red(){
		return p.red(color);
	}
	
	public float green(){
		return p.green(color);
	}
	
	public float blue(){
		return p.blue(color);
	}
	
	public Color set(float r, float g, float b){
		this.color = p.color(r,g,b);
		return this;
	}
	
	public static Color color(PApplet parent,float r, float g, float b){
		return new Color(parent, 0).set(r, g, b);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Color){
			Color that = (Color) obj;
			return this == obj || this.color() == that.color();
		}
		return false;
	}
}
