/*
 * MIT License
 *
 * Copyright (c) 2019 Kapralov Sergey
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */
package com.github.skapral.jersey.se;

import com.github.skapral.jersey.se.config.ConfigProperty;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.ServletRegistration;
import java.io.IOException;

/**
 * Grizzly server with jersey-based endpoints
 *
 * @author Kapralov Sergey
 */
public class SrvGrizzlyWithJersey implements Server {
    private final ConfigProperty port;
    private final ResourceConfig config;

    /**
     * Ctor.
     *
     * @param port Port.
     * @param config Jersey resource config.
     */
    public SrvGrizzlyWithJersey(ConfigProperty port, ResourceConfig config) {
        this.port = port;
        this.config = config;
    }

    @Override
    public final ServerStopHandle start() {
        try {
            WebappContext webappContext = new WebappContext("grizzly web context", "");
            {
                ServletRegistration servletRegistration = webappContext.addServlet("Jersey", new ServletContainer(config));
                servletRegistration.addMapping("/*");
            }
            HttpServer server = HttpServer.createSimpleServer("/", port.optionalValue().map(Integer::valueOf).get());
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
