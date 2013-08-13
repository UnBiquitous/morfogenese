package org.unbiquitous.morfogenese;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceCall;

public class MorfoAppTest {

	@Test public void receiveAMigratedDnaAndAttachToANewCreature(){
		MorfoApp app = new MorfoApp();
		app.morfogenese = mock(Morfogenese.class);
		
		DNA dna = DNA.autoGenese(100, 100, 2);
		
		ServiceCall call = new ServiceCall();
		call.addParameter("dna", dna.toMap());
		app.migrate(call,null);
		
		verify(app.morfogenese).criaBicho(eq(dna));
	}
	
}
