package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.criteria.CodelistVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeCodesSecurityTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    @Test
    public void testErrorPrincipalNotFound() throws Exception {
        try {
            ServiceContext ctx = getServiceContextAdministrador();
            ctx.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, null);
            srmCoreServiceFacade.retrieveCodelistByUrn(ctx, CODELIST_1_V1);
            fail("principal required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveCodelistByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCodelistByUrn(ctx, CODELIST_1_V1);
        }
    }

    @Test
    public void testRetrieveCodelistByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCodelistByUrn(ctx, CODELIST_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRetrieveCodelistVersions() throws Exception {
        srmCoreServiceFacade.retrieveCodelistVersions(getServiceContextTecnicoApoyoProduccion(), CODELIST_1_V1);

    }

    @Test
    public void testRetrieveCodelistVersionsError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCodelistVersions(ctx, CODELIST_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCreateCodelist() throws Exception {
        CodelistMetamacDto codelistDto = CodesMetamacDtoMocks.mockCodelistDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        codelistDto.setVariable(new RelatedResourceDto("VARIABLE_01", VARIABLE_1, null));
        srmCoreServiceFacade.createCodelist(getServiceContextJefeNormalizacion(), codelistDto);
    }

    @Test
    public void testCreateCodelistError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.createCodelist(ctx, CodesMetamacDtoMocks.mockCodelistDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1));
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testUpdateCodelistJefeNormalizacion() throws Exception {
        CodelistMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        CodelistMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_5_V1);
        CodelistMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_6_V1);

        CodelistMetamacDto[] codelistMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

        for (CodelistMetamacDto codelistMetamacDto : codelistMetamacDtos) {
            srmCoreServiceFacade.updateCodelist(getServiceContextJefeNormalizacion(), codelistMetamacDto);
        }
    }

    @Test
    public void testUpdateCodelistError() throws Exception {
        CodelistMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        CodelistMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_5_V1);
        CodelistMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_6_V1);
        CodelistMetamacDto internallyPublishedSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_7_V2);
        {
            // TECNICO_PRODUCCION
            CodelistMetamacDto[] codelistMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextTecnicoProduccion();

            for (CodelistMetamacDto codelistMetamacDto : codelistMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateCodelist(ctx, codelistMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // JEFE_PRODUCCION
            CodelistMetamacDto[] codelistMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextJefeProduccion();

            for (CodelistMetamacDto codelistMetamacDto : codelistMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateCodelist(ctx, codelistMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // NOT_EXISTS
            CodelistMetamacDto[] codelistMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            for (CodelistMetamacDto codelistMetamacDto : codelistMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateCodelist(getServiceContextWithoutSrmRole(), codelistMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // WRONG PROC STATUS
            try {
                srmCoreServiceFacade.updateCodelist(getServiceContextWithoutSrmRole(), internallyPublishedSchemeVersion);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testDeleteCodelistJefeNormalizacion() throws Exception {
        String[] urns = {CODELIST_2_V1};

        for (String urn : urns) {
            srmCoreServiceFacade.deleteCodelist(getServiceContextJefeNormalizacion(), urn);
        }
    }

    @Test
    public void testDeleteCodelistError() {
        String codelistUrn = CODELIST_1_V2;
        {
            ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : ctxs) {
                try {
                    srmCoreServiceFacade.deleteCodelist(ctx, codelistUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            try {
                srmCoreServiceFacade.deleteCodelist(getServiceContextJefeProduccion(), codelistUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testFindCodelistsByCondition() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.findCodelistsByCondition(ctx, getMetamacCriteria());
        }
    }

    @Test
    public void testFindCodelistsByConditionError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.findCodelistsByCondition(ctx, getMetamacCriteria());
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testSendCodelistToProductionValidationTecnicoApoyoNormalizacion() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        srmCoreServiceFacade.sendCodelistToProductionValidation(getServiceContextTecnicoApoyoNormalizacion(), codelistUrn);

    }

    @Test
    public void testSendCodelistToProductionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String codelistUrn = CODELIST_1_V2;
        srmCoreServiceFacade.sendCodelistToProductionValidation(ctx, codelistUrn);

    }

    @Test
    public void testSendCodelistToProductionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String codelistUrn = CODELIST_1_V2;
        srmCoreServiceFacade.sendCodelistToProductionValidation(ctx, codelistUrn);
    }

    @Test
    public void testSendCodelistToProductionValidationError() throws Exception {
        String codelistUrn = CODELIST_1_V2;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(),
                getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.sendCodelistToProductionValidation(ctx, codelistUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testSendCodelistToDiffusionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String codelistUrn = CODELIST_5_V1;
        srmCoreServiceFacade.sendCodelistToDiffusionValidation(ctx, codelistUrn);
    }

    @Test
    public void testSendCodelistToDiffusionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String codelistUrn = CODELIST_5_V1;
        srmCoreServiceFacade.sendCodelistToDiffusionValidation(ctx, codelistUrn);
    }

    @Test
    public void testSendCodelistToDiffusionValidationError() throws Exception {
        String codelistUrn = CODELIST_5_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextTecnicoApoyoNormalizacion(),
                getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.sendCodelistToDiffusionValidation(ctx, codelistUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRejectCodelistToProductionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String codelistUrn = CODELIST_5_V1;
        srmCoreServiceFacade.rejectCodelistProductionValidation(ctx, codelistUrn);
    }

    @Test
    public void testRejectCodelistToProductionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String codelistUrn = CODELIST_5_V1;
        srmCoreServiceFacade.rejectCodelistProductionValidation(ctx, codelistUrn);
    }

    @Test
    public void testRejectCodelistToProductionValidationError() throws Exception {
        String codelistUrn = CODELIST_5_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextTecnicoApoyoNormalizacion(),
                getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.rejectCodelistProductionValidation(ctx, codelistUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRejectCodelistDiffusionValidation() throws Exception {
        srmCoreServiceFacade.rejectCodelistDiffusionValidation(getServiceContextJefeNormalizacion(), CODELIST_6_V1);
    }

    @Test
    public void testRejectCodelistDiffusionValidationError() throws Exception {
        {
            // Wrong role
            ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                    getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.rejectCodelistDiffusionValidation(ctx, CODELIST_6_V1);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        // Wrong procStatus
        {
            try {
                srmCoreServiceFacade.rejectCodelistDiffusionValidation(getServiceContextJefeNormalizacion(), CODELIST_7_V2);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testPublishInternallyCodelist() throws Exception {
        srmCoreServiceFacade.publishCodelistInternally(getServiceContextJefeNormalizacion(), CODELIST_6_V1);
    }

    @Test
    public void testPublishInternallyCodelistError() throws Exception {
        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.publishCodelistInternally(ctx, CODELIST_6_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testPublishExternallyCodelistJefeNormalizacion() throws Exception {
        String codelistUrn = CODELIST_7_V2;
        srmCoreServiceFacade.publishCodelistExternally(getServiceContextJefeNormalizacion(), codelistUrn);
    }

    @Test
    public void testPublishExternallyCodelistJefeProduccion() throws Exception {
        String codelistUrn = CODELIST_7_V2;
        srmCoreServiceFacade.publishCodelistExternally(getServiceContextJefeNormalizacion(), codelistUrn);
    }

    @Test
    public void testPublishExternallyCodelistError() throws Exception {
        String codelistUrn = CODELIST_3_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.publishCodelistExternally(ctx, codelistUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testVersioningCodelist() throws Exception {
        srmCoreServiceFacade.versioningCodelist(getServiceContextJefeNormalizacion(), CODELIST_7_V1, null, VersionTypeEnum.MAJOR);
    }

    @Test
    public void testVersioningCodelistError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.versioningCodelist(ctx, CODELIST_7_V1, null, VersionTypeEnum.MAJOR);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testEndCodelistValidityJefeNormalizacion() throws Exception {
        String codelistUrn = CODELIST_7_V1;
        srmCoreServiceFacade.endCodelistValidity(getServiceContextJefeNormalizacion(), codelistUrn);
    }

    @Test
    public void testEndCodelistValidityJefeProduccion() throws Exception {
        String codelistUrn = CODELIST_7_V1;
        srmCoreServiceFacade.endCodelistValidity(getServiceContextJefeNormalizacion(), codelistUrn);
    }

    @Test
    public void testEndCodelistValidityError() throws Exception {
        String codelistUrn = CODELIST_1_V2;
        {
            ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : ctxs) {
                try {
                    srmCoreServiceFacade.endCodelistValidity(ctx, codelistUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            try {
                srmCoreServiceFacade.endCodelistValidity(getServiceContextJefeProduccion(), codelistUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testUpdateCodeExhaustiveWithAccessAndWithoutAccess() throws Exception {
        // 1) CODELIST DRAFT
        {
            CodeMetamacDto draftCodeMetamacDto = srmCoreServiceFacade.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_1_V2_CODE_1);

            // Access
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    draftCodeMetamacDto = srmCoreServiceFacade.updateCode(ctx, draftCodeMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(),
                        getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateCode(ctx, draftCodeMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }

        // 2) CODELIST PRODUCTION_VALIDATION
        {
            CodeMetamacDto prodValidationCodeMetamacDto = srmCoreServiceFacade.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_5_V1_CODE_1);
            prodValidationCodeMetamacDto.setName(MetamacMocks.mockInternationalStringDto());

            // Access
            {
                ServiceContext[] contexts = {getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    prodValidationCodeMetamacDto = srmCoreServiceFacade.updateCode(ctx, prodValidationCodeMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoProduccion(),
                        getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateCode(ctx, prodValidationCodeMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }

        // 3) CODELIST DIFFUSION_VALIDATION
        {
            CodeMetamacDto difValidationCodeMetamacDto = srmCoreServiceFacade.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_6_V1_CODE_1);
            difValidationCodeMetamacDto.setName(MetamacMocks.mockInternationalStringDto());

            // Access
            {
                ServiceContext[] contexts = {getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    difValidationCodeMetamacDto = srmCoreServiceFacade.updateCode(ctx, difValidationCodeMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoProduccion(),
                        getServiceContextJefeProduccion(), getServiceContextTecnicoNormalizacion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                        getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateCode(ctx, difValidationCodeMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }

        // 4) CODELIST INTERNALLY_PUBLISHED
        {
            CodeMetamacDto internallyPublishedCodeMetamacDto = srmCoreServiceFacade.retrieveCodeByUrn(getServiceContextAdministrador(), CODELIST_7_V2_CODE_1);

            // Error (wrong procStatus)
            try {
                srmCoreServiceFacade.updateCode(getServiceContextJefeNormalizacion(), internallyPublishedCodeMetamacDto);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCreateCode() throws Exception {

        // JefeNormalizacion
        {
            CodelistMetamacDto draftCodelistVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
            CodelistMetamacDto[] codelistMetamacDtos = {draftCodelistVersion};

            CodeMetamacDto codeMetamacDto = CodesMetamacDtoMocks.mockCodeDto();
            for (CodelistMetamacDto codelistMetamacDto : codelistMetamacDtos) {
                codeMetamacDto.setItemSchemeVersionUrn(codelistMetamacDto.getUrn());
                srmCoreServiceFacade.createCode(getServiceContextJefeNormalizacion(), codeMetamacDto);
            }
        }
        // Note: no more tests because security is same that update code
    }

    @Test
    public void testCreateCodeError() throws Exception {
        CodelistMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_1_V2);
        CodelistMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_5_V1);
        CodelistMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_6_V1);

        CodeMetamacDto codeMetamacDto = CodesMetamacDtoMocks.mockCodeDto();

        {
            // TECNICO_PRODUCCION
            CodelistMetamacDto[] codelistMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextTecnicoProduccion();

            for (CodelistMetamacDto codelistMetamacDto : codelistMetamacDtos) {
                try {
                    codeMetamacDto.setItemSchemeVersionUrn(codelistMetamacDto.getUrn());
                    srmCoreServiceFacade.createCode(ctx, codeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // JEFE_PRODUCCION
            CodelistMetamacDto[] codelistMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextJefeProduccion();

            for (CodelistMetamacDto codelistMetamacDto : codelistMetamacDtos) {
                try {
                    codeMetamacDto.setItemSchemeVersionUrn(codelistMetamacDto.getUrn());
                    srmCoreServiceFacade.createCode(ctx, codeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // NOT_EXISTS
            CodelistMetamacDto[] codelistMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            for (CodelistMetamacDto codelistMetamacDto : codelistMetamacDtos) {
                try {
                    codeMetamacDto.setItemSchemeVersionUrn(codelistMetamacDto.getUrn());
                    srmCoreServiceFacade.createCode(getServiceContextWithoutSrmRole(), codeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testRetrieveCodeByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCodeByUrn(ctx, CODELIST_1_V1_CODE_1);
        }
    }

    @Test
    public void testRetrieveCodeByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCodeByUrn(ctx, CODELIST_1_V1_CODE_1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testDeleteCodeJefeNormalizacion() throws Exception {
        srmCoreServiceFacade.deleteCode(getServiceContextJefeNormalizacion(), CODELIST_1_V2_CODE_4_1_1);
        // Note: no more tests because security is same that update code
    }

    @Test
    public void testDeleteCodeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextTecnicoProduccion()};
        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.deleteCode(ctx, CODELIST_1_V2_CODE_1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
        // Note: no more tests because security is same that update code
    }

    @Test
    public void testRetrieveCodesByCodelistUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCodesByCodelistUrn(ctx, CODELIST_1_V1, null);
        }
    }

    @Test
    public void testRetrieveCodesByCodelistUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCodesByCodelistUrn(ctx, CODELIST_1_V1, null);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testFindCodesByCondition() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.findCodesByCondition(ctx, getMetamacCriteria());
        }
    }

    @Test
    public void testFindCodesByConditionError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.findCodesByCondition(ctx, getMetamacCriteria());
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRetrieveCodelistFamilyByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCodelistFamilyByUrn(ctx, CODELIST_FAMILY_1);
        }
    }

    @Test
    public void testRetrieveCodelistFamilyByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCodelistByUrn(ctx, CODELIST_FAMILY_1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testFindCodelistFamiliesByCondition() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.findCodelistFamiliesByCondition(ctx, getMetamacCriteria());
        }
    }

    @Test
    public void testFindCodelistFamiliesByConditionError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.findCodelistFamiliesByCondition(ctx, getMetamacCriteria());
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    private MetamacCriteria getMetamacCriteria() {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(CodelistVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        return metamacCriteria;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }

}
