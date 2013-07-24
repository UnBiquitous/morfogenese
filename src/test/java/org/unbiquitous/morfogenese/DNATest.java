package org.unbiquitous.morfogenese;

import java.awt.Point;

import org.fest.assertions.data.MapEntry;
import org.junit.Test;
import static org.fest.assertions.api.Assertions.*;

public class DNATest {

	@Test public void fromMap(){
		DNA dna = new DNA()
						.position(new Point(10,20))
						.velocidadeAuto(15.2f) //TODO: what is this?
						.easing(13.4f) 
						.easingAcceleration(1.5f) 
						;
		
		assertThat(dna.toMap())
			.contains(MapEntry.entry("position", new Point(10,20)))
			.contains(MapEntry.entry("velocidadeAuto", 15.2f))
			.contains(MapEntry.entry("easing", 13.4f))
			.contains(MapEntry.entry("easingAcceleration", 1.5f))
			;
	}
	
}
