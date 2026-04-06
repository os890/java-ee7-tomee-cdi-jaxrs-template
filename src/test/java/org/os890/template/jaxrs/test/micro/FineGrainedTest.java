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

package org.os890.template.jaxrs.test.micro;

import org.os890.template.cdi.ApplicationScopedBean;
import org.os890.template.jaxrs.DemoApplication;
import org.os890.template.jaxrs.service.CdiAwareResource;
import org.os890.template.jaxrs.service.HelloResource;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit5.RunWithApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Default;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.Module;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Fine-grained integration test using ApplicationComposer for explicit
 * micro-deployment of individual classes.
 *
 * <p>Only the classes listed in the {@link Classes} annotation are deployed,
 * giving precise control over the test classpath.</p>
 */
@Default
@EnableServices(jaxrs = true)
@RunWithApplicationComposer
public class FineGrainedTest {

    /** The context root for this micro-deployment. */
    public static final String SERVICE_ROOT = "serviceRoot";

    private static WebTarget target;

    /**
     * Defines the micro-deployment with explicit bean classes.
     *
     * @return a {@link WebApp} configured with the service root context
     */
    @Module
    @Classes(cdi = true, value = {DemoApplication.class, HelloResource.class,
            CdiAwareResource.class, ApplicationScopedBean.class})
    public WebApp war() {
        return new WebApp().contextRoot(SERVICE_ROOT);
    }

    /**
     * Creates the JAX-RS client target for the micro-deployment.
     */
    @BeforeAll
    static void createTarget() {
        ApplicationPath applicationPath = DemoApplication.class.getAnnotation(ApplicationPath.class);
        URI uri = UriBuilder.fromUri("http://127.0.0.1:4204/"
                + SERVICE_ROOT + "/" + applicationPath.value() + "/").build();
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
