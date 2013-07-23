package org.unbiquitous.morfogenese;

import java.util.Map;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.UosApplication;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyDeploy;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyStart;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyUndeploy;

import processing.core.PApplet;

public class MorfoApp implements UosApplication {

	protected Morfogenese morfogenese;

	public void init(OntologyDeploy ontology, String appId) {
		this.morfogenese = new Morfogenese();
	}
	
	public void start(Gateway gateway, OntologyStart ontology) {
//		PApplet.main(Morfogenese.class.getName());
//		PApplet.main(new String[] { "--present", Morfogenese.class.getName()});
		
		PApplet.runSketch(new String[]{"Morfogenese"},  morfogenese);
	}

	public void stop() throws Exception {}

	public void tearDown(OntologyUndeploy ontology) throws Exception {}

	public void migrate(Map<String, Object> parameter) {
		this.morfogenese.criaBicho((int[]) parameter.get("dna"));
	}

}
