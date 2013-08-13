package org.unbiquitous.morfogenese;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.unbiquitous.json.JSONException;
import org.unbiquitous.json.JSONObject;
import org.unbiquitous.uos.core.UOSLogging;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.applicationManager.CallContext;
import org.unbiquitous.uos.core.applicationManager.UosApplication;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceCall;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceCall.ServiceType;
import org.unbiquitous.uos.core.messageEngine.messages.ServiceResponse;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyDeploy;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyStart;
import org.unbiquitous.uos.core.ontologyEngine.api.OntologyUndeploy;

import processing.core.PApplet;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import docs.oracle.com.example.ListDialog;

public class MorfoApp implements UosApplication {

	private static final Logger logger = UOSLogging.getLogger();
	
	protected Morfogenese morfogenese;
	protected Gateway gateway;
	protected Set<Bicho> migrando = new HashSet<Bicho>(); 

	public void init(OntologyDeploy ontology, String appId) {
		this.morfogenese = new Morfogenese();
		this.morfogenese.setMListener(new MorfoClickListener() {
			
			public void perform(Point position, Character key, final Object param) {
				new Thread(new Runnable() {
					public void run() {
						synchronized (migrando) {
							if(migrando.contains(param)){
								return;
							}
							migrando.add((Bicho) param);
						}
						migrate(param, selectDevice());
						synchronized (migrando) {
							migrando.remove((Bicho) param);
						}
					}
				}).start();
				
			}

			private void migrate(Object param, UpDevice selectedDevice) {
				if(selectedDevice == null){
					return;
				}
				//TODO: Test this and finish it
				ServiceCall call = new ServiceCall("app","migrate","morfogenese_app");
				call.setChannels(1);
				call.addParameter("dna", ((Bicho)param).dna.toMap());
				call.setServiceType(ServiceType.STREAM);
				try {
					ServiceResponse r = gateway.callService(selectedDevice, call);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DataOutputStream outChannel = r.getMessageContext().getDataOutputStream(0);
					ObjectOutputStream writer_agent = new ObjectOutputStream(outChannel);
					writer_agent.writeObject(param);
					writer_agent.close();
					morfogenese.removeBicho((Bicho) param);
				} catch (ServiceCallException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			private UpDevice selectDevice() {
				List<UpDevice> listDevices = gateway.listDevices();
				String[] options = 
						Lists.transform(listDevices, new Function<UpDevice, String>() {
							public String apply(UpDevice input) {
								return input.getName();
							}
						}).toArray(new String[]{});
				String option = ListDialog.showDialog(null, null, 
						"pra onde?", "Migrar o Bicho", 
						options, null, null);
				System.out.println("Option:"+option);
				if (option == null) return null;
				UpDevice selectedDevice = null;
				for(UpDevice d : gateway.listDevices()){
					if (option.equalsIgnoreCase(d.getName())){
						selectedDevice = d;
						break;
					}
				}
				System.out.println("Device:"+selectedDevice);
				return selectedDevice;
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
	public ServiceResponse migrate(ServiceCall call, CallContext ctx) {
		Map<String, Object> parameter = call.getParameters();
		if (call.getServiceType() == ServiceType.STREAM ){
				final DataInputStream agent = ctx.getDataInputStream();
				new Thread(new Runnable() {
					public void run() {
						try {
							while (agent.available() == 0){/* busy waiting*/}
							ObjectInputStream reader = new ObjectInputStream(agent);
							Bicho bicho = (Bicho) reader.readObject();
							MorfoApp.this.morfogenese.adicionaBicho(bicho);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
		}else{
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
		return new ServiceResponse();
	}

}

interface MorfoClickListener{
	public void perform(Point position, Character key, Object parameter);
}