package org.siemac.metamac.srm.rest.internal.v1_0.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.NOT_EXISTS;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_LATEST;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraints;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RegionReference;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.constraint.utils.ContentConstraintsDoMocks;
import org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks;

import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraintProperties;
import com.arte.statistic.sdmx.srm.core.constraint.domain.KeyValue;
import com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue;
import com.arte.statistic.sdmx.srm.core.constraint.serviceapi.ConstraintsService;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationRepository;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class SrmRestInternalFacadeV10ConstraintsTest extends SrmRestInternalFacadeV10BaseTest {

    private ConstraintsService     contraintsService;
    private OrganisationRepository organisationRepository;

    public static String           CONTENT_CONSTRAINT_1_CODE      = "contentConstraint1";
    public static String           CONTENT_CONSTRAINT_1_VERSION_1 = "01.000";
    public static String           CONTENT_CONSTRAINT_2_CODE      = "contentConstraint2";
    public static String           CONTENT_CONSTRAINT_2_VERSION_1 = "01.000";
    public static String           CONTENT_CONSTRAINT_3_CODE      = "contentConstraint3";
    public static String           CONTENT_CONSTRAINT_3_VERSION_1 = "01.000";

    @Test
    public void testFindContentConstraints() throws Exception {
        // without limits
        testFindContentConstraints(null, null, null, null, null, null, null, null);
        // without limits
        testFindContentConstraints(null, null, null, "10000", null, null, null, null);
        // without limits, first page
        testFindContentConstraints(null, null, null, null, "0", null, null, null);
        // first page with pagination
        testFindContentConstraints(null, null, null, "2", "0", null, null, null);
        // other page with pagination
        testFindContentConstraints(null, null, null, "2", "2", null, null, null);
        // query by id, without limits
        testFindContentConstraints(null, null, null, null, null, QUERY_ID_LIKE_1, null, null);
        // query by id and name, without limits
        testFindContentConstraints(null, null, null, null, null, QUERY_ID_LIKE_1_NAME_LIKE_2, null, null);
        // latest
        testFindContentConstraints(null, null, null, null, null, QUERY_LATEST, null, null);
        // query by id and name, first page
        testFindContentConstraints(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, null, null);
        // query by id and name, first page
        testFindContentConstraints(null, null, null, "1", "0", QUERY_ID_LIKE_1_NAME_LIKE_2, ORDER_BY_ID_DESC, null);
    }

    private void testFindContentConstraints(String agencyID, String resourceID, String version, String limit, String offset, String query, String orderBy, String includeDraft) throws Exception {
        resetMocks();

        // Find
        ContentConstraints findContentConstraints = null;
        if (agencyID == null) {
            findContentConstraints = getSrmRestInternalFacadeClientXml().findContentConstraints(query, orderBy, limit, offset, includeDraft);
        } else if (resourceID == null) {
            findContentConstraints = getSrmRestInternalFacadeClientXml().findContentConstraints(agencyID, query, orderBy, limit, offset, includeDraft);
        } else {
            findContentConstraints = getSrmRestInternalFacadeClientXml().findContentConstraints(agencyID, resourceID, query, orderBy, limit, offset, includeDraft);
        }

        assertNotNull(findContentConstraints);
        assertEquals(SrmRestConstants.KIND_CONTENT_CONSTRAINTS, findContentConstraints.getKind());

        // Verify with Mockito
        // TODO verifyFindConstraints(contraintsService, agencyID, resourceID, version, limit, offset, query, orderBy);
    }

    @Test
    public void test_GET_RetrieveContentConstraint() throws Exception {
        {
            WebClient create = WebClient.create(baseApi);
            incrementRequestTimeOut(create); // Timeout
            // create.path("contentConstraints");
            create.path("contentConstraints/{0}/{1}/{2}", AGENCY_1, CONTENT_CONSTRAINT_1_CODE, CONTENT_CONSTRAINT_1_VERSION_1);
            Response response = create.get();

            InputStream responseExpected = SrmRestInternalFacadeV10ConstraintsTest.class.getResourceAsStream("/responses/constraints/retrieveContentConstraint.id1.xml");
            assertEquals(200, response.getStatus());
            assertInputStream(responseExpected, (InputStream) response.getEntity(), false);
        }
    }

    @Test
    public void test_GET_FindContentConstraints() throws Exception {
        {
            WebClient create = WebClient.create(baseApi);
            incrementRequestTimeOut(create); // Timeout
            create.path("contentConstraints");
            Response response = create.get();

            InputStream responseExpected = SrmRestInternalFacadeV10ConstraintsTest.class.getResourceAsStream("/responses/constraints/findContentConstraints.xml");
            assertEquals(200, response.getStatus());
            assertInputStream(responseExpected, (InputStream) response.getEntity(), false);
        }
    }

    @Test
    public void test_PUT_ContentConstraint() throws Exception {
        {
            WebClient create = WebClient.create(baseApi);
            incrementRequestTimeOut(create); // Timeout
            create.path("contentConstraints");

            ContentConstraint contentConstraint = ContentConstraintsDoMocks.mockContentConstraint(AGENCY_1, CONTENT_CONSTRAINT_1_CODE, CONTENT_CONSTRAINT_1_VERSION_1);
            contentConstraint.getRegions().clear();
            org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ContentConstraint putObject = contentConstraintsDo2RestMapper.toContentConstraint(contentConstraint);

            Response response = create.put(putObject);

            InputStream responseExpected = SrmRestInternalFacadeV10ConstraintsTest.class.getResourceAsStream("/responses/constraints/putContentConstraint.xml");
            assertEquals(201, response.getStatus());
            assertInputStream(responseExpected, (InputStream) response.getEntity(), false);
        }
    }

    @Test
    public void test_PUT_RegionForContentConstraint() throws Exception {
        {
            WebClient create = WebClient.create(baseApi);
            incrementRequestTimeOut(create); // Timeout
            StringBuilder pathBld = new StringBuilder("contentConstraints");
            pathBld.append("/").append(AGENCY_1).append("/").append(CONTENT_CONSTRAINT_1_CODE).append("/").append(CONTENT_CONSTRAINT_1_VERSION_1).append("/").append("regions");
            create.path(pathBld.toString());

            ContentConstraint contentConstraint = ContentConstraintsDoMocks.mockContentConstraint(AGENCY_1, CONTENT_CONSTRAINT_1_CODE, CONTENT_CONSTRAINT_1_VERSION_1);
            RegionValue regionValue = contentConstraint.getRegions().iterator().next();
            RegionReference putObject = contentConstraintsDo2RestMapper.toRegionReference(GeneratorUrnUtils.generateContentConstraintUrn(contentConstraint.getMaintainableArtefact()), regionValue);
            Response response = create.put(putObject);

            InputStream responseExpected = SrmRestInternalFacadeV10ConstraintsTest.class.getResourceAsStream("/responses/constraints/putRegion.xml");
            assertEquals(201, response.getStatus());
            assertInputStream(responseExpected, (InputStream) response.getEntity(), false);
        }
    }

    @Override
    protected void resetMocks() throws MetamacException {
        contraintsService = applicationContext.getBean(ConstraintsService.class);
        reset(contraintsService);

        organisationRepository = applicationContext.getBean(OrganisationRepository.class);
        reset(contraintsService);

        mockFindConstraintsByCondition();
        mockRetrieveOrganisationsByIdAsMaintainer();
        mockCreateContentConstraint();
        mockSaveRegion();
        mockUpdateRegionKeys();
        mockFindRegionValueByUrn();

    }
    @SuppressWarnings("unchecked")
    private void mockFindConstraintsByCondition() throws MetamacException {
        when(contraintsService.findContentConstraintsByCondition(any(ServiceContext.class), any(List.class), any(PagingParameter.class))).thenAnswer(
                new Answer<PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint>>() {

                    @Override
                    public org.fornax.cartridges.sculptor.framework.domain.PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> answer(InvocationOnMock invocation)
                            throws Throwable {
                        List<ConditionalCriteria> conditions = (List<ConditionalCriteria>) invocation.getArguments()[1];
                        String agencyID = getAgencyIdFromConditionalCriteria(conditions, ContentConstraintProperties.maintainableArtefact());
                        String resourceID = getItemSchemeIdFromConditionalCriteria(conditions, ContentConstraintProperties.maintainableArtefact());
                        String version = getVersionFromConditionalCriteria(conditions, ContentConstraintProperties.maintainableArtefact());
                        Boolean latest = getVersionLatestFromConditionalCriteria(conditions, ContentConstraintProperties.maintainableArtefact());
                        if (agencyID != null && resourceID != null && (version != null || Boolean.TRUE.equals(latest))) {
                            // Retrieve one
                            com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint contentConstraint = null;
                            if (NOT_EXISTS.equals(agencyID) || NOT_EXISTS.equals(resourceID) || NOT_EXISTS.equals(version)) {
                                contentConstraint = null;
                            } else if (AGENCY_1.equals(agencyID) && CONTENT_CONSTRAINT_1_CODE.equals(resourceID) && (CONTENT_CONSTRAINT_1_VERSION_1.equals(version) || Boolean.TRUE.equals(latest))) {
                                contentConstraint = ContentConstraintsDoMocks.mockContentConstraint(AGENCY_1, CONTENT_CONSTRAINT_1_CODE, CONTENT_CONSTRAINT_1_VERSION_1);
                            } else {
                                fail();
                            }
                            List<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> contentConstraints = new ArrayList<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint>();
                            if (contentConstraint != null) {
                                contentConstraints.add(contentConstraint);
                            }
                            return new PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint>(contentConstraints, 0, contentConstraints.size(), contentConstraints.size());
                        } else {
                            // any
                            List<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> contentConstraints = new ArrayList<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint>();
                            contentConstraints.add(ContentConstraintsDoMocks.mockContentConstraint(AGENCY_1, CONTENT_CONSTRAINT_1_CODE, CONTENT_CONSTRAINT_1_VERSION_1));
                            contentConstraints.add(ContentConstraintsDoMocks.mockContentConstraint(AGENCY_1, CONTENT_CONSTRAINT_2_CODE, CONTENT_CONSTRAINT_2_VERSION_1));
                            contentConstraints.add(ContentConstraintsDoMocks.mockContentConstraint(AGENCY_2, CONTENT_CONSTRAINT_3_CODE, CONTENT_CONSTRAINT_3_VERSION_1));
                            return new PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint>(contentConstraints, contentConstraints.size(), contentConstraints.size(),
                                    contentConstraints.size(), contentConstraints.size() * 10, 0);
                        }
                    };
                });
    }

    @SuppressWarnings("unchecked")
    private void mockCreateContentConstraint() throws MetamacException {
        when(contraintsService.createContentConstraint(any(ServiceContext.class), any(ContentConstraint.class), any(Boolean.class))).thenAnswer(
                new Answer<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint>() {

                    @Override
                    public ContentConstraint answer(InvocationOnMock invocation) throws Throwable {

                        ContentConstraint result = (ContentConstraint) invocation.getArguments()[1];
                        result.getMaintainableArtefact().setUrn(GeneratorUrnUtils.generateContentConstraintUrn(result.getMaintainableArtefact()));
                        return result;
                    }

                });
    }

    @SuppressWarnings("unchecked")
    private void mockSaveRegion() throws MetamacException {
        when(contraintsService.saveRegion(any(ServiceContext.class), any(String.class), any(RegionValue.class))).thenAnswer(
                new Answer<com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue>() {

                    @Override
                    public RegionValue answer(InvocationOnMock invocation) throws Throwable {
                        RegionValue result = (RegionValue) invocation.getArguments()[2];
                        return result;
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void mockUpdateRegionKeys() throws MetamacException {
        when(contraintsService.updateRegionKeys(any(ServiceContext.class), any(String.class), any(String.class), anySetOf(KeyValue.class))).thenAnswer(
                new Answer<com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue>() {

                    @Override
                    public RegionValue answer(InvocationOnMock invocation) throws Throwable {
                        ContentConstraint contentConstraint = ContentConstraintsDoMocks.mockContentConstraint(AGENCY_1, CONTENT_CONSTRAINT_1_CODE, CONTENT_CONSTRAINT_1_VERSION_1);
                        RegionValue regionValue = contentConstraint.getRegions().iterator().next();
                        return regionValue;
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private void mockRetrieveOrganisationsByIdAsMaintainer() throws MetamacException {
        when(organisationRepository.retrieveOrganisationsByIdAsMaintainer(any(String.class))).thenAnswer(new Answer<com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation>() {

            @Override
            public Organisation answer(InvocationOnMock invocation) throws Throwable {
                OrganisationSchemeVersionMetamac itemScheme1 = OrganisationsDoMocks.mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
                Organisation organisation = OrganisationsDoMocks.mockOrganisation(ITEM_1_CODE, itemScheme1, null, OrganisationTypeEnum.AGENCY);

                return organisation;
            }

        });
    }

    @SuppressWarnings("unchecked")
    private void mockFindRegionValueByUrn() throws MetamacException {
        when(contraintsService.findRegionValueByUrn(any(ServiceContext.class), any(String.class), any(String.class))).thenAnswer(
                new Answer<com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue>() {

                    @Override
                    public RegionValue answer(InvocationOnMock invocation) throws Throwable {
                        ContentConstraint contentConstraint = ContentConstraintsDoMocks.mockContentConstraint(AGENCY_1, CONTENT_CONSTRAINT_1_CODE, CONTENT_CONSTRAINT_1_VERSION_1);
                        RegionValue regionValue = contentConstraint.getRegions().iterator().next();
                        return regionValue;
                    }

                });
    }

    @Override
    protected String getSupathMaintainableArtefacts() {
        return "contentConstraints";
    }

    @Override
    protected String getSupathItems() {
        return "TODO_SUBPATH";
    }

}
