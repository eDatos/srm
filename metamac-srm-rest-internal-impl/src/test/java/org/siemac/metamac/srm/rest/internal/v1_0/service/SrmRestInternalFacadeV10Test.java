package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.common.test.MetamacRestBaseTest;
import org.siemac.metamac.rest.common.test.ServerResource;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.springframework.context.ApplicationContext;

public class SrmRestInternalFacadeV10Test extends MetamacRestBaseTest {

    private static final String             PORT                      = ServerResource.PORT;
    private static String                   jaxrsServerAddress        = "http://localhost:" + PORT + "/apis/srm-internal";
    private static String                   baseApi                   = jaxrsServerAddress + "/v1.0";

    @Mock
    private ConceptsMetamacService          conceptsMetamacService;

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
        setUpMockito();
    }

    @Test
    public void testWithoutMatchError404() throws Exception {

        String requestUri = baseApi + "/nomatch";
        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Ignore
    @Test
    public void testFindConceptsSchemesXml() throws Exception {

        {
            // without limits
            String limit = null;
            String offset = null;
            String query = null;
            String orderBy = null;
            ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(query, orderBy, limit, offset);
            ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
            ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
            verify(conceptsMetamacService).findConceptsByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());
            assertEquals(1, conditions.getValue().size());
        }
        // {
        // // without limits, first page
        // String limit = "10000";
        // String offset = null;
        // String query = null;
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset), operations);
        // }
        // {
        // // without limits, first page
        // String limit = null;
        // String offset = "0";
        // String query = null;
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset), operations);
        // }
        // {
        // // first page with pagination
        // String limit = "2";
        // String offset = "0";
        // String query = null;
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset), operations);
        // }
        // {
        // // second page with pagination
        // String limit = "2";
        // String offset = "2";
        // String query = null;
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset), operations);
        // }
        // {
        // // last page with pagination
        // String limit = "2";
        // String offset = "8";
        // String query = null;
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset), operations);
        // }
        // {
        // // no results
        // String limit = "2";
        // String offset = "9";
        // String query = null;
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset), operations);
        // }
        //
        // // Queries
        // {
        // // query by id, without limits
        // String limit = null;
        // String offset = null;
        // String query = QUERY_OPERATION_ID_LIKE_1; // operation1 and operation10
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset, query), operations);
        // }
        // {
        // // query by id and indicators system, without limits
        // String limit = null;
        // String offset = null;
        // String query = QUERY_OPERATION_ID_LIKE_1_AND_INDICATORS_SYSTEM; // operation1
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset, query), operations);
        // }
        // {
        // // query by id, first page
        // String limit = "1";
        // String offset = "0";
        // String query = QUERY_OPERATION_ID_LIKE_1; // operation1 and operation10
        // String orderBy = null;
        // Operations operations = getStatisticalOperationsRestInternalFacadeClientXml().findOperations(query, orderBy, limit, offset);
        // StatisticalOperationsRestAsserts.assertEqualsOperations(StatisticalOperationsRestMocks.mockOperations(statisticalOperationsApiInternalEndpointV10, limit, offset, query), operations);
        // }
    }
    private static void setUpMockito() throws MetamacException {

    }

    private SrmRestInternalFacadeV10 getSrmRestInternalFacadeClientXml() {
        WebClient.client(srmRestInternalFacadeClientXml).reset();
        WebClient.client(srmRestInternalFacadeClientXml).accept(APPLICATION_XML);
        return srmRestInternalFacadeClientXml;
    }
}
