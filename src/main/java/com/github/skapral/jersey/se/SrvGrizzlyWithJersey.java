/*-
 * ===========================================================================
 * Jersey Standalone
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright (C) 2019 - 2020 Kapralov Sergey
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
package com.github.skapral.jersey.se;

import com.github.skapral.config.ConfigProperty;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletRegistration;
import java.io.IOException;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;

/**
 * Grizzly server with jersey-based endpoints
 *
 * @author Kapralov Sergey
 */
public class SrvGrizzlyWithJersey implements Server {
    private final ConfigProperty port;
    private final ResourceConfig config;
    private final String apiRoot;
    private final String staticRoot;

    /**
     * Ctor.
     * 
     * @param port Port.
     * @param config Jersey resource config.
     * @param apiRoot context root for endpoint calls
     * @param staticRoot context root for static files access
     */
    public SrvGrizzlyWithJersey(ConfigProperty port, ResourceConfig config, String apiRoot, String staticRoot) {
        this.port = port;
        this.config = config;
        this.apiRoot = apiRoot;
        this.staticRoot = staticRoot;
    }
    
    /**
     * Ctor.
     * 
     * @param port Port.
     * @param config Jersey resource config.
     * @param apiRoot context root for endpoint calls
     */
    private SrvGrizzlyWithJersey(ConfigProperty port, ResourceConfig config, String apiRoot) {
        this(
            port,
            config,
            apiRoot,
            "/"
        );
    }
    
    /**
     * Ctor.
     *
     * @param port Port.
     * @param config Jersey resource config.
     */
    public SrvGrizzlyWithJersey(ConfigProperty port, ResourceConfig config) {
        this(
            port,
            config,
            "/api",
            "/"
        );
    }

    @Override
    public final ServerStopHandle start() {
        try {
            final String apiRoot = this.apiRoot.startsWith("/") ? this.apiRoot : "/" + this.apiRoot;
            final String staticRoot = this.staticRoot.startsWith("/") ? this.staticRoot : "/" + this.staticRoot;
            final WebappContext webappContext = new WebappContext("grizzly web context", "");
            {
                ServletRegistration servletRegistration = webappContext.addServlet("Jersey", new ServletContainer(config));
                servletRegistration.addMapping(
                    apiRoot.endsWith("/") ? apiRoot + "*" : apiRoot + "/*"
                );
            }
            final HttpServer server = HttpServer.createSimpleServer(null, port.optionalValue().map(Integer::valueOf).get());
            final HttpHandler httpHandler = new CLStaticHttpHandler(this.getClass().getClassLoader(), "/static/");
            server.getServerConfiguration().addHttpHandler(
                httpHandler,
                staticRoot.endsWith("/") ? staticRoot + "*" : staticRoot + "/*"
            );
            webappContext.deploy(server);
            server.start();
            return () -> {
                server.shutdownNow();
            };
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
