package org.siemac.metamac.srm.rest.external.v1_0.utils;

import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.statistical_operations.v1_0.domain.Operation;

public class StatisticalOperationsRestMocks {

    public static Operation mockOperation(String identifier) {
        Operation operation = new Operation();
        operation.setId(identifier);

        InternationalString internationalString = new InternationalString();
        internationalString.getTexts().add(RestMocks.mockLocalisedString("es", "title-Tn2JaF3RSI en Espanol"));
        operation.setName(internationalString);

        return operation;
    }
}
