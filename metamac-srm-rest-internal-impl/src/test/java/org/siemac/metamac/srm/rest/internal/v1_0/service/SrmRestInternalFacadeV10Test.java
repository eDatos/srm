package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.JAXBElementProvider;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.rest.common.test.MetamacRestBaseTest;
import org.siemac.metamac.rest.common.test.ServerResource;
import org.siemac.metamac.rest.common.test.utils.MetamacRestAsserts;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.springframework.context.ApplicationContext;

public class SrmRestInternalFacadeV10Test extends MetamacRestBaseTest {

    private static final String             PORT                                       = ServerResource.PORT;
    private static String                   jaxrsServerAddress                         = "http://localhost:" + PORT + "/apis/srm-internal";
    private static String                   baseApi                                    = jaxrsServerAddress + "/v1.0";

    private ConceptsMetamacService          conceptsService;

    // not read property from properties file to check explicity
    // private static String srmApiInternalEndpointV10 = "http://data.istac.es/apis/srm-internal/v1.0";

    private static SrmRestInternalFacadeV10 srmRestInternalFacadeClientXml;

    private static ApplicationContext       applicationContext                         = null;

    private String                          QUERY_CONCEPT_SCHEME_ID_LIKE_1             = ConceptSchemeCriteriaPropertyRestriction.ID + " " + ComparisonOperator.LIKE + " \"1\"";
    private String                          QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2 = ConceptSchemeCriteriaPropertyRestriction.ID + " " + ComparisonOperator.LIKE + " \"1\"" + " "
                                                                                               + LogicalOperator.AND + " " + ConceptSchemeCriteriaPropertyRestriction.NAME + " "
                                                                                               + ComparisonOperator.LIKE + " \"2\"";
    private String                          AGENCY_1                                   = "agency1";
    private String                          CONCEPT_SCHEME_1                           = "conceptScheme1";

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
    public void testWithoutMatchError404() throws Exception {
        String requestUri = baseApi + "/nomatch";
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void testFindConceptsSchemes() throws Exception {

        testFindConceptsSchemes(null, null, null, null); // without limits
        testFindConceptsSchemes("10000", null, null, null); // without limits
        testFindConceptsSchemes(null, "0", null, null); // without limits, first page
        testFindConceptsSchemes("2", "0", null, null); // first page with pagination
        testFindConceptsSchemes("2", "2", null, null); // other page with pagination
        testFindConceptsSchemes(null, null, QUERY_CONCEPT_SCHEME_ID_LIKE_1, null); // query by id, without limits
        testFindConceptsSchemes(null, null, QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindConceptsSchemes("1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
    }

    @Test
    public void testFindConceptsSchemesByAgency() throws Exception {
        testFindConceptsSchemes(AGENCY_1, null, null, null, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, null, null, null, null);
        testFindConceptsSchemes(AGENCY_1, null, "0", null, null);
        testFindConceptsSchemes(AGENCY_1, "2", "0", null, null);
        testFindConceptsSchemes(AGENCY_1, "1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, "1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null);
    }

    @Test
    public void testFindConceptsSchemesByAgencyAndResource() throws Exception {
        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1, null, null, null, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, CONCEPT_SCHEME_1, null, null, null, null);
        testFindConceptsSchemes(AGENCY_1, RestInternalConstants.WILDCARD, null, null, null, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, RestInternalConstants.WILDCARD, null, null, null, null);

        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1, "2", null, null, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, CONCEPT_SCHEME_1, "2", null, null, null);
        testFindConceptsSchemes(AGENCY_1, RestInternalConstants.WILDCARD, "2", null, null, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, RestInternalConstants.WILDCARD, "2", null, null, null);

        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1, null, "0", null, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, CONCEPT_SCHEME_1, null, "0", null, null);
        testFindConceptsSchemes(AGENCY_1, RestInternalConstants.WILDCARD, null, "0", null, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, RestInternalConstants.WILDCARD, null, "0", null, null);

        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1, "2", "0", null, null);
        testFindConceptsSchemes(AGENCY_1, CONCEPT_SCHEME_1, "1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null);
        testFindConceptsSchemes(RestInternalConstants.WILDCARD, CONCEPT_SCHEME_1, "1", "0", QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2, null);
    }

    private void testFindConceptsSchemes(String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(query, orderBy, limit, offset);
        assertFindConceptSchemes(null, null, limit, offset, query, orderBy, conceptSchemes);
    }

    private void testFindConceptsSchemes(String agencyID, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(agencyID, query, orderBy, limit, offset);
        assertFindConceptSchemes(agencyID, null, limit, offset, query, orderBy, conceptSchemes);
    }

    private void testFindConceptsSchemes(String agencyID, String resourceID, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();
        ConceptSchemes conceptSchemes = getSrmRestInternalFacadeClientXml().findConceptSchemes(agencyID, resourceID, query, orderBy, limit, offset);
        assertFindConceptSchemes(agencyID, resourceID, limit, offset, query, orderBy, conceptSchemes);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void assertFindConceptSchemes(String agencyID, String resourceID, String limit, String offset, String query, String orderBy, ConceptSchemes conceptSchemesActual) throws Exception {

        assertNotNull(conceptSchemesActual);
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, conceptSchemesActual.getKind());

        // Verify
        ArgumentCaptor<List> conditions = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<PagingParameter> pagingParameter = ArgumentCaptor.forClass(PagingParameter.class);
        verify(conceptsService).findConceptSchemesByCondition(any(ServiceContext.class), conditions.capture(), pagingParameter.capture());

        // Validate
        MetamacRestAsserts.assertEqualsConditionalCriteria(buildExpectedConditionalCriteria(agencyID, resourceID, query, orderBy), conditions.getValue());
        MetamacRestAsserts.assertEqualsPagingParameter(buildExpectedPagingParameter(offset, limit), pagingParameter.getValue());
    }

    private List<ConditionalCriteria> buildExpectedConditionalCriteria(String agencyID, String resourceID, String query, String orderBy) {
        List<ConditionalCriteria> expected = new ArrayList<ConditionalCriteria>();
        expected.addAll(buildExpectedConditionalCriteriaOrderBy(orderBy));
        expected.addAll(buildExpectedConditionalCriteriaQuery(query));
        expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().buildSingle());
        expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.lifeCycleMetadata().procStatus())
                .in(ProcStatusEnum.INTERNALLY_PUBLISHED, ProcStatusEnum.EXTERNALLY_PUBLISHED).buildSingle());
        if (agencyID != null && !RestInternalConstants.WILDCARD.equals(agencyID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                    .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().maintainer().idAsMaintainer()).eq(agencyID).buildSingle());
        }
        if (resourceID != null && !RestInternalConstants.WILDCARD.equals(resourceID)) {
            expected.add(ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).eq(resourceID)
                    .buildSingle());
        }
        return expected;
    }

    private List<ConditionalCriteria> buildExpectedConditionalCriteriaOrderBy(String orderBy) {
        if (orderBy == null) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).orderBy(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).ascending().build();
        } else {
            fail();
            return null;
        }
    }

    private List<ConditionalCriteria> buildExpectedConditionalCriteriaQuery(String query) {
        if (query == null) {
            return new ArrayList<ConditionalCriteria>();
        }
        if (QUERY_CONCEPT_SCHEME_ID_LIKE_1.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).like("%1%").build();
        } else if (QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2.equals(query)) {
            return ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().code()).like("%1%")
                    .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().label()).like("%2%").build();
        }
        fail();
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resetMocks() throws MetamacException {
        conceptsService = applicationContext.getBean(ConceptsMetamacService.class);
        reset(conceptsService);

        when(conceptsService.findConceptSchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenReturn(
                new PagedResult<ConceptSchemeVersionMetamac>(new ArrayList<ConceptSchemeVersionMetamac>(), 0, 1, 1));
    }

    private SrmRestInternalFacadeV10 getSrmRestInternalFacadeClientXml() {
        WebClient.client(srmRestInternalFacadeClientXml).reset();
        WebClient.client(srmRestInternalFacadeClientXml).accept(APPLICATION_XML);
        return srmRestInternalFacadeClientXml;
    }

    private PagingParameter buildExpectedPagingParameter(String offset, String limit) {
        Integer startRow = null;
        if (offset == null) {
            startRow = Integer.valueOf(0);
        } else {
            startRow = Integer.valueOf(offset);
        }
        Integer maximumResultSize = null;
        if (limit == null) {
            maximumResultSize = Integer.valueOf(25);
        } else {
            maximumResultSize = Integer.valueOf(limit);
        }
        if (maximumResultSize > Integer.valueOf(1000)) {
            maximumResultSize = Integer.valueOf(1000);
        }
        int endRow = startRow + maximumResultSize;
        return PagingParameter.rowAccess(startRow, endRow, false);
    }
}