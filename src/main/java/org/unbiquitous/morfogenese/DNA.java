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
	private float pesoDaLinha;
	private float diametroDaForma;
	private int formaCabeca;
	private int formaPescoco;
	private int formaRabo;
	private int instrumento;
	private int notaMusical;
	private float energia;
	private float pontoDeMaturidadeParaCruzamento;
	private int chance;
	private boolean vida;
	private boolean podre;

	private Color cor;
	private Color corLinha;
	
	private float corAlpha; // Calculated
	private float corLinhaAlpha; // Calculated
	private float velocidade; // Init
	private float novachance; // Init
	private float maturidade; // Init
	private float velocidadeAutoOriginal; // = velocidadeAuto
	private float finalBando; // = energia
	private int bancodadosinstrumento; // Calculated
	private float atracao; // Init
	private float maxformadiam; // Calculated
	private int numerodepontosdalinha; // Calculated
	private float[] pontox; // Calculated
	private float[] pontoy; // Calculated
	private float[] distx1; // Calculated
	private float[] disty1; // Calculated
	private int[] ordemxy; // Calculated
	
	public DNA position(Point position) {
		this.position = position;
		return this;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> theMap = new HashMap<String, Object>();
		
		theMap.put("position", position);
		theMap.put("velocidadeAuto", velocidadeAuto);
		theMap.put("easing", easing);
		theMap.put("easingAcceleration", easingAcceleration);
		
		theMap.put("tamanho", tamanho);
		theMap.put("pesoDaLinha", pesoDaLinha);
		theMap.put("diametroDaForma", diametroDaForma);
		theMap.put("formaCabeca", formaCabeca);
		theMap.put("formaPescoco", formaPescoco);
		theMap.put("formaRabo", formaRabo);
		theMap.put("instrumento", instrumento);
		theMap.put("notaMusical", notaMusical);
		theMap.put("energia", energia);
		theMap.put("pontoDeMaturidadeParaCruzamento", pontoDeMaturidadeParaCruzamento);
		theMap.put("chance", chance);
		theMap.put("vida", vida);
		theMap.put("podre", podre);
		
		return theMap;
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
	
	public DNA tamanho(float tamanho) {
		this.tamanho = tamanho;
		return this;
	}

	public DNA pesoDaLinha(float pesoDaLinha) {
		this.pesoDaLinha = pesoDaLinha;
		return this;
	}

	public DNA diametroDaForma(float diametroDaForma) {
		this.diametroDaForma = diametroDaForma;
		return this;
	}

	public DNA formaCabeca(int formaCabeca) {
		this.formaCabeca = formaCabeca;
		return this;
	}

	public DNA formaPescoco(int formaPescoco) {
		this.formaPescoco = formaPescoco;
		return this;
	}

	public DNA formaRabo(int formaRabo) {
		this.formaRabo = formaRabo;
		return this;
	}

	public DNA instrumento(int instrumento) {
		this.instrumento = instrumento;
		return this;
	}

	public DNA notaMusical(int notaMusical) {
		this.notaMusical = notaMusical;
		return this;
	}

	public DNA energia(float energia) {
		this.energia = energia;
		return this;
	}

	public DNA pontoDeMaturidadeParaCruzamento(float pontoDeMaturidadeParaCruzamento) {
		this.pontoDeMaturidadeParaCruzamento = pontoDeMaturidadeParaCruzamento;
		return this;
	}

	public DNA chance(int chance) {
		this.chance = chance;
		return this;
	}

	public DNA vida(boolean vida) {
		this.vida = vida;
		return this;
	}

	public DNA podre(boolean podre) {
		this.podre = podre;
		return this;
	}

}
