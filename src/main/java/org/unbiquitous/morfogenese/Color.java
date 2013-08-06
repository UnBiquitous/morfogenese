package org.unbiquitous.morfogenese;


public class Color {

	private int color;
	
	public Color(int color) {
		this.color = color;
	}
	
	public int color(){
		return color;
	}
	
	public Color set(float r, float g, float b){
		this.color = colorCalc(r, g, b, 255);
		return this;
	}
	
	public static Color color(float r, float g, float b){
		return new Color(0).set(r, g, b);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Color){
			Color that = (Color) obj;
			return this == obj || this.color() == that.color();
		}
		return false;
	}
	
	// Replication from processing
	public float red(){
		return (color >> 16) & 0xff;
	}
	
	public float green(){
		return (color >> 8) & 0xff;
	}
	
	public float blue(){
		return color & 0xff;
	}
	
	protected int colorCalc(float r, float g, float b, float a) {
	    if (r > 255) r = 255;
	    if (g > 255) g = 255;
	    if (b > 255) b = 255;
	    if (a > 255) a = 255;

	    if (r < 0) r = 0;
	    if (g < 0) g = 0;
	    if (b < 0) b = 0;
	    if (a < 0) a = 0;

        int calcRi = (int)(255*(r / 255)); 
	    int calcGi = (int)(255*(g / 255));
	    int calcBi = (int)(255*(b / 255)); 
	    int calcAi = (int)(255*(a / 255));
	    int calcColor = (calcAi << 24) | (calcRi << 16) | (calcGi << 8) | calcBi;
	    return calcColor;
	  }
}
