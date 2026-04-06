/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.os890.template.jaxrs.test;

import org.os890.template.jaxrs.DemoApplication;
import org.apache.openejb.testing.Application;
import org.apache.tomee.embedded.junit.jupiter.RunWithTomEEEmbedded;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Integration test that verifies JAX-RS resources using TomEE embedded
 * with a fixed port.
 */
@RunWithTomEEEmbedded
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SimpleTest {

    @Application
    private TestApplication app;

    private WebTarget target;

    /**
     * Creates the JAX-RS client target using the default port.
     */
    @BeforeEach
    void createTarget() {
        ApplicationPath applicationPath = DemoApplication.class.getAnnotation(ApplicationPath.class);
        URI uri = UriBuilder.fromUri("http://127.0.0.1:" + app.getPort()
                + "/" + TestApplication.SERVICE_ROOT + "/" + applicationPath.value() + "/").build();
        target = ClientBuilder.newClient().target(uri);
    }

    /**
     * Verifies that the simple hello resource responds correctly.
     */
    @Test
    void simpleResource() {
        String responseString = target.path("hello").request().get(String.class);
        Assertions.assertEquals("Hello JAX-RS!", responseString);
    }

    /**
     * Verifies that the CDI-aware resource responds with the injected bean value.
     */
    @Test
    void cdiAwareResource() {
        String responseString = target.path("hello-cdi").request().get(String.class);
        Assertions.assertEquals("Hello CDI!", responseString);
    }
}
