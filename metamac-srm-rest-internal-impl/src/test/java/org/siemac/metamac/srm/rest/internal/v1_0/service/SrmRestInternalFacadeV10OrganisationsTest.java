package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsMockitoVerify.verifyFindAgencySchemes;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.WebClient;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.common.test.utils.ConditionalCriteriaUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class SrmRestInternalFacadeV10OrganisationsTest extends SrmRestInternalFacadeV10BaseTest {

    private OrganisationsMetamacService organisationsService;

    @Test
    public void testErrorJsonNonAcceptable() throws Exception {

        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.NOT_ACCEPTABLE.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindAgenciesSchemes() throws Exception {
        testFindAgencySchemes(null, null, null, null, null, null, null); // without limits
        testFindAgencySchemes(null, null, null, "10000", null, null, null); // without limits
        testFindAgencySchemes(null, null, null, null, "0", null, null); // without limits, first page
        testFindAgencySchemes(null, null, null, "2", "0", null, null); // first page with pagination
        testFindAgencySchemes(null, null, null, "2", "2", null, null); // other page with pagination
        testFindAgencySchemes(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindAgencySchemes(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindAgencySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindAgencySchemes(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindAgenciesSchemesXml() throws Exception {
        String requestUri = getUriOrganisationSchemes(OrganisationSchemeTypeEnum.AGENCY_SCHEME, null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/organisations/findAgencySchemes.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    //
    // @Test
    // public void testFindAgenciesSchemesByAgency() throws Exception {
    // testFindAgenciesSchemes(AGENCY_1, null, null, null, null, null, null);
    // testFindAgenciesSchemes(AGENCY_1, null, null, null, "0", null, null);
    // testFindAgenciesSchemes(AGENCY_1, null, null, "2", "0", null, null);
    // testFindAgenciesSchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
    // testFindAgenciesSchemes(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    // }
    //
    // @Test
    // public void testFindAgenciesSchemesByAgencyXml() throws Exception {
    // String requestUri = getUriOrganisationSchemes(AGENCY_1, null, null, "4", "4");
    // InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/concepts/findConceptSchemes.byAgency.xml");
    //
    // // Request and validate
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    // }
    //
    // @Test
    // public void testFindAgenciesSchemesByAgencyErrorWildcard() throws Exception {
    // String requestUri = getUriOrganisationSchemes(WILDCARD, null, null);
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    // }
    //
    // @Test
    // public void testFindAgenciesSchemesByAgencyAndResource() throws Exception {
    // testFindAgenciesSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, null, null, null);
    // testFindAgenciesSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, null, null, null);
    // testFindAgenciesSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
    // testFindAgenciesSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "2", null, null, null);
    // testFindAgenciesSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
    // testFindAgenciesSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, null, "0", null, null);
    // testFindAgenciesSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "2", "0", null, null);
    // testFindAgenciesSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
    // testFindAgenciesSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
    // testFindAgenciesSchemes(WILDCARD, ITEM_SCHEME_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    // }
    //
    // @Test
    // public void testFindAgenciesSchemesByAgencyAndResourceXml() throws Exception {
    // String requestUri = getUriOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, null, "4", null);
    // InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/concepts/findConceptSchemes.byAgencyResource.xml");
    //
    // // Request and validate
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    // }
    //
    // @Test
    // public void testFindAgenciesSchemesByAgencyAndResourceErrorWildcard() throws Exception {
    // String requestUri = getUriOrganisationSchemes(AGENCY_1, WILDCARD, null);
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    // }
    //
    // @Test
    // public void testRetrieveConceptScheme() throws Exception {
    // resetMocks();
    //
    // String agencyID = AGENCY_1;
    // String resourceID = ITEM_SCHEME_1_CODE;
    // String version = ITEM_SCHEME_1_VERSION_1;
    // ConceptScheme conceptScheme = getSrmRestInternalFacadeClientXml().retrieveConceptScheme(agencyID, resourceID, version);
    //
    // // Validation
    // assertNotNull(conceptScheme);
    // // other metadata are tested in mapper tests
    // assertEquals("idAsMaintainer" + agencyID, conceptScheme.getAgencyID());
    // assertEquals(resourceID, conceptScheme.getId());
    // assertEquals(version, conceptScheme.getVersion());
    // assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, conceptScheme.getKind());
    // assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, conceptScheme.getSelfLink().getKind());
    // assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, conceptScheme.getParentLink().getKind());
    // assertTrue(conceptScheme.getConcepts().get(0) instanceof ConceptType);
    // assertFalse(conceptScheme.getConcepts().get(0) instanceof Concept);
    // }
    //
    // @Test
    // public void testRetrieveConceptSchemeXml() throws Exception {
    //
    // String requestBase = getUriOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
    // String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
    //
    // for (int i = 0; i < requestUris.length; i++) {
    // String requestUri = requestUris[i];
    // InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/concepts/retrieveConceptScheme.id1.xml");
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    // }
    // }
    //
    // @Test
    // public void testRetrieveConceptsSchemeErrorNotExists() throws Exception {
    // String agencyID = AGENCY_1;
    // String resourceID = NOT_EXISTS;
    // String version = ITEM_SCHEME_1_VERSION_1;
    // try {
    // getSrmRestInternalFacadeClientXml().retrieveConceptScheme(agencyID, resourceID, version);
    // } catch (ServerWebApplicationException e) {
    // org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
    //
    // assertEquals(RestServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND.getCode(), exception.getCode());
    // assertEquals("Concept scheme not found in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
    // assertEquals(3, exception.getParameters().getParameters().size());
    // assertEquals(agencyID, exception.getParameters().getParameters().get(0));
    // assertEquals(resourceID, exception.getParameters().getParameters().get(1));
    // assertEquals(version, exception.getParameters().getParameters().get(2));
    // assertNull(exception.getErrors());
    // } catch (Exception e) {
    // fail("Incorrect exception");
    // }
    // }
    //
    // @Test
    // public void testRetrieveConceptSchemeErrorNotExistsXml() throws Exception {
    // String requestUri = getUriOrganisationSchemes(AGENCY_1, NOT_EXISTS, ITEM_SCHEME_1_VERSION_1);
    // InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/concepts/retrieveConceptScheme.notFound.xml");
    //
    // // Request and validate
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    // }
    //
    // @Test
    // public void testRetrieveConceptSchemeErrorWildcard() throws Exception {
    // {
    // String requestUri = getUriOrganisationSchemes(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    // }
    // {
    // String requestUri = getUriOrganisationSchemes(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1);
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    // }
    // {
    // String requestUri = getUriOrganisationSchemes(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD);
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    // }
    // }
    //
    // @Test
    // public void testFindConcepts() throws Exception {
    //
    // // without parameters
    // testFindConcepts(WILDCARD, WILDCARD, WILDCARD, null, null, null, null); // without limits
    // testFindConcepts(WILDCARD, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
    // testFindConcepts(WILDCARD, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
    // testFindConcepts(WILDCARD, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
    // testFindConcepts(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
    // testFindConcepts(WILDCARD, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    //
    // // agency
    // testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, null, null, null, null); // without limits
    // testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, "10000", null, null, null); // without limits
    // testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, null, "0", null, null); // without limits, first page
    // testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, "2", "0", null, null); // with pagination
    // testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
    // testFindConcepts(AGENCY_1, WILDCARD, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    //
    // // resource
    // testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, null, null, null, null); // without limits
    // testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "10000", null, null, null); // without limits
    // testFindConcepts(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, null, "0", null, null); // without limits, first page
    // testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", null, null); // with pagination
    // testFindConcepts(WILDCARD, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
    // testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    //
    // // version
    // testFindConcepts(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", null, null); // with pagination
    // testFindConcepts(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query
    // testFindConcepts(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, "2", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query and order
    // }
    //
    // @Test
    // public void testFindConceptsXml() throws Exception {
    // String requestUri = getUriItems(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, QUERY_ID_LIKE_1_NAME_LIKE_2, "4", "4");
    // InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/concepts/findConcepts.xml");
    //
    // // Request and validate
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    // }
    //
    // @Test
    // public void testRetrieveConcept() throws Exception {
    // resetMocks();
    //
    // String agencyID = AGENCY_1;
    // String resourceID = ITEM_SCHEME_1_CODE;
    // String version = ITEM_SCHEME_1_VERSION_1;
    // String conceptID = ITEM_1_CODE;
    // Concept concept = getSrmRestInternalFacadeClientXml().retrieveConcept(agencyID, resourceID, version, conceptID);
    //
    // // Validation
    // assertNotNull(concept);
    // assertEquals(conceptID, concept.getId());
    // assertEquals(RestInternalConstants.KIND_CONCEPT, concept.getKind());
    // assertEquals(RestInternalConstants.KIND_CONCEPT, concept.getSelfLink().getKind());
    // assertEquals(RestInternalConstants.KIND_CONCEPTS, concept.getParentLink().getKind());
    // assertTrue(concept instanceof ConceptType);
    // assertTrue(concept instanceof Concept);
    // // other metadata are tested in transformation tests
    // }
    //
    // @Test
    // public void testRetrieveConceptXml() throws Exception {
    //
    // String requestBase = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
    // String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};
    // for (int i = 0; i < requestUris.length; i++) {
    // String requestUri = requestUris[i];
    // InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/concepts/retrieveConcept.id1.xml");
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    // }
    // }
    //
    // @Test
    // public void testRetrieveConceptErrorNotExists() throws Exception {
    // String agencyID = AGENCY_1;
    // String resourceID = ITEM_SCHEME_1_CODE;
    // String version = ITEM_SCHEME_1_VERSION_1;
    // String conceptID = NOT_EXISTS;
    // try {
    // getSrmRestInternalFacadeClientXml().retrieveConcept(agencyID, resourceID, version, conceptID);
    // } catch (ServerWebApplicationException e) {
    // org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
    //
    // assertEquals(RestServiceExceptionType.CONCEPT_NOT_FOUND.getCode(), exception.getCode());
    // assertEquals("Concept not found with ID " + conceptID + " in Concept scheme in AgencyID " + agencyID + " with ID " + resourceID + " and version " + version, exception.getMessage());
    // assertEquals(4, exception.getParameters().getParameters().size());
    // assertEquals(conceptID, exception.getParameters().getParameters().get(0));
    // assertEquals(agencyID, exception.getParameters().getParameters().get(1));
    // assertEquals(resourceID, exception.getParameters().getParameters().get(2));
    // assertEquals(version, exception.getParameters().getParameters().get(3));
    // assertNull(exception.getErrors());
    // } catch (Exception e) {
    // fail("Incorrect exception");
    // }
    // }
    //
    // @Test
    // public void testRetrieveConceptErrorNotExistsXml() throws Exception {
    // String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, NOT_EXISTS);
    // InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/concepts/retrieveConcept.notFound.xml");
    //
    // // Request and validate
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    // }
    //
    // @Test
    // public void testRetrieveConceptErrorWildcard() throws Exception {
    // {
    // String requestUri = getUriItem(WILDCARD, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    // }
    // {
    // String requestUri = getUriItem(AGENCY_1, WILDCARD, ITEM_SCHEME_1_VERSION_1, ITEM_1_CODE);
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    // }
    // {
    // String requestUri = getUriItem(AGENCY_1, ITEM_SCHEME_1_CODE, WILDCARD, ITEM_1_CODE);
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, new ByteArrayInputStream(new byte[0]));
    // }
    // }
    //
    // @Test
    // public void testRetrieveConceptTypes() throws Exception {
    //
    // // Retrieve
    // ConceptTypes conceptTypes = getSrmRestInternalFacadeClientXml().retrieveConceptTypes();
    //
    // // Validation
    // assertEquals(RestInternalConstants.KIND_CONCEPT_TYPES, conceptTypes.getKind());
    // assertTrue(conceptTypes.getConceptTypes().size() > 0);
    // }
    //
    // @Test
    // public void testRetrieveConceptTypesXml() throws Exception {
    //
    // String requestUri = getUriConceptTypes();
    // InputStream responseExpected = SrmRestInternalFacadeV10OrganisationsTest.class.getResourceAsStream("/responses/concepts/retrieveConceptTypes.xml");
    //
    // // Request and validate
    // testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    // }
    //
    private void testFindAgencySchemes(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        AgencySchemes itemSchemes = null;
        if (agencyID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findAgencySchemes(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            itemSchemes = getSrmRestInternalFacadeClientXml().findAgencySchemes(agencyID, query, orderBy, limit, offset);
        } else {
            itemSchemes = getSrmRestInternalFacadeClientXml().findAgencySchemes(agencyID, resourceID, query, orderBy, limit, offset);
        }

        // Verify with Mockito
        verifyFindAgencySchemes(organisationsService, agencyID, resourceID, limit, offset, query, orderBy, itemSchemes);
    }

    // private void testFindConcepts(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
    // resetMocks();
    // Concepts concepts = getSrmRestInternalFacadeClientXml().findConcepts(agencyID, resourceID, version, query, orderBy, limit, offset);
    // verifyFindConcepts(organisationsService, agencyID, resourceID, version, limit, offset, query, orderBy, concepts);
    // }

    @SuppressWarnings("unchecked")
    private void mockFindOrganisationSchemesByCondition() throws MetamacException {
        when(organisationsService.findOrganisationSchemesByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<OrganisationSchemeVersionMetamac>>() {

                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<OrganisationSchemeVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().maintainer().idAsMaintainer());
                        ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().code());
                        ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.maintainableArtefact().versionLogic());
                        ConditionalCriteria conditionalCriteriaType = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal,
                                OrganisationSchemeVersionMetamacProperties.organisationSchemeType());

                        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = conditionalCriteriaType != null ? (OrganisationSchemeTypeEnum) conditionalCriteriaType.getFirstOperant() : null;
                        if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null) {
                            // Retrieve one
                            OrganisationSchemeVersionMetamac itemSchemeVersion = null;
                            if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
                                    || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant())) {
                                itemSchemeVersion = null;
                            } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
                                    && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant())) {
                                itemSchemeVersion = OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, organisationSchemeTypeEnum);
                            } else {
                                fail();
                            }
                            List<OrganisationSchemeVersionMetamac> itemSchemes = new ArrayList<OrganisationSchemeVersionMetamac>();
                            if (itemSchemeVersion != null) {
                                itemSchemes.add(itemSchemeVersion);
                            }
                            return new PagedResult<OrganisationSchemeVersionMetamac>(itemSchemes, 0, itemSchemes.size(), itemSchemes.size());
                        } else {
                            // any
                            List<OrganisationSchemeVersionMetamac> itemSchemes = new ArrayList<OrganisationSchemeVersionMetamac>();
                            if (organisationSchemeTypeEnum != null) {
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2, organisationSchemeTypeEnum));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1, organisationSchemeTypeEnum));
                            } else {
                                // different types
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1,
                                        OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1,
                                        OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2,
                                        OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME));
                                itemSchemes.add(OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1,
                                        OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME));
                            }
                            return new PagedResult<OrganisationSchemeVersionMetamac>(itemSchemes, itemSchemes.size(), itemSchemes.size(), itemSchemes.size(), itemSchemes.size() * 10, 0);
                        }
                    };
                });
    }

    // @SuppressWarnings("unchecked")
    // private void mockFindConceptsByCondition() throws MetamacException {
    // when(organisationsService.findConceptsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(new Answer<PagedResult<OrganisationMetamac>>() {
    //
    // public org.fornax.cartridges.sculptor.framework.domain.PagedResult<OrganisationMetamac> answer(InvocationOnMock invocation) throws Throwable {
    // List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
    // ConditionalCriteria conditionalCriteriaAgencyID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
    // .itemSchemeVersion().maintainableArtefact().maintainer().idAsMaintainer());
    // ConditionalCriteria conditionalCriteriaResourceID = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties
    // .itemSchemeVersion().maintainableArtefact().code());
    // ConditionalCriteria conditionalCriteriaVersion = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties.itemSchemeVersion()
    // .maintainableArtefact().versionLogic());
    // ConditionalCriteria conditionalCriteriaItem = ConditionalCriteriaUtils.getConditionalCriteriaByPropertyName(conditions, Operator.Equal, OrganisationMetamacProperties.nameableArtefact()
    // .code());
    //
    // if (conditionalCriteriaAgencyID != null && conditionalCriteriaResourceID != null && conditionalCriteriaVersion != null && conditionalCriteriaItem != null) {
    // // Retrieve one
    // OrganisationMetamac concept = null;
    // if (NOT_EXISTS.equals(conditionalCriteriaAgencyID.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaResourceID.getFirstOperant())
    // || NOT_EXISTS.equals(conditionalCriteriaVersion.getFirstOperant()) || NOT_EXISTS.equals(conditionalCriteriaItem.getFirstOperant())) {
    // concept = null;
    // } else if (AGENCY_1.equals(conditionalCriteriaAgencyID.getFirstOperant()) && ITEM_SCHEME_1_CODE.equals(conditionalCriteriaResourceID.getFirstOperant())
    // && ITEM_SCHEME_1_VERSION_1.equals(conditionalCriteriaVersion.getFirstOperant()) && ITEM_1_CODE.equals(conditionalCriteriaItem.getFirstOperant())) {
    // OrganisationSchemeVersionMetamac conceptScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
    // OrganisationMetamac parent = OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, conceptScheme1, null);
    // concept = OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, conceptScheme1, parent);
    // } else {
    // fail();
    // }
    // List<OrganisationMetamac> concepts = new ArrayList<OrganisationMetamac>();
    // if (concept != null) {
    // concepts.add(concept);
    // }
    // return new PagedResult<OrganisationMetamac>(concepts, 0, concepts.size(), concepts.size());
    // } else {
    // // any
    // OrganisationSchemeVersionMetamac conceptScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1);
    // OrganisationSchemeVersionMetamac conceptScheme2 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1);
    //
    // List<OrganisationMetamac> concepts = new ArrayList<OrganisationMetamac>();
    // concepts.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, conceptScheme1, null));
    // concepts.add(OrganisationsDoMocks.mockOrganisation(ITEM_2_CODE, conceptScheme1, null));
    // concepts.add(OrganisationsDoMocks.mockOrganisation(ITEM_3_CODE, conceptScheme1, null));
    // concepts.add(OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, conceptScheme2, null));
    //
    // return new PagedResult<OrganisationMetamac>(concepts, concepts.size(), concepts.size(), concepts.size(), concepts.size() * 10, 0);
    // }
    // };
    // });
    // }

    @Override
    protected void resetMocks() throws MetamacException {
        organisationsService = applicationContext.getBean(OrganisationsMetamacService.class);
        reset(organisationsService);
        mockFindOrganisationSchemesByCondition();
        // mockFindConceptsByCondition(); // TODO
    }

    private String getUriOrganisationSchemes(OrganisationSchemeTypeEnum type, String agencyID, String resourceID, String version, String query, String limit, String offset) throws Exception {
        return getUriItemSchemes(getSupathItemSchemes(type), agencyID, resourceID, version, query, limit, offset);
    }

    @Override
    protected String getSupathItemSchemes() {
        fail("This method can not be called because subpath is different in organisation scheme types");
        return null;
    }

    @Override
    protected String getSupathItems() {
        fail("This method can not be called because subpath is different in organisation scheme types");
        return null;
    }

    private String getSupathItemSchemes(OrganisationSchemeTypeEnum type) {
        switch (type) {
            case AGENCY_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_AGENCY_SCHEMES;
            case ORGANISATION_UNIT_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES;
            case DATA_CONSUMER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES;
            case DATA_PROVIDER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_PROVIDER_SCHEMES;
            default:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATION_SCHEMES;
        }
    }
}