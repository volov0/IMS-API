package org.sampleapi.tmimsivtapi.api;

/*-
 * ===========================LICENSE_START====================================
 * tm-ims-ivt
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

import javax.resource.cci.Connection;
import java.io.UnsupportedEncodingException;
import javax.resource.ResourceException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.ibm.connector2.ims.ico.IMSConnectionFactory; //imsico.jar
import com.ibm.connector2.ims.ico.IMSInteraction; //imsico.jar
import com.ibm.connector2.ims.ico.IMSInteractionSpec; //imsico.jar
import com.ibm.connector2.ims.ico.IMSManagedConnectionFactory; //imsico.jar

public class IvtDisplay {
    private String message;
    private String lastname;
    private String firstname;
    private String extension;
    private String zipcode;
    
    public IvtDisplay(String lastname) throws ResourceException, UnsupportedEncodingException {
        this.lastname = lastname;
        this.message = "---";
        this.firstname = "---";
        this.extension = "---";
        this.zipcode = "---";

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

        //Create connection factory from ManagedConnectionFactory
		cf = (IMSConnectionFactory) mcf.createConnectionFactory();
		
		// Create an IMSConnection object
	    Connection connection = cf.getConnection();

		// Create an IMSInteraction from the connection 
	    IMSInteraction interaction = (IMSInteraction) connection.createInteraction();
	    
	    // Create an IMSConnection object
	    IMSInteractionSpec ixnSpec = new IMSInteractionSpec();

	    // Doing non-conversational IMS transaction - input message send to IMS -> IMS replies with output message 
	    ixnSpec.setInteractionVerb(IMSInteractionSpec.SYNC_SEND_RECEIVE);
	    
	    // I'm gonna wait one second
	    ixnSpec.setExecutionTimeout(1000); //how long to wait for an answer from IMS
	    //printIMSSpecs(ixnSpec);
	    
	    
	    // IVTNO transaction accepts 59 bytes long input message
	    IMSMessage inputMessage = new IMSMessage(59);
		
	    // I need to put the input verbs to the right places in the message
	    inputMessage.setContent("IVTNO", 4, 10);
	    inputMessage.setContent("DISPLAY", 14, 8);
	    inputMessage.setContent(lastname, 22, 10);
	    inputMessage.setContent(" ", 32, 27);   // fill the rest with blanks 
		inputMessage.dumpBuffer();

	    // IVTNO transaction responds wit 93bytes long input message
        IMSMessage outputMessage = new IMSMessage(93);
        
	    //run the IMS interaction
	    try {
	    	interaction.execute( ixnSpec, inputMessage, outputMessage );
	    	
	    	//printIMSSpecs(ixnSpec);

            // Display the output
            outputMessage.dumpBuffer();
	    	System.out.println("Message: " + outputMessage.getContent(4, 40));	    	
		    System.out.println("Lastname: " + outputMessage.getContent(52, 10));
		    System.out.println("Firstname: " + outputMessage.getContent(62, 10));
		    System.out.println("Extension: " + outputMessage.getContent(72, 10));
            System.out.println("Zipcode: " + outputMessage.getContent(82, 7));
            this.message = outputMessage.getContent(4, 40);
            this.firstname = outputMessage.getContent(62, 10);
            this.extension = outputMessage.getContent(72, 10);
            this.zipcode = outputMessage.getContent(82, 7);
            
	    	
	    } catch (ResourceException e) {
			e.printStackTrace();
		} finally {
		    //Close both the interaction and the connection
		    interaction.close();
		    connection.close();
		}
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMessage() {
        return message;
    }

    public String getExtension() {
        return extension;
    }

    public String getZipcode() {
        return zipcode;
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

}
