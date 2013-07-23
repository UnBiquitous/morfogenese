package org.unbiquitous.morfogenese;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.mockito.Mockito.*;

public class MorfoAppTest {

	@Test public void receiveAMigratedDnaAndAttachToANewCreature(){
		MorfoApp app = new MorfoApp();
		app.morfogenese = mock(Morfogenese.class);
		
		int[] dna = new int[]{
			1,2,3,4,5,6,7,8,9,0,
			1,2,3,4,5,6,7,8,9,0,
			1,2,3,4
		};
		
		Map<String,Object> parameter = new HashMap<String, Object>();
		parameter.put("dna", dna);
		app.migrate(parameter);
		
		verify(app.morfogenese).criaBicho(dna);
	}
	
}
