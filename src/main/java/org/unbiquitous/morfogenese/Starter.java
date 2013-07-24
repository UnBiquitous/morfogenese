package org.unbiquitous.morfogenese;

import java.util.ListResourceBundle;

import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.network.socket.connectionManager.EthernetTCPConnectionManager;

public class Starter {

	public static void main(String[] args) {
		new UOS().init(new ListResourceBundle() {
			protected Object[][] getContents() {
				return new Object[][] {
						{ "ubiquitos.connectionManager",EthernetTCPConnectionManager.class.getName() },
						{ "ubiquitos.eth.tcp.port", "14984" },
						{ "ubiquitos.eth.tcp.passivePortRange", "14985-15000" },
						{ "ubiquitos.application.deploylist",MorfoApp.class.getName()+"(morfogenese_app)"
					} 
				};
			}
		});

	}

}
