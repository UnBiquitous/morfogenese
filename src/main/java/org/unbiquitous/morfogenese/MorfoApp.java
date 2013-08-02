package org.unbiquitous.morfogenese;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.applicationManager.UosApplication;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyDeploy;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyStart;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyUndeploy;

import processing.core.PApplet;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import docs.oracle.com.example.ListDialog;

public class MorfoApp implements UosApplication {

	protected Morfogenese morfogenese;
	protected Gateway gateway;

	public void init(OntologyDeploy ontology, String appId) {
		this.morfogenese = new Morfogenese();
		this.morfogenese.setMListener(new MorfoClickListener() {
			
			synchronized public void perform(Point position, Character key, Object param) {
				System.out.println(
						String.format(	"Selected Creature at position %s " +
										"with key '%s' and param '%s'", 
										position, key, param));
				List<UpDevice> listDevices = gateway.listDevices();
				String[] options = 
						Lists.transform(listDevices, new Function<UpDevice, String>() {
							public String apply(UpDevice input) {
								return input.getName();
							}
						}).toArray(new String[]{});
				String option = ListDialog.showDialog(null, null, 
						"pra onde?", "Migrar o Bicho", 
						options, options[0], options[0]);
				
				System.out.println("Selected Device :"+ option);
				//TODO: Test this and finish it
//				ServiceCall call = new ServiceCall("app","migrate","morfogenese_app");
//				call.addParameter("dna", value)
			}
		});
	}
	
	public void start(Gateway gateway, OntologyStart ontology) {
//		PApplet.main(Morfogenese.class.getName());
//		PApplet.main(new String[] { "--present", Morfogenese.class.getName()});
		this.gateway = gateway;
		PApplet.runSketch(new String[]{"Morfogenese"},  morfogenese);
	}

	public void stop() throws Exception {}

	public void tearDown(OntologyUndeploy ontology) throws Exception {}

	@SuppressWarnings("unchecked")
	public void migrate(Map<String, Object> parameter) {
		this.morfogenese.criaBicho(DNA.fromMap((Map<String, Object>)parameter.get("dna")));
	}

}

interface MorfoClickListener{
	public void perform(Point position, Character key, Object parameter);
}