package org.unbiquitous.morfogenese;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import org.unbiquitous.json.JSONException;
import org.unbiquitous.json.JSONObject;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.applicationManager.UosApplication;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceCall;
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
				
				UpDevice selectedDevice = null;
				for(UpDevice d : gateway.listDevices()){
					if (option.equalsIgnoreCase(d.getName())){
						selectedDevice = d;
						break;
					}
				}
				
				System.out.println("Selected Device :"+ option);
				//TODO: Test this and finish it
				ServiceCall call = new ServiceCall("app","migrate","morfogenese_app");
				call.addParameter("dna", ((Bicho)param).dna.toMap());
				try {
					gateway.callService(selectedDevice, call);
					morfogenese.removeBicho((Bicho) param);
				} catch (ServiceCallException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void start(Gateway gateway, OntologyStart ontology) {
		this.gateway = gateway;
		PApplet.runSketch(new String[]{"Morfogenese"},  morfogenese);
	}

	public void stop() throws Exception {}

	public void tearDown(OntologyUndeploy ontology) throws Exception {}

	@SuppressWarnings("unchecked")
	public void migrate(Map<String, Object> parameter) {
		try {
			Object paramObj = parameter.get("dna");
			Map<String, Object> dnaMap;
			if (paramObj instanceof JSONObject){
				dnaMap = ((JSONObject) paramObj).toMap();
			}else{
				dnaMap = (Map<String, Object>) paramObj;
			}
			DNA dna = DNA.fromMap(dnaMap);
			this.morfogenese.criaBicho(dna);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

interface MorfoClickListener{
	public void perform(Point position, Character key, Object parameter);
}