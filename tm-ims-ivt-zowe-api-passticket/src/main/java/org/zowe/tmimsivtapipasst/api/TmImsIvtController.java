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

import java.io.UnsupportedEncodingException;

import javax.resource.ResourceException;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;

@RestController
public class TmImsIvtController {

    @GetMapping("/api/v1")
    public IvtDisplay read(@RequestParam(value = "lastname", defaultValue = "") String name,
    @RequestHeader(value = "authorization", defaultValue = "") String header_auth) {
        /* By now APIML should have provided passticket along with username in the http authorization. 
         * Let's parse it and pass it to IMS service so it can interface with IMS through that passticket. */
        if (header_auth.length() == 0) {
            System.out.println("Request skipped - no http authorization header provided.");
            return null;
        }
        String encoded = header_auth.substring("Basic".length()).trim();
        byte[] decoded = Base64.getDecoder().decode(encoded);
        String credentials = new String(decoded, StandardCharsets.UTF_8);
        String username = credentials.split(":", 2)[0];
        String passticket = credentials.split(":", 2)[1];
        try {
            return new IvtDisplay(name, username, passticket);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ResourceException e) {
            e.printStackTrace();
        }
        return null;
    }
}
