package org.unbiquitous.morfogenese;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.UosApplication;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyDeploy;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyStart;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyUndeploy;

import processing.core.PApplet;

public class App implements UosApplication {

	public void init(OntologyDeploy ontology, String appId) {}
	
	public void start(Gateway gateway, OntologyStart ontology) {
//		PApplet.main(Morfogenese.class.getName());
//		PApplet.main(new String[] { "--present", Morfogenese.class.getName()});
		PApplet.runSketch(new String[]{"Morfogenese"},  new Morfogenese());
	}

	public void stop() throws Exception {}

	public void tearDown(OntologyUndeploy ontology) throws Exception {}

}
