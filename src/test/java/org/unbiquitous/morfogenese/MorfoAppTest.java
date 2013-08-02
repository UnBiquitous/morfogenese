package org.unbiquitous.morfogenese;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import processing.core.PApplet;
import static org.mockito.Mockito.*;

public class MorfoAppTest {

	@Test public void receiveAMigratedDnaAndAttachToANewCreature(){
		MorfoApp app = new MorfoApp();
		app.morfogenese = mock(Morfogenese.class);
		
		DNA dna = DNA.autoGenese(new PApplet(), 100, 100, 2);
		
		Map<String,Object> parameter = new HashMap<String, Object>();
		parameter.put("dna", dna.toMap());
		app.migrate(parameter);
		
		verify(app.morfogenese).criaBicho(eq(dna));
	}
	
}
