package com.consol.citrus.simulator.porting.scenario;

import com.consol.citrus.simulator.scenario.AbstractSimulatorScenario;
import com.consol.citrus.simulator.scenario.Scenario;
import com.consol.citrus.simulator.scenario.ScenarioDesigner;
import com.consol.citrus.ws.client.WebServiceClient;
import com.consol.citrus.ws.message.SoapMessageHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import static com.consol.citrus.simulator.porting.starter.ScenarioParameterHelper.*;

@Scenario("DonatingCarrier")
public class DonatingCarrier extends AbstractSimulatorScenario {

    @Autowired
    private WebServiceClient webServiceClient;

    @Override
    public void run(ScenarioDesigner scenario) {
        scenario.echo("Receiving PortingRequest ...");
        scenario
                .receive()
                .header(SoapMessageHeaders.SOAP_ACTION, "http://www.citrusframework.org/simulator/porting/sendNegotiationRequest")
                .extractFromPayload("//requestId", REQUEST_ID)
                .extractFromPayload("//receivingCarrier", RECEIVING_CARRIER)
                .extractFromPayload("//donatingCarrier", DONATING_CARRIER)
        ;

        scenario.echo("Sending Acknowledgement ...");
        scenario
                .send()
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");

        scenario.echo("Sending PortingResponse ...");
        scenario.send(webServiceClient)
                .payload("<env:Response xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <requestId>" + placeholder(REQUEST_ID) + "</requestId>\n" +
                        "    <receivingCarrier>" + placeholder(RECEIVING_CARRIER) + "</receivingCarrier>\n" +
                        "    <donatingCarrier>" + placeholder(DONATING_CARRIER) + "</donatingCarrier>\n" +
                        "    <code>APPROVED</code>\n" +
                        "    <text></text>\n" +
                        "</env:Response>")
                .header("citrus_soap_action", "http://www.citrusframework.org/simulator/porting/sendNegotiationResponse");

        scenario.echo("Receving Acknowledgement ...");
        scenario.receive(webServiceClient)
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");
    }
}
