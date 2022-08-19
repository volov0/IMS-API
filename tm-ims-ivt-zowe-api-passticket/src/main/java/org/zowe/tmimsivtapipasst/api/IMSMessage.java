package org.zowe.tmimsivtapipasst.api;

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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@SuppressWarnings("serial")
public class IMSMessage implements javax.resource.cci.Record, javax.resource.cci.Streamable {
	
	private final int message_length;
	private byte[] buf;   // buffer containing message bytes
	
	/*
	 * Constructor
	 */
	public IMSMessage(int len) {
		message_length = len;
		buf = new byte[message_length];
		
		/* set message length into the first two bytes of message buffer */
		buf[0] = (byte)(len / 256);;
		buf[1] = (byte)(len % 256);
				
	}
	
	
	/**
	 * @param arg Argument to be inserted into message
	 * @param pos Starting position of argument in the message buffer 
	 * @param len Length of argument - fixed
	 * @throws UnsupportedEncodingException 
	 */
	public void setContent(String arg, int pos, int len) throws UnsupportedEncodingException {
		byte [] barg = String.format("%1$-"+len+"s", arg).getBytes("Cp1047");
		System.arraycopy(barg, 0, buf, pos, len);
	}
	
	public String getContent(int pos, int len) throws UnsupportedEncodingException {
		String s = new String(Arrays.copyOfRange(buf, pos, pos+len), "Cp1047");
		return s;
	}

	
	public void dumpBuffer() {
		for (int j = 0; j < buf.length; j++) {
		   System.out.format("%02X", buf[j]);
		}
		System.out.println();
	}
	
	public Object clone() throws CloneNotSupportedException {
		return (super.clone());
	}

	public void read(InputStream ins) throws IOException {
		byte[] input = new byte[ins.available()];
		ins.read(input);
		buf = input;
	}

	public void write(OutputStream outs) throws IOException {
		outs.write(buf, 0, message_length);
		
	}

    public String getRecordName() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRecordShortDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setRecordName(String arg0) {
        // TODO Auto-generated method stub

    }

    public void setRecordShortDescription(String arg0) {
        // TODO Auto-generated method stub

    }

}
