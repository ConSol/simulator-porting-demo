/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.simulator;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.simulator.model.ScenarioParameter;
import com.consol.citrus.ws.client.WebServiceClient;
import com.consol.citrus.ws.message.SoapMessageHeaders;
import com.consol.citrus.ws.server.WebServiceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.consol.citrus.simulator.porting.starter.ScenarioParameterHelper.*;

@Test
public class SimulatorWebServiceIT extends TestNGCitrusTestDesigner {

    /**
     * SOAP client for sending SOAP requests to the simulator
     */
    @Autowired
    @Qualifier("simulatorClient")
    private WebServiceClient soapClient;

    /**
     * SOAP server for reveiving SOAP requests from the simulator
     */
    @Autowired
    @Qualifier("simulatorServer")
    private WebServiceServer soapServer;

    /**
     * REST client for sending HTTP/JSON requests to the simulator
     */
    @Autowired
    @Qualifier("simulatorRestEndpoint")
    protected HttpClient restEndpoint;

    /**
     * Tests the default acknowledgement scenario
     */
    @CitrusTest
    public void testAcknowledgement() {
        variable(REQUEST_ID, "citrus:randomString(10)");

        echo("Sending Request ...");
        send(soapClient)
                .payload("<env:Request xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <requestId>" + placeholder(REQUEST_ID) + "</requestId>\n" +
                        "    <receivingCarrier>ABC</receivingCarrier>\n" +
                        "    <donatingCarrier>DEF</donatingCarrier>\n" +
                        "    <subscriber>\n" +
                        "        <forename>Joe</forename>\n" +
                        "        <surname>Bloggs</surname>\n" +
                        "        <dateOfBirth>11.11.2011</dateOfBirth>\n" +
                        "        <phoneNumber>12345678</phoneNumber>\n" +
                        "    </subscriber>\n" +
                        "</env:Request>")
                .header("citrus_soap_action", "http://www.citrusframework.org/simulator/porting/sendNegotiationRequest");

        echo("Receving Acknowledgement ...");
        receive(soapClient)
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");
    }

    /**
     * Tests the DonatingCarrierScenario
     */
    @CitrusTest
    public void testDonatingCarrierScenario() {
        variable(REQUEST_ID, "citrus:randomString(10)");

        echo("Sending Request ...");
        send(soapClient)
                .payload("<env:Request xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <requestId>" + placeholder(REQUEST_ID) + "</requestId>\n" +
                        "    <receivingCarrier>ABC</receivingCarrier>\n" +
                        "    <donatingCarrier>DEF</donatingCarrier>\n" +
                        "    <subscriber>\n" +
                        "        <forename>Joe</forename>\n" +
                        "        <surname>DonatingCarrier</surname>\n" +
                        "        <dateOfBirth>11.11.2011</dateOfBirth>\n" +
                        "        <phoneNumber>12345678</phoneNumber>\n" +
                        "    </subscriber>\n" +
                        "</env:Request>")
                .header("citrus_soap_action", "http://www.citrusframework.org/simulator/porting/sendNegotiationRequest");

        echo("Receving Acknowledgement ...");
        receive(soapClient)
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");

        echo("Receiving Porting Response ...");
        receive(soapServer)
                .header(SoapMessageHeaders.SOAP_ACTION, "http://www.citrusframework.org/simulator/porting/sendNegotiationResponse")
        ;

        echo("Sending Acknowledgement ...");
        send(soapServer)
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");
    }

    /**
     * Tests the DonatingCarrierScenario
     */
    @CitrusTest
    public void testReceivingCarrierScenario() {
        http()
                .client(restEndpoint)
                .send()
                .post("/api/scenario/launch/ReceivingCarrier")
                .contentType("application/json")
                .payload(asJson(requestId(),
                        receivingCarrier(),
                        donatingCarrier(),
                        forename(),
                        phoneNumber(),
                        dateOfBirth())
                );

        http()
                .client(restEndpoint)
                .receive().response(HttpStatus.OK);


        echo("Receiving PortingRequest ...");
        receive(soapServer)
                .header(SoapMessageHeaders.SOAP_ACTION, "http://www.citrusframework.org/simulator/porting/sendNegotiationRequest")
                .extractFromPayload("//requestId", REQUEST_ID)
                .extractFromPayload("//receivingCarrier", RECEIVING_CARRIER)
                .extractFromPayload("//donatingCarrier", DONATING_CARRIER)
        ;

        echo("Sending Acknowledgement ...");
        send(soapServer)
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");

        echo("Sending PortingResponse ...");
        send(soapClient)
                .payload("<env:Response xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <requestId>" + placeholder(REQUEST_ID) + "</requestId>\n" +
                        "    <receivingCarrier>" + placeholder(RECEIVING_CARRIER) + "</receivingCarrier>\n" +
                        "    <donatingCarrier>" + placeholder(DONATING_CARRIER) + "</donatingCarrier>\n" +
                        "    <code>APPROVED</code>\n" +
                        "    <text></text>\n" +
                        "</env:Response>")
                .header("citrus_soap_action", "http://www.citrusframework.org/simulator/porting/sendNegotiationResponse");

        echo("Receving Acknowledgement ...");
        receive(soapClient)
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");
    }

    private String asJson(ScenarioParameter... scenarioParameters) {
        final Jackson2JsonObjectMapper mapper = new Jackson2JsonObjectMapper();
        try {
            return mapper.toJson(Arrays.asList(scenarioParameters));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
