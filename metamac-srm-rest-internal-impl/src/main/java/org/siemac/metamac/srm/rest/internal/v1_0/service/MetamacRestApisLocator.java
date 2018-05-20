package org.siemac.metamac.srm.rest.internal.v1_0.service;

import javax.annotation.PostConstruct;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dataset;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.rest.internal.exception.ServiceExceptionUtils;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.service.StatisticalOperationsRestInternalFacadeV10;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.service.StatisticalResourcesV1_0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metamacRestApisLocator")
public class MetamacRestApisLocator {

    @Autowired
    private SrmConfiguration                           srmConfiguration;

    private StatisticalResourcesV1_0                   statisticalResourcesV1_0;
    private StatisticalOperationsRestInternalFacadeV10 statisticalOperationsRestInternalFacadeV1_0 = null;

    public static final String                               FIELD_EXCLUDE_METADATA                      = "-metadata";
    public static final String                               FIELD_EXCLUDE_DATA                          = "-data";

    @PostConstruct
    public void initService() throws Exception {
        String statisticalOperationsInternalApi = srmConfiguration.retrieveStatisticalOperationsInternalApiUrlBase();
        statisticalOperationsRestInternalFacadeV1_0 = JAXRSClientFactory.create(statisticalOperationsInternalApi, StatisticalOperationsRestInternalFacadeV10.class, null, true);
    }

    public StatisticalResourcesV1_0 getStatisticalResourcesV1_0() throws MetamacException {
        if (statisticalResourcesV1_0 == null) {
            String baseApi = srmConfiguration.retrieveStatisticalResourcesInternalApiUrlBase();
            statisticalResourcesV1_0 = JAXRSClientFactory.create(baseApi, StatisticalResourcesV1_0.class, null, true); // true to do thread safe
        }

        // reset thread context
        WebClient.client(statisticalResourcesV1_0).reset();
        WebClient.client(statisticalResourcesV1_0).accept("application/xml");

        return statisticalResourcesV1_0;
    }

    public StatisticalOperationsRestInternalFacadeV10 getStatisticalOperationsRestInternalFacadeV1_0() {
        // Reset thread context
        WebClient.client(statisticalOperationsRestInternalFacadeV1_0).reset();
        WebClient.client(statisticalOperationsRestInternalFacadeV1_0).accept("application/xml");
        return statisticalOperationsRestInternalFacadeV1_0;
    }

    public Dataset retrieveDatasetVersion(String agencyID, String resourceID, String version) throws MetamacException {
        try {
            return getStatisticalResourcesV1_0().retrieveDataset(agencyID, resourceID, version, null, FIELD_EXCLUDE_METADATA + FIELD_EXCLUDE_DATA, null);
        } catch (Exception e) {
            throw manageSrmInternalRestException(e);
        }
    }

    // -------------------------------------------------------------------------------------------------
    // PRIVATE UTILS
    // -------------------------------------------------------------------------------------------------

    private MetamacException manageSrmInternalRestException(Exception e) throws MetamacException {
        return ServiceExceptionUtils.manageMetamacRestException(e, ServiceExceptionParameters.API_SRM_SDMX, getStatisticalResourcesV1_0());
    }
}
