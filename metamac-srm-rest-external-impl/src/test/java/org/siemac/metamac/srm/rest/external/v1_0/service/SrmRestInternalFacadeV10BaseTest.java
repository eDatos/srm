package org.siemac.metamac.srm.rest.external.v1_0.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.siemac.metamac.common.test.utils.ConditionalCriteriaUtils;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.common.test.MetamacRestBaseTest;
import org.siemac.metamac.rest.common.test.ServerResource;
import org.siemac.metamac.rest.constants.RestConstants;
import org.siemac.metamac.rest.utils.RestUtils;
import org.springframework.context.ApplicationContext;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefactProperties.IdentifiableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactProperties.MaintainableArtefactProperty;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefactProperties.NameableArtefactProperty;

public abstract class SrmRestInternalFacadeV10BaseTest extends MetamacRestBaseTest {

    private static String                   jaxrsServerAddress = "http://localhost:" + ServerResource.PORT + "/apis/structural-resources";
    protected String                        baseApi            = jaxrsServerAddress + "/v1.0";
    protected static ApplicationContext     applicationContext = null;
    private static SrmRestExternalFacadeV10 srmRestInternalFacadeClientXml;
    private static String                   apiEndpointv10;

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
            srmRestInternalFacadeClientXml = JAXRSClientFactory.create(jaxrsServerAddress, SrmRestExternalFacadeV10.class, providers, Boolean.TRUE);
        }
    }

    @Before
    public void setUp() throws MetamacException {
        ConfigurationService configurationService = applicationContext.getBean(ConfigurationService.class);
        apiEndpointv10 = configurationService.retrieveSrmExternalApiUrlBase() + "/v1.0";

        resetMocks();
    }

    @Test
    public void testErrorWithoutMatchError404() throws Exception {
        String requestUri = baseApi + "/nomatch";

        WebClient webClient = WebClient.create(requestUri).accept(APPLICATION_XML);
        Response response = webClient.get();

        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
        InputStream responseActual = (InputStream) response.getEntity();
        assertTrue(StringUtils.isBlank(IOUtils.toString(responseActual)));
    }

    protected SrmRestExternalFacadeV10 getSrmRestInternalFacadeClientXml() {
        WebClient.client(srmRestInternalFacadeClientXml).reset();
        WebClient.client(srmRestInternalFacadeClientXml).accept(APPLICATION_XML);
        return srmRestInternalFacadeClientXml;
    }

    protected String getUriMaintainableArtefacts(String agencyID, String resourceID, String version) {
        StringBuilder uri = new StringBuilder();
        uri.append(baseApi + "/" + getSupathMaintainableArtefacts());
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
    protected String getUriItemSchemes(String agencyID, String resourceID, String version) {
        return getUriMaintainableArtefacts(agencyID, resourceID, version);
    }
    protected String getUriItemSchemes(String agencyID, String resourceID, String version, String query, String limit, String offset) throws Exception {
        String uri = getUriItemSchemes(agencyID, resourceID, version);
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

    @SuppressWarnings("rawtypes")
    protected String getAgencyIdFromConditionalCriteria(List<ConditionalCriteria> conditions, MaintainableArtefactProperty maintainableArtefactProperty) {
        ConditionalCriteria conditionalCriteria = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, maintainableArtefactProperty.maintainer().idAsMaintainer());
        return conditionalCriteria != null ? (String) conditionalCriteria.getFirstOperant() : null;
    }

    @SuppressWarnings("rawtypes")
    protected String getItemSchemeIdFromConditionalCriteria(List<ConditionalCriteria> conditions, MaintainableArtefactProperty maintainableArtefactProperty) {
        ConditionalCriteria conditionalCriteria = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Or, maintainableArtefactProperty.code());
        return conditionalCriteria != null ? (String) conditionalCriteria.getFirstOperant() : null;
    }

    @SuppressWarnings("rawtypes")
    protected String getVersionFromConditionalCriteria(List<ConditionalCriteria> conditions, MaintainableArtefactProperty maintainableArtefactProperty) {
        ConditionalCriteria conditionalCriteria = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, maintainableArtefactProperty.versionLogic());
        return conditionalCriteria != null ? (String) conditionalCriteria.getFirstOperant() : null;
    }

    @SuppressWarnings("rawtypes")
    protected Boolean getVersionLatestFromConditionalCriteria(List<ConditionalCriteria> conditions, MaintainableArtefactProperty maintainableArtefactProperty) {
        ConditionalCriteria conditionalCriteria = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, maintainableArtefactProperty.latestPublic());
        return conditionalCriteria != null ? (Boolean) conditionalCriteria.getFirstOperant() : null;
    }

    @SuppressWarnings("rawtypes")
    protected String getItemIdFromConditionalCriteria(List<ConditionalCriteria> conditions, NameableArtefactProperty nameableArtefactProperty) {
        ConditionalCriteria conditionalCriteria = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Or, nameableArtefactProperty.code());
        return conditionalCriteria != null ? (String) conditionalCriteria.getFirstOperant() : null;
    }

    @SuppressWarnings("rawtypes")
    protected String getNameableArtefactCodeFromConditionalCriteria(List<ConditionalCriteria> conditions, NameableArtefactProperty nameableArtefactProperty) {
        ConditionalCriteria conditionalCriteria = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, nameableArtefactProperty.code());
        return conditionalCriteria != null ? (String) conditionalCriteria.getFirstOperant() : null;
    }

    @SuppressWarnings("rawtypes")
    protected String getIdentifiableArtefactCodeFromConditionalCriteria(List<ConditionalCriteria> conditions, IdentifiableArtefactProperty identifiableArtefactProperty) {
        ConditionalCriteria conditionalCriteria = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, identifiableArtefactProperty.code());
        return conditionalCriteria != null ? (String) conditionalCriteria.getFirstOperant() : null;
    }

    protected String getApiEndpoint() {
        return apiEndpointv10;
    }

    protected abstract void resetMocks() throws MetamacException;
    protected abstract String getSupathMaintainableArtefacts();
    protected abstract String getSupathItems();
}