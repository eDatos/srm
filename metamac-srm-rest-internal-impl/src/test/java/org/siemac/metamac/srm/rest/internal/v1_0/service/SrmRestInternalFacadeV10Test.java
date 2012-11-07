package org.siemac.metamac.srm.rest.internal.v1_0.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.junit.BeforeClass;
import org.junit.Test;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.common.test.MetamacRestBaseTest;
import org.siemac.metamac.rest.common.test.ServerResource;
import org.springframework.context.ApplicationContext;

public class SrmRestInternalFacadeV10Test extends MetamacRestBaseTest {

    private static final String             PORT                      = ServerResource.PORT;
    private static String                   jaxrsServerAddress        = "http://localhost:" + PORT + "/apis/srm-internal";
    private static String                   baseApi                   = jaxrsServerAddress + "/v1.0";

    // not read property from properties file to check explicity
    private static String                   srmApiInternalEndpointV10 = "http://data.istac.es/apis/srm-internal/v1.0";

    private static SrmRestInternalFacadeV10 srmRestInternalFacadeClientXml;

    private static ApplicationContext       applicationContext        = null;

    private static String                   NOT_EXISTS                = "NOT_EXISTS";
    public static String                    AGENCY_SMDX               = "SDMX";
    public static String                    CONCEPT_SCHEME_1_CODE     = "conceptScheme01";
    public static String                    CONCEPT_SCHEME_1_URN      = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX01:CONCEPTSCHEME01(1.0)";
    public static String                    CONCEPT_SCHEME_1_VERSION  = "1.0";

    @SuppressWarnings({"unchecked", "rawtypes"})
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // Start server
        assertTrue("server did not launch correctly", launchServer(ServerResource.class, true));

        // Get application context from Jetty
        applicationContext = ApplicationContextProvider.getApplicationContext();

        // Rest clients
        // xml
        {
            List providers = new ArrayList();
            providers.add(applicationContext.getBean("jaxbProvider", JAXBElementProvider.class));
            srmRestInternalFacadeClientXml = JAXRSClientFactory.create(jaxrsServerAddress, SrmRestInternalFacadeV10.class, providers, Boolean.TRUE);
        }
        // Mockito
        // setUpMockito();
    }

    @Test
    public void testWithoutMatchError404() throws Exception {

        String requestUri = baseApi + "/nomatch";

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    private SrmRestInternalFacadeV10 getSrmRestInternalFacadeClientXml() {
        WebClient.client(srmRestInternalFacadeClientXml).reset();
        WebClient.client(srmRestInternalFacadeClientXml).accept(APPLICATION_XML);
        return srmRestInternalFacadeClientXml;
    }
}
