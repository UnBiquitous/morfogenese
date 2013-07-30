package org.unbiquitous.morfogenese;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;

import processing.core.PApplet;
import static org.fest.assertions.api.Assertions.*;

public class DNATest {

	@Test public void convertFromMap(){
		DNA theDna = createTestDNA();
		
		Map<String, Object> theMap = createTestDNAMap();
		
		Map<String, Object> resultMap = theDna.toMap();
		
		for(Entry<String, Object> e : theMap.entrySet()){
			assertThat(resultMap.get(e.getKey())).isEqualTo(e.getValue());
		}
		
	}
	@Test public void convertToMap(){
		Map<String, Object> theMap = createTestDNAMap();
		
		DNA resultDNA = DNA.fromMap(theMap);
		for(Entry<String, Object> e : resultDNA.toMap().entrySet()){
			assertThat(theMap.get(e.getKey())).isEqualTo(e.getValue());
		}
	}

	private Map<String, Object> createTestDNAMap() {
		Map<String, Object> theMap = new HashMap<String, Object>();
		theMap.put("position", new Point(10,20));
		theMap.put("velocidadeAuto", 15.2f);
		theMap.put("easing", 13.4f);
		theMap.put("easingAcceleration", 1.5f);
		theMap.put("tamanho", 3.2f);
		theMap.put("pesoDaLinha", 4.1f);
		theMap.put("diametroDaForma", 5.0f);
		theMap.put("formaCabeca", 1);
		theMap.put("formaPescoco", 2);
		theMap.put("formaRabo", 3);
		theMap.put("instrumento", 4);
		theMap.put("notaMusical", 5);
		theMap.put("energia", 100.5f);
		theMap.put("pontoDeMaturidadeParaCruzamento", 150.0f);
		theMap.put("chance", 1);
		theMap.put("vida", true);
		theMap.put("podre", false);
		theMap.put("cor", Color.color(new PApplet(), 1,2,3));
		theMap.put("corLinha", Color.color(new PApplet(), 3,2,1));
		theMap.put("velocidade", 3f);
		theMap.put("novachance", 4f);
		theMap.put("maturidade", 5f);
		theMap.put("velocidadeAutoOriginal", 15.2f);
		theMap.put("finalBando", 100.5f);
		theMap.put("atracao", 6f);
		theMap.put("corLinhaAlpha", 7f);
		theMap.put("corAlpha", 8f);
		theMap.put("bancodadosinstrumento", 9);
		theMap.put("maxformadiam", 10f);
		theMap.put("numerodepontosdalinha", 11);
		theMap.put("pontox", new float[]{12f});
		theMap.put("pontoy", new float[]{13f});
		theMap.put("distx1", new float[]{14f});
		theMap.put("disty1", new float[]{15f});
		theMap.put("ordemxy", new int[]{16});
		return theMap;
	}

	private DNA createTestDNA() {
		DNA theDna = new DNA()
						.position(new Point(10,20))
						.velocidadeAuto(15.2f) 
						.easing(13.4f) 
						.easingAcceleration(1.5f) 
						.tamanho(3.2f)
						.pesoDaLinha(4.1f)
						.diametroDaForma(5.0f)
						.formaCabeca(1)
						.formaPescoco(2)
						.formaRabo(3)
						.instrumento(4)
						.notaMusical(5)
						.energia(100.5f)
						.pontoDeMaturidadeParaCruzamento(150.0f)
						.chance(1)
						.vida(true)
						.podre(false)
						.cor(Color.color(new PApplet(), 1,2,3))
						.corLinha(Color.color(new PApplet(), 3,2,1))
						.velocidade(3f)
						.novachance(4f)
						.maturidade(5f)
						.velocidadeAutoOriginal(15.2f)
						.finalBando(100.5f)
						.atracao(6f)
						.corLinhaAlpha(7f)
						.corAlpha(8f)
						.bancodadosinstrumento(9)
						.maxformadiam(10f)
						.numerodepontosdalinha(11)
						.pontox(new float[]{12f})
						.pontoy(new float[]{13f})
						.distx1(new float[]{14f})
						.disty1(new float[]{15f})
						.ordemxy(new int[]{16})
						;
		return theDna;
	}
	
}
