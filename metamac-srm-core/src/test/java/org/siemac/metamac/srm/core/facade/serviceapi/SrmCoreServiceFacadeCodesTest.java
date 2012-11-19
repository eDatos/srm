package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts.assertEqualsCodelistMetamacDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeCodesTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    // IMPORTANT: Metadata transformation is tested in Do2Dto tests

    // ---------------------------------------------------------------------------------------
    // CODELISTS
    // ---------------------------------------------------------------------------------------

    @Test
    public void testRetrieveCodelistByUrn() throws Exception {
        // Retrieve
        CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V1);

        // Validate
        assertEquals(CODELIST_1_V1, codelistMetamacDto.getUrn());
    }

    @Test
    public void testCreateCodelist() throws Exception {
        // Create
        CodelistMetamacDto codelistDto = CodesMetamacDtoMocks.mockCodelistDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        CodelistMetamacDto codelistMetamacCreated = srmCoreServiceFacade.createCodelist(getServiceContextAdministrador(), codelistDto);

        // Validate some metadata
        assertEquals(codelistDto.getCode(), codelistMetamacCreated.getCode());
        assertNotNull(codelistMetamacCreated.getUrn());
        assertEquals(ProcStatusEnum.DRAFT, codelistMetamacCreated.getLifeCycle().getProcStatus());
        assertEquals(Long.valueOf(0), codelistMetamacCreated.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateCodelist() throws Exception {
        // Update
        CodelistMetamacDto codelistMetamacDto = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        codelistMetamacDto.setName(MetamacMocks.mockInternationalStringDto());
        CodelistMetamacDto codelistMetamacDtoUpdated = srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDto);

        // Validate
        assertNotNull(codelistMetamacDto);
        assertEqualsCodelistMetamacDto(codelistMetamacDto, codelistMetamacDtoUpdated);
        assertTrue(codelistMetamacDtoUpdated.getVersionOptimisticLocking() > codelistMetamacDto.getVersionOptimisticLocking());
    }

    @Test
    public void testUpdateCodelistErrorOptimisticLocking() throws Exception {
        String urn = CODELIST_1_V2;

        CodelistMetamacDto codelistMetamacDtoSession1 = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), codelistMetamacDtoSession1.getVersionOptimisticLocking());
        codelistMetamacDtoSession1.setIsPartial(Boolean.TRUE);

        CodelistMetamacDto codelistMetamacDtoSession2 = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
        assertEquals(Long.valueOf(1), codelistMetamacDtoSession2.getVersionOptimisticLocking());
        codelistMetamacDtoSession2.setIsPartial(Boolean.TRUE);

        // Update by session 1
        CodelistMetamacDto codelistMetamacDtoSession1AfterUpdate1 = srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDtoSession1);
        assertTrue(codelistMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking() > codelistMetamacDtoSession1.getVersionOptimisticLocking());

        // Fails when is updated by session 2
        try {
            srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDtoSession2);
            fail("Optimistic locking");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.OPTIMISTIC_LOCKING.getCode(), e.getExceptionItems().get(0).getCode());
            assertNull(e.getExceptionItems().get(0).getMessageParameters());
        }

        // Session 1 can modify because has last version
        codelistMetamacDtoSession1AfterUpdate1.setIsPartial(Boolean.FALSE);
        CodelistMetamacDto codelistMetamacDtoSession1AfterUpdate2 = srmCoreServiceFacade.updateCodelist(getServiceContextAdministrador(), codelistMetamacDtoSession1AfterUpdate1);
        assertTrue(codelistMetamacDtoSession1AfterUpdate2.getVersionOptimisticLocking() > codelistMetamacDtoSession1AfterUpdate1.getVersionOptimisticLocking());
    }

    @Test
    public void testDeleteCodelist() throws Exception {
        String urn = CODELIST_2_V1;

        // Delete category scheme only with version in draft
        srmCoreServiceFacade.deleteCodelist(getServiceContextAdministrador(), urn);

        // Validation
        try {
            srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), urn);
            fail("Codelist deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    // ---------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }

}
