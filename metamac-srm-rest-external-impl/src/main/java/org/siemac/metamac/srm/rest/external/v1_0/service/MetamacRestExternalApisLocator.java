package org.siemac.metamac.srm.rest.external.v1_0.service;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.statistical_operations.rest.external.v1_0.service.StatisticalOperationsV1_0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metamacRestExternalApisLocator")
public class MetamacRestExternalApisLocator {

    @Autowired
    private SrmConfiguration          srmConfiguration;

    private StatisticalOperationsV1_0 statisticalOperationsV1_0 = null;

    public static final String              FIELD_EXCLUDE_METADATA    = "-metadata";
    public static final String              FIELD_EXCLUDE_DATA        = "-data";

    @PostConstruct
    public void initService() throws Exception {
        String statisticalOperationsInternalApi = srmConfiguration.retrieveStatisticalOperationsExternalApiUrlBase();
        statisticalOperationsV1_0 = JAXRSClientFactory.create(statisticalOperationsInternalApi, StatisticalOperationsV1_0.class, null, true);
    }

    public StatisticalOperationsV1_0 getStatisticalOperationsV1_0() {
        // Reset thread context
        WebClient.client(statisticalOperationsV1_0).reset();
        WebClient.client(statisticalOperationsV1_0).accept("application/xml");
        return statisticalOperationsV1_0;
    }
}
