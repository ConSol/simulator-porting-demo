package com.consol.citrus.simulator.porting;

import com.consol.citrus.simulator.scenario.mapper.ContentBasedXPathScenarioMapper;
import com.consol.citrus.simulator.scenario.mapper.ScenarioMapper;
import com.consol.citrus.simulator.ws.SimulatorWebServiceAdapter;
import com.consol.citrus.xml.namespace.NamespaceContextBuilder;
import org.springframework.stereotype.Component;

@Component
public class PortingSimulatorWebServiceAdapter extends SimulatorWebServiceAdapter {

    private final NamespaceContextBuilder namespaceContextBuilder;

    public PortingSimulatorWebServiceAdapter(NamespaceContextBuilder namespaceContextBuilder) {
        this.namespaceContextBuilder = namespaceContextBuilder;
    }

    @Override
    public ScenarioMapper scenarioMapper() {
        return new ContentBasedXPathScenarioMapper(namespaceContextBuilder)
                .addXPathExpression("//portenv:Request/subscriber/surname");
    }
}
