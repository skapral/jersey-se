/*-
 * ===========================================================================
 * Jersey Standalone
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 Kapralov Sergey
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ============================================================================
 */
package com.github.skapral.jersey.se.configs;

import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Simple JAX-RS config for tests
 * 
 * @author skapral
 */
public class SimpleConfig extends ResourceConfig {
    /**
     * Ctor.
     * @param endpoints JAX-RS annotated endpoints
     */
    public SimpleConfig(Class... endpoints) {
        super(
            endpoints
        );
    }
    
    /**
     * Status endpoint
     */
    @Path("status")
    public static class StatusEndpoint {
        /**
         * @return returns "OK"
         */
        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public final String status() {
            return "OK";
        }
    }
    
    /**
     * File upload endpoint
     */
    @Path("upload")
    public static class FileUploadEndpoint {
        /**
         * @param stream Uploaded byte stream
         * @return returns "OK"
         */
        @POST
        @Produces(MediaType.TEXT_PLAIN)
        public final String uploadFile(@FormDataParam("file") InputStream stream) {
            try {
                return IOUtils.toString(stream, "UTF-8");
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
