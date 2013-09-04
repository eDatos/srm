package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils.DataStructuresMockitoVerify.verifyFindDataStructures;
import static org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils.DataStructuresMockitoVerify.verifyRetrieveDataStructure;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.ServerWebApplicationException;
import org.apache.cxf.jaxrs.client.WebClient;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamacProperties;
import org.siemac.metamac.srm.core.dsd.serviceapi.DataStructureDefinitionMetamacService;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils.DataStructuresDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;

public class SrmRestInternalFacadeV10DataStructuresTest extends SrmRestInternalFacadeV10BaseTest {

    private DataStructureDefinitionMetamacService dsdsService;
    private StructureVersionRepository            structureVersionRepository;

    @Test
    public void testJsonAcceptable() throws Exception {

        String requestUri = getUriStructures(AGENCY_1, ARTEFACT_1_CODE, VERSION_1);

        // Request and validate
        WebClient webClient = WebClient.create(requestUri).accept("application/json");
        Response response = webClient.get();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testFindDataStructures() throws Exception {
        testFindDataStructures(null, null, null, null, null, null, null); // without limits
        testFindDataStructures(null, null, null, "10000", null, null, null); // without limits
        testFindDataStructures(null, null, null, null, "0", null, null); // without limits, first page
        testFindDataStructures(null, null, null, "2", "0", null, null); // first page with pagination
        testFindDataStructures(null, null, null, "2", "2", null, null); // other page with pagination
        testFindDataStructures(null, null, null, null, null, QUERY_ID_LIKE_1, null); // query by id, without limits
        testFindDataStructures(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, without limits
        testFindDataStructures(null, null, null, null, null, QUERY_LATEST, null); // latest
        testFindDataStructures(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null); // query by id and name, first page
        testFindDataStructures(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC); // query by id and name, first page
    }

    @Test
    public void testFindDataStructuresXml() throws Exception {
        String requestUri = getUriItemSchemes(null, null, null, null, "4", "4");
        InputStream responseExpected = SrmRestInternalFacadeV10DataStructuresTest.class.getResourceAsStream("/responses/dsds/findDataStructures.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
    }

    @Test
    public void testFindDataStructuresByAgency() throws Exception {
        testFindDataStructures(AGENCY_1, null, null, null, null, null, null);
        testFindDataStructures(AGENCY_1, null, null, null, "0", null, null);
        testFindDataStructures(AGENCY_1, null, null, "2", "0", null, null);
        testFindDataStructures(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindDataStructures(AGENCY_1, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindDataStructuresByAgencyTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        DataStructures dataStructures = getSrmRestInternalFacadeClientXml().findDataStructures(agencyID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/datastructures/" + agencyID + "?limit=4&offset=4", dataStructures.getSelfLink());
        assertEquals(getApiEndpoint() + "/datastructures/" + agencyID + "?limit=4&offset=0", dataStructures.getFirstLink());
        assertEquals(getApiEndpoint() + "/datastructures/" + agencyID + "?limit=4&offset=0", dataStructures.getPreviousLink());
        assertEquals(getApiEndpoint() + "/datastructures/" + agencyID + "?limit=4&offset=36", dataStructures.getLastLink());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURES, dataStructures.getKind());
    }
    @Test
    public void testFindDataStructuresByAgencyErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findDataStructures(WILDCARD_ALL, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testFindDataStructuresByAgencyAndResource() throws Exception {
        testFindDataStructures(AGENCY_1, ARTEFACT_1_CODE, null, null, null, null, null);
        testFindDataStructures(WILDCARD_ALL, ARTEFACT_1_CODE, null, null, null, null, null);
        testFindDataStructures(AGENCY_1, ARTEFACT_1_CODE, null, "2", null, null, null);
        testFindDataStructures(WILDCARD_ALL, ARTEFACT_1_CODE, null, "2", null, null, null);
        testFindDataStructures(AGENCY_1, ARTEFACT_1_CODE, null, null, "0", null, null);
        testFindDataStructures(WILDCARD_ALL, ARTEFACT_1_CODE, null, null, "0", null, null);
        testFindDataStructures(AGENCY_1, ARTEFACT_1_CODE, null, "2", "0", null, null);
        testFindDataStructures(AGENCY_1, ARTEFACT_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindDataStructures(WILDCARD_ALL, ARTEFACT_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null);
        testFindDataStructures(WILDCARD_ALL, ARTEFACT_1_CODE, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC);
    }

    @Test
    public void testFindDataStructuresByAgencyAndResourceTestLinks() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ARTEFACT_1_CODE;
        DataStructures dataStructures = getSrmRestInternalFacadeClientXml().findDataStructures(agencyID, resourceID, null, null, "4", "4");
        assertEquals(getApiEndpoint() + "/datastructures/" + agencyID + "/" + resourceID + "?limit=4&offset=4", dataStructures.getSelfLink());
        assertEquals(getApiEndpoint() + "/datastructures/" + agencyID + "/" + resourceID + "?limit=4&offset=0", dataStructures.getFirstLink());
        assertEquals(getApiEndpoint() + "/datastructures/" + agencyID + "/" + resourceID + "?limit=4&offset=0", dataStructures.getPreviousLink());
        assertEquals(getApiEndpoint() + "/datastructures/" + agencyID + "/" + resourceID + "?limit=4&offset=36", dataStructures.getLastLink());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURES, dataStructures.getKind());
    }

    @Test
    public void testFindDataStructuresByAgencyAndResourceErrorWildcard() throws Exception {
        try {
            getSrmRestInternalFacadeClientXml().findDataStructures(AGENCY_1, WILDCARD_ALL, null, null, null, null);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveDataStructure() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ARTEFACT_1_CODE;
        String version = VERSION_1;
        DataStructure dataStructure = getSrmRestInternalFacadeClientXml().retrieveDataStructure(agencyID, resourceID, version);

        // Validation
        assertNotNull(dataStructure);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, dataStructure.getAgencyID());
        assertEquals(resourceID, dataStructure.getId());
        assertEquals(version, dataStructure.getVersion());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURE, dataStructure.getKind());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURE, dataStructure.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURES, dataStructure.getParentLink().getKind());

        // Verify with Mockito
        verifyRetrieveDataStructure(dsdsService, agencyID, resourceID, version);
    }

    @Test
    public void testRetrieveDataStructureVersionLatest() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = ARTEFACT_1_CODE;
        String version = WILDCARD_LATEST;
        DataStructure dataStructure = getSrmRestInternalFacadeClientXml().retrieveDataStructure(agencyID, resourceID, version);

        // Validation
        assertNotNull(dataStructure);
        // other metadata are tested in mapper tests
        assertEquals(agencyID, dataStructure.getAgencyID());
        assertEquals(resourceID, dataStructure.getId());
        assertEquals(VERSION_1, dataStructure.getVersion());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURE, dataStructure.getKind());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURE, dataStructure.getSelfLink().getKind());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURES, dataStructure.getParentLink().getKind());

        // Verify with Mockito
        verifyRetrieveDataStructure(dsdsService, agencyID, resourceID, version);
    }

    @Test
    public void testRetrieveDataStructureXml() throws Exception {

        String requestBase = getUriItemSchemes(AGENCY_1, ARTEFACT_1_CODE, VERSION_1);
        String[] requestUris = new String[]{requestBase, requestBase + ".xml", requestBase + "?_type=xml"};

        for (int i = 0; i < requestUris.length; i++) {
            String requestUri = requestUris[i];
            InputStream responseExpected = SrmRestInternalFacadeV10DataStructuresTest.class.getResourceAsStream("/responses/dsds/retrieveDataStructure.id1.xml");
            testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.OK, responseExpected);
        }
    }

    @Test
    public void testRetrieveDataStructureErrorNotExists() throws Exception {
        String agencyID = AGENCY_1;
        String resourceID = NOT_EXISTS;
        String version = VERSION_1;
        try {
            getSrmRestInternalFacadeClientXml().retrieveDataStructure(agencyID, resourceID, version);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.NOT_FOUND.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.DATA_STRUCTURE_NOT_FOUND.getCode(), exception.getCode());
            assertEquals("DataStructure " + resourceID + " not found in version " + version + " from Agency " + agencyID, exception.getMessage());
            assertEquals(3, exception.getParameters().getParameters().size());
            assertEquals(resourceID, exception.getParameters().getParameters().get(0));
            assertEquals(version, exception.getParameters().getParameters().get(1));
            assertEquals(agencyID, exception.getParameters().getParameters().get(2));
            assertNull(exception.getErrors());
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    @Test
    public void testRetrieveDataStructureErrorNotExistsXml() throws Exception {
        String requestUri = getUriItemSchemes(AGENCY_1, NOT_EXISTS, VERSION_1);
        InputStream responseExpected = SrmRestInternalFacadeV10DataStructuresTest.class.getResourceAsStream("/responses/dsds/retrieveDataStructure.notFound.xml");

        // Request and validate
        testRequestWithoutJaxbTransformation(requestUri, APPLICATION_XML, Status.NOT_FOUND, responseExpected);
    }

    @Test
    public void testRetrieveDataStructureErrorWildcard() throws Exception {
        // Agency
        try {
            getSrmRestInternalFacadeClientXml().retrieveDataStructure(WILDCARD_ALL, ARTEFACT_1_CODE, VERSION_1);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter agencyID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_AGENCY_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // Resource
        try {
            getSrmRestInternalFacadeClientXml().retrieveDataStructure(AGENCY_1, WILDCARD_ALL, VERSION_1);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter resourceID has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_RESOURCE_ID, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }

        // Version
        try {
            getSrmRestInternalFacadeClientXml().retrieveDataStructure(AGENCY_1, ARTEFACT_1_CODE, WILDCARD_ALL);
        } catch (ServerWebApplicationException e) {
            assertEquals(Status.BAD_REQUEST.getStatusCode(), e.getStatus());

            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = extractErrorFromException(getSrmRestInternalFacadeClientXml(), e);
            assertEquals(RestServiceExceptionType.PARAMETER_INCORRECT.getCode(), exception.getCode());
            assertEquals("Parameter version has incorrect value", exception.getMessage());
            assertEquals(1, exception.getParameters().getParameters().size());
            assertEquals(SrmRestConstants.PARAMETER_VERSION, exception.getParameters().getParameters().get(0));
        } catch (Exception e) {
            fail("Incorrect exception");
        }
    }

    private void testFindDataStructures(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy) throws Exception {
        resetMocks();

        // Find
        DataStructures dataStructures = null;
        if (agencyID == null) {
            dataStructures = getSrmRestInternalFacadeClientXml().findDataStructures(query, orderBy, limit, offset);
        } else if (resourceID == null) {
            dataStructures = getSrmRestInternalFacadeClientXml().findDataStructures(agencyID, query, orderBy, limit, offset);
        } else {
            dataStructures = getSrmRestInternalFacadeClientXml().findDataStructures(agencyID, resourceID, query, orderBy, limit, offset);
        }

        assertNotNull(dataStructures);
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURES, dataStructures.getKind());

        // Verify with Mockito
        verifyFindDataStructures(dsdsService, agencyID, resourceID, null, limit, offset, query, orderBy);
    }

    private void mockRetrieveStructureVersionByVersion() throws MetamacException {
        when(structureVersionRepository.retrieveByVersion(any(Long.class), any(String.class))).thenAnswer(new Answer<StructureVersion>() {

            @Override
            public StructureVersion answer(InvocationOnMock invocation) throws Throwable {
                String version = (String) invocation.getArguments()[1];
                return DataStructuresDoMocks.mockDataStructure("agencyID", version, version);
            };
        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindDataStructuresByCondition() throws MetamacException {
        when(dsdsService.findDataStructureDefinitionsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<DataStructureDefinitionVersionMetamac>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<DataStructureDefinitionVersionMetamac> answer(InvocationOnMock invocation) throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        String agencyID = getAgencyIdFromConditionalCriteria(conditions, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact());
                        String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact());
                        String version = getVersionFromConditionalCriteria(conditions, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact());
                        Boolean latest = getVersionLatestFromConditionalCriteria(conditions, DataStructureDefinitionVersionMetamacProperties.maintainableArtefact());
                        if (agencyID != null && resourceID != null && (version != null || Boolean.TRUE.equals(latest))) {
                            // Retrieve one
                            DataStructureDefinitionVersionMetamac dataStructureDefinitionVersion = null;
                            if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version)) {
                                dataStructureDefinitionVersion = null;
                            } else if (AGENCY_1.equals(agencyID) && ARTEFACT_1_CODE.equals(resourceID) && (VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                                dataStructureDefinitionVersion = DataStructuresDoMocks.mockDataStructureWithComponents(agencyID, resourceID, VERSION_1);
                            } else {
                                fail();
                            }
                            List<DataStructureDefinitionVersionMetamac> dataStructures = new ArrayList<DataStructureDefinitionVersionMetamac>();
                            if (dataStructureDefinitionVersion != null) {
                                dataStructures.add(dataStructureDefinitionVersion);
                            }
                            return new PagedResult<DataStructureDefinitionVersionMetamac>(dataStructures, 0, dataStructures.size(), dataStructures.size());
                        } else {
                            // any
                            List<DataStructureDefinitionVersionMetamac> dataStructures = new ArrayList<DataStructureDefinitionVersionMetamac>();
                            dataStructures.add(DataStructuresDoMocks.mockDataStructureWithComponents(AGENCY_1, ARTEFACT_1_CODE, VERSION_1));
                            dataStructures.add(DataStructuresDoMocks.mockDataStructureWithComponents(AGENCY_1, ARTEFACT_2_CODE, VERSION_1));
                            dataStructures.add(DataStructuresDoMocks.mockDataStructureWithComponents(AGENCY_1, ARTEFACT_2_CODE, VERSION_2));
                            dataStructures.add(DataStructuresDoMocks.mockDataStructureWithComponents(AGENCY_2, ARTEFACT_3_CODE, VERSION_1));
                            return new PagedResult<DataStructureDefinitionVersionMetamac>(dataStructures, dataStructures.size(), dataStructures.size(), dataStructures.size(),
                                    dataStructures.size() * 10, 0);
                        }
                    };
                });
    }

    @Override
    protected void resetMocks() throws MetamacException {
        dsdsService = applicationContext.getBean(DataStructureDefinitionMetamacService.class);
        reset(dsdsService);
        structureVersionRepository = applicationContext.getBean(StructureVersionRepository.class);
        reset(structureVersionRepository);

        mockRetrieveStructureVersionByVersion();
        mockFindDataStructuresByCondition();
    }

    private String getUriStructures(String agencyID, String resourceID, String version) {
        return getUriMaintainableArtefacts(agencyID, resourceID, version);
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return "datastructures";
    }

    @Override
    protected String getSupathItems() {
        fail("unsupported");
        return null;
    }
}