package com.consol.citrus.simulator.porting;

import com.consol.citrus.endpoint.adapter.mapping.MappingKeyExtractor;
import com.consol.citrus.simulator.annotation.SimulatorWebServiceAdapter;
import com.consol.citrus.simulator.mapper.ContentBasedXPathScenarioMapper;
import com.consol.citrus.xml.namespace.NamespaceContextBuilder;
import org.springframework.stereotype.Component;

@Component
public class PortingSimulatorWebServiceAdapter extends SimulatorWebServiceAdapter {

    private final NamespaceContextBuilder namespaceContextBuilder;

    public PortingSimulatorWebServiceAdapter(NamespaceContextBuilder namespaceContextBuilder) {
        this.namespaceContextBuilder = namespaceContextBuilder;
    }

    @Override
    public MappingKeyExtractor mappingKeyExtractor() {
        return new ContentBasedXPathScenarioMapper(namespaceContextBuilder)
                .addXPathExpression("//portenv:Request/subscriber/surname");
    }
}
