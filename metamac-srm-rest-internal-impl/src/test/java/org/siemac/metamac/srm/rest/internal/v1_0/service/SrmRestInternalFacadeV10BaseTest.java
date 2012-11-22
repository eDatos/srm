package org.siemac.metamac.srm.rest.internal.v1_0.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.common.test.MetamacRestBaseTest;
import org.siemac.metamac.rest.common.test.ServerResource;
import org.siemac.metamac.rest.constants.RestConstants;
import org.siemac.metamac.rest.utils.RestUtils;
import org.springframework.context.ApplicationContext;

public abstract class SrmRestInternalFacadeV10BaseTest extends MetamacRestBaseTest {

    private static String                   jaxrsServerAddress = "http://localhost:" + ServerResource.PORT + "/apis/srm-internal";
    protected String                        baseApi            = jaxrsServerAddress + "/v1.0";
    protected static ApplicationContext     applicationContext = null;
    private static SrmRestInternalFacadeV10 srmRestInternalFacadeClientXml;

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
    }

    @Before
    public void setUp() throws MetamacException {
        resetMocks();
    }

    @Test
    public void testErrorWithoutMatchError404() throws Exception {
        String requestUri = baseApi + "/nomatch";
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    protected SrmRestInternalFacadeV10 getSrmRestInternalFacadeClientXml() {
        WebClient.client(srmRestInternalFacadeClientXml).reset();
        WebClient.client(srmRestInternalFacadeClientXml).accept(APPLICATION_XML);
        return srmRestInternalFacadeClientXml;
    }

    protected String getUriItemSchemes(String agencyID, String resourceID, String version) {
        StringBuilder uri = new StringBuilder();
        uri.append(baseApi + "/" + getSupathItemSchemes());
        if (agencyID != null) {
            uri.append("/" + agencyID);
            if (resourceID != null) {
                uri.append("/" + resourceID);
                if (version != null) {
                    uri.append("/" + version);
                }
            }
        }
        return uri.toString();
    }

    protected String getUriItemSchemes(String agencyID, String resourceID, String query, String limit, String offset) throws Exception {
        String uri = getUriItemSchemes(agencyID, resourceID, null);
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    protected String getUriItems(String agencyID, String resourceID, String version, String query, String limit, String offset) throws Exception {
        String uri = getUriItemSchemes(agencyID, resourceID, version) + "/" + getSupathItems();
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_QUERY, RestUtils.encodeParameter(query));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_LIMIT, RestUtils.encodeParameter(limit));
        uri = RestUtils.createLinkWithQueryParam(uri, RestConstants.PARAMETER_OFFSET, RestUtils.encodeParameter(offset));
        return uri.toString();
    }

    protected String getUriItem(String agencyID, String resourceID, String version, String itemID) throws Exception {
        return getUriItems(agencyID, resourceID, version, null, null, null) + "/" + itemID;
    }

    protected abstract void resetMocks() throws MetamacException;
    protected abstract String getSupathItemSchemes();
    protected abstract String getSupathItems();
}