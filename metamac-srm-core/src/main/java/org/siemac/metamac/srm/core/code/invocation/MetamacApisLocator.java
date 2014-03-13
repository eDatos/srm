package org.siemac.metamac.srm.core.code.invocation;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.notices.rest.internal.v1_0.service.NoticesV1_0;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MetamacApisLocator {

    @Autowired
    private SrmConfiguration configurationService;

    private NoticesV1_0      noticesV10 = null;

    public NoticesV1_0 getNoticesRestInternalFacadeV10() throws MetamacException {
        if (noticesV10 == null) {
            String baseApi = configurationService.retrieveNoticesInternalApiUrlBase();
            noticesV10 = JAXRSClientFactory.create(baseApi, NoticesV1_0.class, null, true); // true to do thread safe
        }
        // reset thread context
        WebClient.client(noticesV10).reset();
        WebClient.client(noticesV10).accept("application/xml");

        return noticesV10;
    }
}
