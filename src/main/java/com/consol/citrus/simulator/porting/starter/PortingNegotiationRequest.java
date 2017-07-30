package com.consol.citrus.simulator.porting.starter;

import com.consol.citrus.simulator.model.ScenarioParameter;
import com.consol.citrus.simulator.porting.SoapActions;
import com.consol.citrus.simulator.scenario.AbstractScenarioStarter;
import com.consol.citrus.simulator.scenario.ScenarioDesigner;
import com.consol.citrus.simulator.scenario.Starter;
import com.consol.citrus.ws.client.WebServiceClient;
import com.consol.citrus.ws.message.SoapMessageHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;

import static com.consol.citrus.simulator.porting.starter.ScenarioParameterHelper.*;

@Starter("PortingNegotiationRequest")
public class PortingNegotiationRequest extends AbstractScenarioStarter {

    @Autowired
    private WebServiceClient webServiceClient;

    @Override
    public void run(ScenarioDesigner scenario) {
        scenario.echo("Sending Porting Request ...");
        scenario.send(webServiceClient)
                .payload("<env:Request xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <requestId>" + placeholder(REQUEST_ID) + "</requestId>\n" +
                        "    <receivingCarrier>" + placeholder(RECEIVING_CARRIER) + "</receivingCarrier>\n" +
                        "    <donatingCarrier>" + placeholder(DONATING_CARRIER) + "</donatingCarrier>\n" +
                        "    <subscriber>\n" +
                        "        <forename>" + placeholder(FORENAME) + "</forename>\n" +
                        "        <surname>" + placeholder(SURNAME) + "</surname>\n" +
                        "        <dateOfBirth>" + placeholder(DATE_OF_BIRTH) + "</dateOfBirth>\n" +
                        "        <phoneNumber>" + placeholder(PHONE_NUMBER) + "</phoneNumber>\n" +
                        "    </subscriber>\n" +
                        "</env:Request>")
                .header(SoapMessageHeaders.SOAP_ACTION, SoapActions.SEND_NEGOTIATION_REQUEST_ACTION);

        scenario.echo("Receving Acknowledgement ...");
        scenario.receive(webServiceClient)
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");
    }

    @Override
    public Collection<ScenarioParameter> getScenarioParameters() {
        return Arrays.asList(
                requestId(),
                receivingCarrier(),
                donatingCarrier(),
                forename(),
                surname(),
                phoneNumber(),
                dateOfBirth()
        );
    }
}
