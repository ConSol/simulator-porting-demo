package com.consol.citrus.simulator.porting.scenario;

import com.consol.citrus.simulator.scenario.AbstractSimulatorScenario;
import com.consol.citrus.simulator.scenario.Scenario;
import com.consol.citrus.simulator.scenario.ScenarioDesigner;
import com.consol.citrus.ws.message.SoapMessageHeaders;

import static com.consol.citrus.simulator.porting.starter.ScenarioParameterHelper.REQUEST_ID;

@Scenario("Acknowledgement")
public class Acknowledgement extends AbstractSimulatorScenario {

    @Override
    public void run(ScenarioDesigner scenario) {
        scenario.echo("Receiving porting request or response ...");
        scenario
                .receive()
                .header(SoapMessageHeaders.SOAP_ACTION, "http://www.citrusframework.org/simulator/porting/sendNegotiationRequest")
                .extractFromPayload("//requestId", REQUEST_ID)
        ;

        scenario.echo("Sending acknowledgement for request with id '${requestId}' ...");
        scenario
                .send()
                .payload("<env:Acknowledgement xmlns:env=\"http://www.citrusframework.org/simulator/porting/envelope\">\n" +
                        "    <status>OK</status>\n" +
                        "    <text></text>\n" +
                        "</env:Acknowledgement>");
    }
}
