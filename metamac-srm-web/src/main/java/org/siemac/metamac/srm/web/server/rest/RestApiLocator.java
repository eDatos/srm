package org.siemac.metamac.srm.web.server.rest;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.service.StatisticalOperationsRestInternalFacadeV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestApiLocator {

    @Autowired
    private ConfigurationService                       configurationService;

    private StatisticalOperationsRestInternalFacadeV10 statisticalOperationsRestInternalFacadeV10 = null;

    @PostConstruct
    public void initService() throws Exception {
        String baseApi = configurationService.getProperties().getProperty(RestApiConstants.STATISTICAL_OPERATIONS_REST_INTERNAL);
        statisticalOperationsRestInternalFacadeV10 = JAXRSClientFactory.create(baseApi, StatisticalOperationsRestInternalFacadeV10.class, null, true); // true to do thread safe
    }

    public StatisticalOperationsRestInternalFacadeV10 getStatisticalOperationsRestFacadeV10() {
        // Reset thread context
        WebClient.client(statisticalOperationsRestInternalFacadeV10).reset();
        WebClient.client(statisticalOperationsRestInternalFacadeV10).accept("application/xml");
        return statisticalOperationsRestInternalFacadeV10;
    }

}
