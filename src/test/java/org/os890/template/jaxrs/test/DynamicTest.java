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
import org.apache.tomee.embedded.junit.TomEEEmbeddedSingleRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

//micro-deployment similar to the approach propagated by arquillian
@RunWith(TomEEEmbeddedSingleRunner.class)
public class DynamicTest {
    @Application
    private TestApplication app;

    private WebTarget target;

    @Before
    public void createTarget() {
        ApplicationPath applicationPath = DemoApplication.class.getAnnotation(ApplicationPath.class);
        URI uri = UriBuilder.fromUri("http://127.0.0.1:" + app.getPort() + "/" + TestApplication.SERVICE_ROOT + "/" + applicationPath.value() + "/").build();
        target = ClientBuilder.newClient().target(uri);
    }

    @Test
    public void simpleResource() {
        String responseString = target.path("hello").request().get(String.class);
        Assert.assertEquals("Hello JAX-RS!", responseString);
    }

    @Test
    public void cdiAwareResource() {
        String responseString = target.path("hello-cdi").request().get(String.class);
        Assert.assertEquals("Hello CDI!", responseString);
    }
}