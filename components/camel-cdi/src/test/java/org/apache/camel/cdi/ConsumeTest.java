/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.cdi;

import javax.inject.Inject;

import org.apache.camel.Consume;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.support.MockEndpointInjectedBean;
import org.apache.camel.component.cdi.Mock;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

/**
 * Test consume annotation
 */
public class ConsumeTest extends CdiTestSupport {

    @Inject @Mock
    private MockEndpoint result;

    @Inject @Produce(uri = "seda:start")
    private ProducerTemplate producer;

    @Consume(uri = "seda:start")
    public void onStart(String body) {
        producer.sendBody("mock:result", "Hello " + body + "!");
    }

    @Test
    public void consumeAnnotation() throws Exception {
        assertNotNull("Could not inject producer", producer);
        assertNotNull("Could not inject mock endpoint", result);

        result.expectedBodiesReceived("Hello world!");

        producer.sendBody("world");

        result.assertIsSatisfied();
    }

}
