package org.unbiquitous.morfogenese;

import java.awt.Point;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;
import static org.fest.assertions.api.Assertions.*;

public class DNATest {

	@Test public void fromMap(){
		DNA dna = new DNA()
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
						;
		
		assertThat(dna.toMap())
			.contains(MapEntry.entry("position", new Point(10,20)))
			.contains(MapEntry.entry("velocidadeAuto", 15.2f))
			.contains(MapEntry.entry("easing", 13.4f))
			.contains(MapEntry.entry("easingAcceleration", 1.5f))
			.contains(MapEntry.entry("tamanho", 3.2f))
			.contains(MapEntry.entry("pesoDaLinha", 4.1f))
			.contains(MapEntry.entry("diametroDaForma", 5.0f))
			.contains(MapEntry.entry("formaCabeca", 1))
			.contains(MapEntry.entry("formaPescoco", 2))
			.contains(MapEntry.entry("formaRabo", 3))
			.contains(MapEntry.entry("instrumento", 4))
			.contains(MapEntry.entry("notaMusical", 5))
			.contains(MapEntry.entry("energia", 100.5f))
			.contains(MapEntry.entry("pontoDeMaturidadeParaCruzamento", 150.0f))
			.contains(MapEntry.entry("chance", 1))
			.contains(MapEntry.entry("vida", true))
			.contains(MapEntry.entry("podre", false))
			;
	}
	
}
