package org.unbiquitous.morfogenese;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class DNA {

	private Point position;
	private float velocidadeAuto;
	private float easing;
	private float easingAcceleration;
	
	private float tamanho;
	private float pesodalinha;

	public DNA position(Point position) {
		this.position = position;
		return this;
	}

	public DNA velocidadeAuto(float velocidadeAuto) {
		this.velocidadeAuto = velocidadeAuto;
		return this;
	}

	public DNA easing(float easing) {
		this.easing = easing;
		return this;
	}

	public DNA easingAcceleration(float easingAcceleration) {
		this.easingAcceleration = easingAcceleration;
		return this;
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> theMap = new HashMap<String, Object>();
		
		theMap.put("position", position);
		theMap.put("velocidadeAuto", velocidadeAuto);
		theMap.put("easing", easing);
		theMap.put("easingAcceleration", easingAcceleration);
		
		return theMap;
	}

}
