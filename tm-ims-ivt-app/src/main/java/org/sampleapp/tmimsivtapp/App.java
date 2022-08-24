package org.sampleapp.tmimsivtapp;

/*-
 * ===========================LICENSE_START====================================
 * tm-ims-IVT
 * ---
 * Copyright (C) 2019 Broadcom
 * ---
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 2
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 * =============================LICENSE_END=====================================
 */

/*
 * Following imports are from IMS TM Resource adapter ims1516.rar which can be either downloaded or taken from SMP/E:
 *     ims1516.rar for this code following is required: imsico.jar, ccf2.jar, IMSLogin.jar, CWYBS_AdapterFoundation.jar
 */
import com.ibm.connector2.ims.ico.IMSConnectionFactory; 
import com.ibm.connector2.ims.ico.IMSInteraction; 
import com.ibm.connector2.ims.ico.IMSInteractionSpec; 
import com.ibm.connector2.ims.ico.IMSManagedConnectionFactory; 

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.resource.cci.Connection;


@SuppressWarnings("unused")

public class App {

	/**
	 * Constructs a new IMSIVP object.
	 */
	public App() {
	}

	public static void main(String[] args) throws Exception {
		String lastname = "LAST1"; // set basic default
		if (args.length >= 1) {
			lastname = args[0];
		}
		App ivtapp = new App();
		ivtapp.run(lastname);
		
	}

	/**
	 * Invoke the transaction IVTNO command DISPLAY
	 * 
	 * @throws Exception
	 */
	public void run(String lastname) throws Exception {
		IMSConnectionFactory cf;
		IMSManagedConnectionFactory mcf = new IMSManagedConnectionFactory();

		// Read configuration file containing hostname, username and password
		Properties props = readConfig();
		if (props == null) {
			System.out.println("Cannot continue.");
			return;
		}

		// set parameters for IMS Connect connection
		mcf.setHostName(props.getProperty("hostname"));
		mcf.setUserName(props.getProperty("username"));
		mcf.setPassword(props.getProperty("password"));
		mcf.setDataStoreName(props.getProperty("datastore"));
		mcf.setPortNumber(Integer.parseInt(props.getProperty("port")));

		// Create connection factory from ManagedConnectionFactory
		cf = (IMSConnectionFactory) mcf.createConnectionFactory();

		// Create an IMSConnection object
		Connection connection = cf.getConnection();

		// Create an IMSInteraction from the connection
		IMSInteraction interaction = (IMSInteraction) connection.createInteraction();

		// Create an IMSInteraction specification object
		IMSInteractionSpec ixnSpec = new IMSInteractionSpec();

		// Doing non-conversational IMS transaction - input message send to IMS -> IMS
		// replies with output message
		ixnSpec.setImsRequestType(IMSInteractionSpec.IMS_REQUEST_TYPE_IMS_TRANSACTION);
		ixnSpec.setCommitMode(IMSInteractionSpec.SEND_THEN_COMMIT);
		ixnSpec.setInteractionVerb(IMSInteractionSpec.SYNC_SEND_RECEIVE);
		ixnSpec.setSyncLevel(IMSInteractionSpec.SYNC_LEVEL_NONE);

		// I'm gonna wait one second
		ixnSpec.setExecutionTimeout(1000); // how long to wait for an answer from IMS
		//printIMSSpecs(ixnSpec);

		// IVTNO transaction accepts 59 bytes long input message
		IMSMessage inputMessage = new IMSMessage(59);

		// I need to put the input verbs to the right places in the message
		inputMessage.setContent("IVTNO", 4, 10);
		inputMessage.setContent("DISPLAY", 14, 8);
		inputMessage.setContent(lastname, 22, 10);
		inputMessage.setContent(" ", 32, 27); // fill the rest with blanks
		inputMessage.dumpBuffer();

		// IVTNO transaction responds wit 93bytes long input message
		IMSMessage outputMessage = new IMSMessage(93);

		// run the IMS interaction
		try {
			interaction.execute(ixnSpec, inputMessage, outputMessage);
			outputMessage.dumpBuffer();

			// printIMSSpecs(ixnSpec);

			// Display the output
			System.out.println("Message: " + outputMessage.getContent(4, 40));
			System.out.println("Lastname: " + outputMessage.getContent(52, 10));
			System.out.println("Firstname: " + outputMessage.getContent(62, 10));
			System.out.println("Extension: " + outputMessage.getContent(72, 10));
			System.out.println("Zipcode: " + outputMessage.getContent(82, 7));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close both the interaction and the connection
			interaction.close();
			connection.close();
		}
	}

	private Properties readConfig() {
		Properties props = new Properties();

		FileInputStream file;
		String path = "./main.properties";
		try {
			file = new FileInputStream(path);
			props.load(file);
			file.close();
			return props;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
  
	private void printIMSSpecs(IMSInteractionSpec is) {
		System.out.println("AltClientID: "+is.getAltClientID());
		System.out.println("CalloutRequestType: "+is.getCalloutRequestType());
		System.out.println("CommitMode: "+is.getCommitMode());
		System.out.println("ConvID: "+is.getConvID());
		System.out.println("DataStoreName: "+is.getDataStoreName());
		System.out.println("ExecutionTimeout: "+is.getExecutionTimeout());
		System.out.println("ImsRequestType: "+is.getImsRequestType());
		System.out.println("InteractionVerb: "+is.getInteractionVerb());
		System.out.println("LtermName: "+is.getLtermName());
		System.out.println("MapName: "+is.getMapName());
		System.out.println("RealmName: "+is.getRealmName());
		System.out.println("ReRouteName: "+is.getReRouteName());
		System.out.println("SecurityName: "+is.getSecurityName());
		System.out.println("SocketTimeout: "+is.getSocketTimeout());
		System.out.println("SyncCalloutStatusCode: "+is.getSyncCalloutStatusCode());
		System.out.println("SyncLevel: "+is.getSyncLevel());
		System.out.println("TrackingID: "+is.getTrackingID());
		System.out.println("hashCode: "+is.hashCode());
		System.out.println("ResumeTpipeNSC: "+is.getResumeTpipeNSC());
	}
}
