package org.zowe.dbimszoweapi.api;

/*-
 * ===========================LICENSE_START====================================
 * db-ims
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.resource.ResourceException;

//import com.ibm.ims.jdbc.IMSDataSource;


public class DBDisplay {
    private String lastname;
    private String firstname;
	private String extension;
	private String zipcode;
    
    public DBDisplay(String lastname) throws ResourceException, UnsupportedEncodingException {
        this.lastname = lastname;
        this.firstname = "---";
		this.extension = "---";
		this.zipcode = "---";

		// Read configuration file containing hostname, username and password
		Properties props = readConfig();
		if (props == null) {
			System.out.println("Cannot continue.");
			return;
		}

		try {
    		Class.forName("com.ibm.ims.jdbc.IMSDriver");
    	} catch (ClassNotFoundException e1) {
    		System.out.println( "class not found "+e1.toString());
		}

		// set parameters for IMS Connect connection
		//String url = "jdbc:ims://"+props.getProperty("hostname")+":6667/P200G";
		String url = "jdbc:ims://"+props.getProperty("hostname")+":6667/VKIVD2";

		try {
			Connection conn = DriverManager.getConnection(url);
			//PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM ENGINE WHERE NAME = ?");
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM PHONEBOOK WHERE LASTNAME = ?");
			preparedStatement.setString(1, lastname);
			ResultSet rs = preparedStatement.executeQuery();
    		while (rs.next()) {
				System.out.println("Firstname: " + rs.getString("FIRSTNAME"));
				System.out.println("Extension: " + rs.getString("EXTENSION"));
				System.out.println("Zipcode: " + rs.getString("ZIPCODE"));
				this.firstname = rs.getString("FIRSTNAME");
				this.extension = rs.getString("EXTENSION");
				this.zipcode = rs.getString("ZIPCODE");
				rs.close();
  				conn.close();
				break;
    		}
    	} catch (SQLException e) {
    		System.out.println( "jdbc error"+e.toString());
    	}
    }

    public String getLastName() {
        return lastname;
    }

    public String getFirstName() {
        return firstname;
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
