package org.siemac.metamac.srm.rest.internal.v1_0.service;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.statistical_resources_internal.v1_0.domain.Dataset;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.rest.internal.exception.ServiceExceptionUtils;
import org.siemac.metamac.statistical_resources.rest.internal.v1_0.service.StatisticalResourcesV1_0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metamacRestApisLocator")
public class MetamacRestApisLocator {

    @Autowired
    private SrmConfiguration         srmConfiguration;

    private StatisticalResourcesV1_0 statisticalResourcesV1_0;

    public static String             FIELD_EXCLUDE_METADATA = "-metadata";
    public static String             FIELD_EXCLUDE_DATA     = "-data";

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

    public Dataset retrieveDatasetVersion(String agencyID, String resourceID, String version) throws MetamacException {
        try {
            Dataset retrieveDataset = getStatisticalResourcesV1_0().retrieveDataset(agencyID, resourceID, version, null, FIELD_EXCLUDE_METADATA + FIELD_EXCLUDE_DATA, null);
            return retrieveDataset;
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
