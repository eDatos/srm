package org.siemac.metamac.srm.core.facade.serviceapi;

import static com.arte.statistic.sdmx.srm.core.organisation.serviceapi.utils.OrganisationsAsserts.assertEqualsOrganisationDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacAsserts.assertEqualsOrganisationSchemeMetamacDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaConjunctionRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaOrderEnum;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeOrganisationsTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    // IMPORTANT: Metadata transformation is tested in Do2Dto tests

    // ---------------------------------------------------------------------------------------
    // ORGANISATION SCHEMES
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveOrganisationSchemeByUrn() throws Exception {

        // Retrieve
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V1);

        // Validate
        assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeMetamacDto.getUrn());
    }

    @Test
    public void testRetrieveOrganisationSchemeVersions() throws Exception {

        // Retrieve all versions
        List<OrganisationSchemeMetamacDto> organisationSchemeMetamacDtos = srmCoreServiceFacade.retrieveOrganisationSchemeVersions(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V1);

        // Validate
        assertEquals(2, organisationSchemeMetamacDtos.size());
        assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeMetamacDtos.get(0).getUrn());
        assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeMetamacDtos.get(1).getUrn());
    }

    @Test
    public void testCreateOrganisationScheme() throws Exception {

        // Create
        OrganisationSchemeMetamacDto organisationSchemeDto = OrganisationsMetamacDtoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
        OrganisationSchemeMetamacDto organisationSchemeMetamacCreated = srmCoreServiceFacade.createOrganisationScheme(getServiceContextAdministrador(), organisationSchemeDto);

        // Validate some metadata
        assertEquals(organisationSchemeDto.getCode(), organisationSchemeMetamacCreated.getCode());
        assertNotNull(organisationSchemeMetamacCreated.getUrn());
        assertEquals(ProcStatusEnum.DRAFT, organisationSchemeMetamacCreated.getLifeCycle().getProcStatus());
        assertEquals(Long.valueOf(0), organisationSchemeMetamacCreated.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateOrganisationScheme() throws Exception {

        // Update
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2);
        organisationSchemeMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        OrganisationSchemeMetamacDto organisationSchemeMetamacDtoUpdated = srmCoreServiceFacade.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeMetamacDto);

        // Validate
        assertNotNull(organisationSchemeMetamacDto);
        assertEqualsOrganisationSchemeMetamacDto(organisationSchemeMetamacDto, organisationSchemeMetamacDtoUpdated);
        assertTrue(organisationSchemeMetamacDtoUpdated.getVersionOptimisticLocking() > organisationSchemeMetamacDto.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateOrganisationSchemeErrorOptimisticLocking() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V2;

        OrganisationSchemeMetamacDto organisationSchemeMetamacDtoSession1 = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), organisationSchemeMetamacDtoSession1.getVersionOptimisticLocking());
        organisationSchemeMetamacDtoSession1.setIsPartial(Boolean.TRUE);

        OrganisationSchemeMetamacDto organisationSchemeMetamacDtoSession2 = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), organisationSchemeMetamacDtoSession2.getVersionOptimisticLocking());
        organisationSchemeMetamacDtoSession2.setIsPartial(Boolean.TRUE);

        // Update by session 1
        OrganisationSchemeMetamacDto organisationSchemeMetamacDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateOrganisationScheme(getServiceContextAdministrador(),
                organisationSchemeMetamacDtoSession1);
        assertTrue(organisationSchemeMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking() > organisationSchemeMetamacDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateOrganisationScheme(getServiceContextAdministrador(), organisationSchemeMetamacDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        organisationSchemeMetamacDtoSession1AfterUpdate1.setIsPartial(Boolean.FALSE);
        OrganisationSchemeMetamacDto organisationSchemeMetamacDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateOrganisationScheme(getServiceContextAdministrador(),
                organisationSchemeMetamacDtoSession1AfterUpdate1);
        assertTrue(organisationSchemeMetamacDtoSession1AfterUpdate2.getVersionOptimisticLocking() > organisationSchemeMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteOrganisationScheme() throws Exception {
        String urn = ORGANISATION_SCHEME_2_V1;

        // Delete organisation scheme only with version in draft
        srmCoreServiceFacade.deleteOrganisationScheme(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
            fail("OrganisationScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testFindOrganisationSchemesByCondition() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaOrderEnum.CODE.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        // Find all
        {
            MetamacCriteriaResult<OrganisationSchemeMetamacDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(12, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_2_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_3_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_4_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_5_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_6_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_7_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_7_V2, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_8_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_9_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_ROOT_1_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }

        // Find by type
        {
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.TYPE.name(), OrganisationSchemeTypeEnum.AGENCY_SCHEME,
                    OperationType.EQ));

            MetamacCriteriaResult<OrganisationSchemeMetamacDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_2_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_8_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_9_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_ROOT_1_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }

        // Find by Name
        {
            metamacCriteria
                    .setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "Nombre organisationScheme-1-v1", OperationType.EQ));

            MetamacCriteriaResult<OrganisationSchemeMetamacDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(1, result.getPaginatorResult().getTotalResults().intValue());

            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.NAME.name(), "NOT FOUND", OperationType.EQ));

            result = srmCoreServiceFacade.findOrganisationSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);
            assertEquals(0, result.getPaginatorResult().getTotalResults().intValue());
        }
    }

    @Test
    public void testFindOrganisationSchemesByProcStatus() throws Exception {

        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaOrderEnum.CODE.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(order);
        }

        // Pagination
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setFirstResult(0);
        metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ProcStatusEnum.DRAFT, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<OrganisationSchemeMetamacDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(2, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_1_V2, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_2_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.DRAFT, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
        {
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.PROC_STATUS.name(),
                    ProcStatusEnum.INTERNALLY_PUBLISHED, OperationType.EQ);
            metamacCriteria.setRestriction(propertyRestriction);

            MetamacCriteriaResult<OrganisationSchemeMetamacDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(4, result.getPaginatorResult().getTotalResults().intValue());
            int i = 0;
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_1_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_3_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_7_V2, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            {
                OrganisationSchemeMetamacDto organisationSchemeMetamacDto = result.getResults().get(i++);
                assertEquals(ORGANISATION_SCHEME_9_V1, organisationSchemeMetamacDto.getUrn());
                assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeMetamacDto.getLifeCycle().getProcStatus());
            }
            assertEquals(result.getPaginatorResult().getTotalResults().intValue(), i);
        }
    }

    @Test
    public void testFindOrganisationSchemesPaginated() throws Exception {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        // Order
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaOrderEnum.CODE.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setType(OrderTypeEnum.ASC);
            order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(order);
        }
        // Pagination
        int maxResultSize = 2;
        metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
        metamacCriteria.getPaginator().setMaximumResultSize(maxResultSize);
        metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

        {
            int firstResult = 0;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<OrganisationSchemeMetamacDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(maxResultSize, result.getPaginatorResult().getMaximumResultSize().intValue());
            assertEquals(ORGANISATION_SCHEME_1_V1, result.getResults().get(0).getUrn());
        }
        {
            int firstResult = 2;
            metamacCriteria.getPaginator().setFirstResult(firstResult);

            MetamacCriteriaResult<OrganisationSchemeMetamacDto> result = srmCoreServiceFacade.findOrganisationSchemesByCondition(getServiceContextAdministrador(), metamacCriteria);

            assertEquals(firstResult, result.getPaginatorResult().getFirstResult().intValue());
            assertEquals(ORGANISATION_SCHEME_2_V1, result.getResults().get(0).getUrn());
        }
    }

    @Test
    public void testSendOrganisationSchemeToProductionValidation() throws Exception {

        String urn = ORGANISATION_SCHEME_2_V1;

        // Sends to production validation
        OrganisationSchemeMetamacDto organisationSchemeDto = srmCoreServiceFacade.sendOrganisationSchemeToProductionValidation(getServiceContextAdministrador(), urn);

        // Validation
        organisationSchemeDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, organisationSchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testSendOrganisationSchemeToDiffusionValidation() throws Exception {

        String urn = ORGANISATION_SCHEME_5_V1;

        // Sends to diffusion validation
        OrganisationSchemeMetamacDto organisationSchemeDto = srmCoreServiceFacade.sendOrganisationSchemeToDiffusionValidation(getServiceContextAdministrador(), urn);

        // Validation
        organisationSchemeDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, organisationSchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testRejectOrganisationSchemeProductionValidation() throws Exception {

        String urn = ORGANISATION_SCHEME_5_V1;

        // Reject
        OrganisationSchemeMetamacDto organisationSchemeDto = srmCoreServiceFacade.rejectOrganisationSchemeProductionValidation(getServiceContextAdministrador(), urn);

        // Validation
        organisationSchemeDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testRejectOrganisationSchemeDiffusionValidation() throws Exception {

        String urn = ORGANISATION_SCHEME_6_V1;

        // Reject
        OrganisationSchemeMetamacDto organisationSchemeDto = srmCoreServiceFacade.rejectOrganisationSchemeDiffusionValidation(getServiceContextAdministrador(), urn);

        // Validation
        organisationSchemeDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.VALIDATION_REJECTED, organisationSchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testPublishInternallyOrganisationScheme() throws Exception {

        String urn = ORGANISATION_SCHEME_6_V1;

        // Publish
        OrganisationSchemeMetamacDto organisationSchemeDto = srmCoreServiceFacade.publishOrganisationSchemeInternally(getServiceContextAdministrador(), urn);

        // Validation
        organisationSchemeDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, organisationSchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testPublishExternallyOrganisationScheme() throws Exception {

        String urn = ORGANISATION_SCHEME_7_V2;

        // Publish
        OrganisationSchemeMetamacDto organisationSchemeDto = srmCoreServiceFacade.publishOrganisationSchemeExternally(getServiceContextAdministrador(), urn);

        // Validation
        organisationSchemeDto = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), urn);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, organisationSchemeDto.getLifeCycle().getProcStatus());
    }

    @Test
    public void testVersioningOrganisationScheme() throws Exception {

        // Versioning
        String urn = ORGANISATION_SCHEME_3_V1;
        OrganisationSchemeMetamacDto organisationSchemeDtoNewVersion = srmCoreServiceFacade.versioningOrganisationScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate
        organisationSchemeDtoNewVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), organisationSchemeDtoNewVersion.getUrn());
        assertEquals("02.000", organisationSchemeDtoNewVersion.getVersionLogic());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATIONSCHEME03(02.000)", organisationSchemeDtoNewVersion.getUrn());
    }

    @Test
    public void testEndOrganisationSchemeValidity() throws Exception {
        OrganisationSchemeMetamacDto organisationSchemeMetamacDto = srmCoreServiceFacade.endOrganisationSchemeValidity(getServiceContextAdministrador(), ORGANISATION_SCHEME_7_V1);
        assertTrue(DateUtils.isSameDay(new Date(), organisationSchemeMetamacDto.getValidTo()));
    }
    // ---------------------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveOrganisationByUrn() throws Exception {
        OrganisationMetamacDto organisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationMetamacDto.getUrn());
    }

    @Test
    public void testFindAllOrganisations() throws Exception {

        // Find all
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_CODE.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(24, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(24, organisationsPagedResult.getResults().size());
            assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
            assertEquals(ORGANISATION_SCHEME_1_V1, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_4_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_5_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_6_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_7_V2_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_8_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_9_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());

            assertEquals(organisationsPagedResult.getResults().size(), i);
        }

    }

    @Test
    public void testFindOrganisationsInFirstLevel() throws Exception {
        // Find only organisations in first level
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_CODE.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Restrictions
            MetamacCriteriaPropertyRestriction propertyRestriction = new MetamacCriteriaPropertyRestriction();
            propertyRestriction.setPropertyName(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_PARENT_URN.name());
            propertyRestriction.setOperationType(OperationType.IS_NULL);
            metamacCriteria.setRestriction(propertyRestriction);

            // Find
            MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(17, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(17, organisationsPagedResult.getResults().size());
            assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
            assertEquals(ORGANISATION_SCHEME_1_V1, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_4_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_5_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_6_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_7_V2_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_8_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_9_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());

            assertEquals(organisationsPagedResult.getResults().size(), i);
        }
    }

    @Test
    public void testFindOrganisationsBySchemeTypeOrganisationUnit() throws Exception {
        // Find by organisation scheme type: organisation unit
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            MetamacCriteriaOrder orderOrganisationSchemeUrn = new MetamacCriteriaOrder();
            orderOrganisationSchemeUrn.setType(OrderTypeEnum.ASC);
            orderOrganisationSchemeUrn.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_URN.name());
            metamacCriteria.getOrdersBy().add(orderOrganisationSchemeUrn);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Restrictions
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_TYPE.name(),
                    OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, OperationType.EQ));

            // Find
            MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(18, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(18, organisationsPagedResult.getResults().size());
            assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_3_V1_ORGANISATION_2_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_4_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_5_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_6_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_7_V2_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());

            assertEquals(organisationsPagedResult.getResults().size(), i);
        }
    }

    @Test
    public void testFindOrganisationsBySchemeTypeAgency() throws Exception {
        // Find by organisation scheme type: agency
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_CODE.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Restrictions
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_TYPE.name(), OrganisationSchemeTypeEnum.AGENCY_SCHEME,
                    OperationType.EQ));

            // Find
            MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(6, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(6, organisationsPagedResult.getResults().size());
            assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_2_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_8_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_9_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(organisationsPagedResult.getResults().size(), i);
        }

        // Find by name (like), code (like) and organisation scheme urn
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.NAME.name(), "Nombre organisationScheme-1-v2-organisation-2-", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.CODE.name(), "ORGANISATION02", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_URN.name(), ORGANISATION_SCHEME_1_V2, OperationType.EQ));
            metamacCriteria.setRestriction(conjunctionRestriction);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(2, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(2, organisationsPagedResult.getResults().size());
            assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
            assertEquals(ORGANISATION_SCHEME_1_V2, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(organisationsPagedResult.getResults().size(), i);
        }

        // Find by organisation scheme urn paginated
        {

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_URN.name(), ORGANISATION_SCHEME_1_V2, OperationType.EQ));

            // First page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(0);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, organisationsPagedResult.getResults().size());
                assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
                assertEquals(ORGANISATION_SCHEME_1_V2, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(organisationsPagedResult.getResults().size(), i);
            }
            // Second page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(3);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, organisationsPagedResult.getResults().size());
                assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
                assertEquals(ORGANISATION_SCHEME_1_V2, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(organisationsPagedResult.getResults().size(), i);
            }
            // Third page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(6);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(2, organisationsPagedResult.getResults().size());
                assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
                assertEquals(ORGANISATION_SCHEME_1_V2, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(organisationsPagedResult.getResults().size(), i);
            }
        }
    }

    @Test
    public void testFindOrganisationsByNameCodeAndSchemeUrn() throws Exception {
        // Find by name (like), code (like) and organisation scheme urn
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.NAME.name(), "Nombre organisationScheme-1-v2-organisation-2-", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.CODE.name(), "ORGANISATION02", OperationType.LIKE));
            conjunctionRestriction.getRestrictions().add(
                    new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_URN.name(), ORGANISATION_SCHEME_1_V2, OperationType.EQ));
            metamacCriteria.setRestriction(conjunctionRestriction);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(2, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(2, organisationsPagedResult.getResults().size());
            assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
            assertEquals(ORGANISATION_SCHEME_1_V2, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(organisationsPagedResult.getResults().size(), i);
        }
    }

    @Test
    public void testFindOrganisationsBySchemeUrnPaginated() throws Exception {
        // Find by organisation scheme urn paginated
        {

            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            metamacCriteria.setRestriction(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.ORGANISATION_SCHEME_URN.name(), ORGANISATION_SCHEME_1_V2, OperationType.EQ));

            // First page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(0);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, organisationsPagedResult.getResults().size());
                assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
                assertEquals(ORGANISATION_SCHEME_1_V2, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(organisationsPagedResult.getResults().size(), i);
            }
            // Second page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(3);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(3, organisationsPagedResult.getResults().size());
                assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
                assertEquals(ORGANISATION_SCHEME_1_V2, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(organisationsPagedResult.getResults().size(), i);
            }
            // Third page
            {
                // Pagination
                metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
                metamacCriteria.getPaginator().setFirstResult(6);
                metamacCriteria.getPaginator().setMaximumResultSize(3);
                metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

                // Find
                MetamacCriteriaResult<OrganisationMetamacDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsByCondition(getServiceContextAdministrador(), metamacCriteria);

                // Validate
                assertEquals(8, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
                assertEquals(2, organisationsPagedResult.getResults().size());
                assertTrue(organisationsPagedResult.getResults().get(0) instanceof OrganisationMetamacDto);
                assertEquals(ORGANISATION_SCHEME_1_V2, organisationsPagedResult.getResults().get(0).getItemSchemeVersionUrn());

                int i = 0;
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationsPagedResult.getResults().get(i++).getUrn());
                assertEquals(organisationsPagedResult.getResults().size(), i);
            }
        }
    }

    @Test
    public void testFindOrganisationsAsMaintainerByCondition() throws Exception {

        // Find all
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_CODE.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.ORGANISATION_SCHEME_URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }
            {
                MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                order.setType(OrderTypeEnum.ASC);
                order.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
                metamacCriteria.getOrdersBy().add(order);
            }

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<RelatedResourceDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsAsMaintainerByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(4, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(4, organisationsPagedResult.getResults().size());

            int i = 0;
            assertEquals("ORGANISATION01", organisationsPagedResult.getResults().get(i).getCode());
            assertEquals(ORGANISATION_SCHEME_8_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals("ORGANISATION01", organisationsPagedResult.getResults().get(i).getCode());
            assertEquals(ORGANISATION_SCHEME_9_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals("SDMX01", organisationsPagedResult.getResults().get(i).getCode());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_1, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals("SDMX02", organisationsPagedResult.getResults().get(i).getCode());
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());

            assertEquals(organisationsPagedResult.getResults().size(), i);
        }

        // Find by code (like)
        {
            MetamacCriteria metamacCriteria = new MetamacCriteria();
            // Order
            metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());

            MetamacCriteriaOrder orderUrn = new MetamacCriteriaOrder();
            orderUrn.setType(OrderTypeEnum.ASC);
            orderUrn.setPropertyName(OrganisationMetamacCriteriaOrderEnum.URN.name());
            metamacCriteria.getOrdersBy().add(orderUrn);

            // Restrictions
            MetamacCriteriaConjunctionRestriction conjunctionRestriction = new MetamacCriteriaConjunctionRestriction();
            conjunctionRestriction.getRestrictions().add(new MetamacCriteriaPropertyRestriction(OrganisationMetamacCriteriaPropertyEnum.CODE.name(), "SDMX02", OperationType.LIKE));
            metamacCriteria.setRestriction(conjunctionRestriction);

            // Pagination
            metamacCriteria.setPaginator(new MetamacCriteriaPaginator());
            metamacCriteria.getPaginator().setFirstResult(0);
            metamacCriteria.getPaginator().setMaximumResultSize(Integer.MAX_VALUE);
            metamacCriteria.getPaginator().setCountTotalResults(Boolean.TRUE);

            // Find
            MetamacCriteriaResult<RelatedResourceDto> organisationsPagedResult = srmCoreServiceFacade.findOrganisationsAsMaintainerByCondition(getServiceContextAdministrador(), metamacCriteria);

            // Validate
            assertEquals(1, organisationsPagedResult.getPaginatorResult().getTotalResults().intValue());
            assertEquals(1, organisationsPagedResult.getResults().size());

            int i = 0;
            assertEquals(ORGANISATION_SCHEME_100_V1_ORGANISATION_2, organisationsPagedResult.getResults().get(i++).getUrn());
            assertEquals(organisationsPagedResult.getResults().size(), i);
        }
    }

    @Test
    public void testCreateOrganisation() throws Exception {
        OrganisationMetamacDto organisationMetamacDto = OrganisationsMetamacDtoMocks.mockOrganisationDto(OrganisationTypeEnum.ORGANISATION_UNIT);
        organisationMetamacDto.setItemSchemeVersionUrn(ORGANISATION_SCHEME_1_V2);

        OrganisationMetamacDto organisationMetamacDtoCreated = srmCoreServiceFacade.createOrganisation(getServiceContextAdministrador(), organisationMetamacDto);
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=SDMX01:ORGANISATIONSCHEME01(02.000)." + organisationMetamacDto.getCode(), organisationMetamacDtoCreated.getUrn());
        assertEqualsOrganisationDto(organisationMetamacDto, organisationMetamacDtoCreated);
    }

    @Test
    public void testCreateOrganisationWithOrganisationParent() throws Exception {
        OrganisationMetamacDto organisationMetamacDto = OrganisationsMetamacDtoMocks.mockOrganisationDto(OrganisationTypeEnum.ORGANISATION_UNIT);
        organisationMetamacDto.setItemParentUrn(ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        organisationMetamacDto.setItemSchemeVersionUrn(ORGANISATION_SCHEME_1_V2);

        OrganisationMetamacDto organisationMetamacDtoCreated = srmCoreServiceFacade.createOrganisation(getServiceContextAdministrador(), organisationMetamacDto);
        assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisationMetamacDtoCreated.getItemParentUrn());
        assertEqualsOrganisationDto(organisationMetamacDto, organisationMetamacDtoCreated);
    }

    @Test
    public void testCreateOrganisationErrorParentNotExists() throws Exception {
        OrganisationMetamacDto organisationMetamacDto = OrganisationsMetamacDtoMocks.mockOrganisationDto(OrganisationTypeEnum.ORGANISATION_UNIT);
        organisationMetamacDto.setItemParentUrn(NOT_EXISTS);
        organisationMetamacDto.setItemSchemeVersionUrn(ORGANISATION_SCHEME_1_V2);

        try {
            srmCoreServiceFacade.createOrganisation(getServiceContextAdministrador(), organisationMetamacDto);
            fail("wrong parent");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(NOT_EXISTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateOrganisation() throws Exception {
        OrganisationMetamacDto organisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
        organisationMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        organisationMetamacDto.setDescription(MetamacMocks.mockInternationalStringDto());

        OrganisationMetamacDto organisationMetamacDtoUpdated = srmCoreServiceFacade.updateOrganisation(getServiceContextAdministrador(), organisationMetamacDto);

        assertEqualsOrganisationDto(organisationMetamacDto, organisationMetamacDtoUpdated);
        assertTrue(organisationMetamacDtoUpdated.getVersionOptimisticLocking() > organisationMetamacDto.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteOrganisation() throws Exception {

        String urn = ORGANISATION_SCHEME_1_V2_ORGANISATION_3;

        // Delete organisation
        srmCoreServiceFacade.deleteOrganisation(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), urn);
            fail("Organisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRetrieveOrganisationsByOrganisationSchemeUrn() throws Exception {

        // Retrieve
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
        List<ItemHierarchyDto> organisations = srmCoreServiceFacade.retrieveOrganisationsByOrganisationSchemeUrn(getServiceContextAdministrador(), organisationSchemeUrn);

        // Validate
        assertEquals(4, organisations.size());
        {
            // Organisation 01
            ItemHierarchyDto organisation = organisations.get(0);
            assertTrue(organisation.getItem() instanceof OrganisationMetamacDto);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_1, organisation.getItem().getUrn());
            assertEquals(0, organisation.getChildren().size());
        }
        {
            // Organisation 02
            ItemHierarchyDto organisation = organisations.get(1);
            assertTrue(organisation.getItem() instanceof OrganisationMetamacDto);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2, organisation.getItem().getUrn());
            assertEquals(1, organisation.getChildren().size());
            {
                // Organisation 02 01
                ItemHierarchyDto organisationChild = (ItemHierarchyDto) organisation.getChildren().get(0);
                assertTrue(organisationChild.getItem() instanceof OrganisationMetamacDto);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1, organisationChild.getItem().getUrn());
                assertEquals(1, organisationChild.getChildren().size());
                {
                    // Organisation 02 01 01
                    ItemHierarchyDto organisationChildChild = (ItemHierarchyDto) organisationChild.getChildren().get(0);
                    assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_2_1_1, organisationChildChild.getItem().getUrn());
                    assertEquals(0, organisationChildChild.getChildren().size());
                }
            }
        }
        {
            // Organisation 03
            ItemHierarchyDto organisation = organisations.get(2);
            assertTrue(organisation.getItem() instanceof OrganisationMetamacDto);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_3, organisation.getItem().getUrn());
            assertEquals(0, organisation.getChildren().size());
        }
        {
            // Organisation 04
            ItemHierarchyDto organisation = organisations.get(3);
            assertTrue(organisation.getItem() instanceof OrganisationMetamacDto);
            assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4, organisation.getItem().getUrn());
            assertEquals(1, organisation.getChildren().size());
            {
                // Organisation 04 01
                ItemHierarchyDto organisationChild = (ItemHierarchyDto) organisation.getChildren().get(0);
                assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1, organisationChild.getItem().getUrn());
                assertEquals(1, organisationChild.getChildren().size());
                {
                    // Organisation 04 01 01
                    ItemHierarchyDto organisationChildChild = (ItemHierarchyDto) organisationChild.getChildren().get(0);
                    assertEquals(ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1, organisationChildChild.getItem().getUrn());
                    assertEquals(0, organisationChildChild.getChildren().size());
                }
            }
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmOrganisationsTest.xml";
    }
}