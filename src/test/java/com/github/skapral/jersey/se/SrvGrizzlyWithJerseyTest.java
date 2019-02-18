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
 */
package com.github.skapral.jersey.se;

import com.github.skapral.config.CpStatic;
import com.github.skapral.jersey.se.configs.SimpleConfig;
import com.pragmaticobjects.oo.tests.TestCase;
import com.pragmaticobjects.oo.tests.junit5.TestsSuite;
import org.apache.http.client.methods.HttpGet;

/**
 * Tests suite for {@link SrvGrizzlyWithJerseyTest}.
 * 
 * @author skapral
 */
public class SrvGrizzlyWithJerseyTest extends TestsSuite {
    /**
     * Ctor.
     */
    public SrvGrizzlyWithJerseyTest() {
        super(
            new TestCase(
                "Jersey server responds on endpoint call",
                new AssertAssumingServer(
                    new SrvGrizzlyWithJersey(
                        new CpStatic("20000"),
                        new SimpleConfig(),
                        "api",
                        "stat"
                    ),
                    new AssertHttpEndpointProducesExpectedResponse(
                        () -> new HttpGet("http://localhost:20000/api/status"),
                        200,
                        "OK"
                    )
                )
            ),
            new TestCase(
                "Jersey server responds on static contents call",
                new AssertAssumingServer(
                    new SrvGrizzlyWithJersey(
                        new CpStatic("20001"),
                        new SimpleConfig(),
                        "api",
                        "stat"
                    ),
                    new AssertHttpEndpointProducesExpectedResponse(
                        () -> new HttpGet("http://localhost:20001/stat/file"),
                        200,
                        "Static content"
                    )
                )
            ),
            new TestCase(
                "Server serves api endpoints at context root",
                new AssertAssumingServer(
                    new SrvGrizzlyWithJersey(
                        new CpStatic("20002"),
                        new SimpleConfig(),
                        "/",
                        "stat"
                    ),
                    new AssertHttpEndpointProducesExpectedResponse(
                        () -> new HttpGet("http://localhost:20002/status"),
                        200,
                        "OK"
                    )
                )
            ),
            new TestCase(
                "Server serves static resources at context root",
                new AssertAssumingServer(
                    new SrvGrizzlyWithJersey(
                        new CpStatic("20003"),
                        new SimpleConfig(),
                        "api",
                        "/"
                    ),
                    new AssertHttpEndpointProducesExpectedResponse(
                        () -> new HttpGet("http://localhost:20003/file"),
                        200,
                        "Static content"
                    )
                )
            ),
            new TestCase(
                "Server responds with default page",
                new AssertAssumingServer(
                    new SrvGrizzlyWithJersey(
                        new CpStatic("20004"),
                        new SimpleConfig(),
                        "api",
                        "/"
                    ),
                    new AssertHttpEndpointProducesExpectedResponse(
                        () -> new HttpGet("http://localhost:20004/"),
                        200,
                        "<!doctype html><meta charset=utf-8><title>Hello world</title>"
                    )
                )
            ),
            new TestCase(
                "Server responds with default page on non-root static resource",
                new AssertAssumingServer(
                    new SrvGrizzlyWithJersey(
                        new CpStatic("20005"),
                        new SimpleConfig(),
                        "api",
                        "/"
                    ),
                    new AssertHttpEndpointProducesExpectedResponse(
                        () -> new HttpGet("http://localhost:20005/nonroot"),
                        200,
                        "<!doctype html><meta charset=utf-8><title>Hello world</title>"
                    )
                )
            ),
            new TestCase(
                "Server responds with default page on non-root static context path",
                new AssertAssumingServer(
                    new SrvGrizzlyWithJersey(
                        new CpStatic("20006"),
                        new SimpleConfig(),
                        "api",
                        "static"
                    ),
                    new AssertHttpEndpointProducesExpectedResponse(
                        () -> new HttpGet("http://localhost:20006/static/"),
                        200,
                        "<!doctype html><meta charset=utf-8><title>Hello world</title>"
                    )
                )
            )
        );
    }
}
