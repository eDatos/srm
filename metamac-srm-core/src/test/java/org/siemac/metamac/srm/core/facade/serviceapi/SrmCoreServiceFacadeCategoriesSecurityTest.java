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
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.criteria.CategorySchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.VersionTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
public class SrmCoreServiceFacadeCategoriesSecurityTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    @Test
    public void testErrorPrincipalNotFound() throws Exception {
        try {
            ServiceContext ctx = getServiceContextAdministrador();
            ctx.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, null);
            srmCoreServiceFacade.retrieveCategorySchemeByUrn(ctx, CATEGORY_SCHEME_1_V1);
            fail("principal required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveCategorySchemeByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCategorySchemeByUrn(ctx, CATEGORY_SCHEME_1_V1);
        }
    }

    @Test
    public void testRetrieveCategorySchemeByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCategorySchemeByUrn(ctx, CATEGORY_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRetrieveCategorySchemeVersions() throws Exception {
        srmCoreServiceFacade.retrieveCategorySchemeVersions(getServiceContextTecnicoApoyoProduccion(), CATEGORY_SCHEME_1_V1);

    }

    @Test
    public void testRetrieveCategorySchemeVersionsError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCategorySchemeVersions(ctx, CATEGORY_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCreateCategoryScheme() throws Exception {
        srmCoreServiceFacade.createCategoryScheme(getServiceContextJefeNormalizacion(), CategoriesMetamacDtoMocks.mockCategorySchemeDto(AGENCY_ROOT_CODE, AGENCY_ROOT_1_V1));
    }

    @Test
    public void testCreateCategorySchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.createCategoryScheme(ctx, CategoriesMetamacDtoMocks.mockCategorySchemeDto(AGENCY_ROOT_CODE, AGENCY_ROOT_1_V1));
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testUpdateCategorySchemeJefeNormalizacion() throws Exception {
        CategorySchemeMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        CategorySchemeMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_5_V1);
        CategorySchemeMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_6_V1);

        CategorySchemeMetamacDto[] categorySchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

        for (CategorySchemeMetamacDto categorySchemeMetamacDto : categorySchemeMetamacDtos) {
            srmCoreServiceFacade.updateCategoryScheme(getServiceContextJefeNormalizacion(), categorySchemeMetamacDto);
        }
    }

    @Test
    public void testUpdateCategorySchemeError() throws Exception {
        CategorySchemeMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        CategorySchemeMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_5_V1);
        CategorySchemeMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_6_V1);
        CategorySchemeMetamacDto internallyPublishedSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_7_V2);
        {
            // TECNICO_PRODUCCION
            CategorySchemeMetamacDto[] categorySchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextTecnicoProduccion();

            for (CategorySchemeMetamacDto categorySchemeMetamacDto : categorySchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateCategoryScheme(ctx, categorySchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // JEFE_PRODUCCION
            CategorySchemeMetamacDto[] categorySchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextJefeProduccion();

            for (CategorySchemeMetamacDto categorySchemeMetamacDto : categorySchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateCategoryScheme(ctx, categorySchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // NOT_EXISTS
            CategorySchemeMetamacDto[] categorySchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            for (CategorySchemeMetamacDto categorySchemeMetamacDto : categorySchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateCategoryScheme(getServiceContextWithoutSrmRole(), categorySchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // WRONG PROC STATUS
            try {
                srmCoreServiceFacade.updateCategoryScheme(getServiceContextWithoutSrmRole(), internallyPublishedSchemeVersion);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testDeleteCategorySchemeJefeNormalizacion() throws Exception {
        String[] urns = {CATEGORY_SCHEME_2_V1};

        for (String urn : urns) {
            srmCoreServiceFacade.deleteCategoryScheme(getServiceContextJefeNormalizacion(), urn);
        }
    }

    @Test
    public void testDeleteCategorySchemeError() {
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        {
            ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : ctxs) {
                try {
                    srmCoreServiceFacade.deleteCategoryScheme(ctx, categorySchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            try {
                srmCoreServiceFacade.deleteCategoryScheme(getServiceContextJefeProduccion(), categorySchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testFindCategorySchemesByCondition() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.findCategorySchemesByCondition(ctx, getMetamacCriteria());
        }
    }

    @Test
    public void testFindCategorySchemesByConditionError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.findCategorySchemesByCondition(ctx, getMetamacCriteria());
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationTecnicoApoyoNormalizacion() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        srmCoreServiceFacade.sendCategorySchemeToProductionValidation(getServiceContextTecnicoApoyoNormalizacion(), categorySchemeUrn);

    }

    @Test
    public void testSendCategorySchemeToProductionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        srmCoreServiceFacade.sendCategorySchemeToProductionValidation(ctx, categorySchemeUrn);

    }

    @Test
    public void testSendCategorySchemeToProductionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        srmCoreServiceFacade.sendCategorySchemeToProductionValidation(ctx, categorySchemeUrn);
    }

    @Test
    public void testSendCategorySchemeToProductionValidationError() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(),
                getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.sendCategorySchemeToProductionValidation(ctx, categorySchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String categorySchemeUrn = CATEGORY_SCHEME_5_V1;
        srmCoreServiceFacade.sendCategorySchemeToDiffusionValidation(ctx, categorySchemeUrn);
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String categorySchemeUrn = CATEGORY_SCHEME_5_V1;
        srmCoreServiceFacade.sendCategorySchemeToDiffusionValidation(ctx, categorySchemeUrn);
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidationError() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_5_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextTecnicoApoyoNormalizacion(),
                getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.sendCategorySchemeToDiffusionValidation(ctx, categorySchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRejectCategorySchemeToProductionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String categorySchemeUrn = CATEGORY_SCHEME_5_V1;
        srmCoreServiceFacade.rejectCategorySchemeProductionValidation(ctx, categorySchemeUrn);
    }

    @Test
    public void testRejectCategorySchemeToProductionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String categorySchemeUrn = CATEGORY_SCHEME_5_V1;
        srmCoreServiceFacade.rejectCategorySchemeProductionValidation(ctx, categorySchemeUrn);
    }

    @Test
    public void testRejectCategorySchemeToProductionValidationError() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_5_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextTecnicoApoyoNormalizacion(),
                getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.rejectCategorySchemeProductionValidation(ctx, categorySchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRejectCategorySchemeDiffusionValidation() throws Exception {
        srmCoreServiceFacade.rejectCategorySchemeDiffusionValidation(getServiceContextJefeNormalizacion(), CATEGORY_SCHEME_6_V1);
    }

    @Test
    public void testRejectCategorySchemeDiffusionValidationError() throws Exception {
        {
            // Wrong role
            ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                    getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.rejectCategorySchemeDiffusionValidation(ctx, CATEGORY_SCHEME_6_V1);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        // Wrong procStatus
        {
            try {
                srmCoreServiceFacade.rejectCategorySchemeDiffusionValidation(getServiceContextJefeNormalizacion(), CATEGORY_SCHEME_7_V2);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testPublishInternallyCategoryScheme() throws Exception {
        srmCoreServiceFacade.publishCategorySchemeInternally(getServiceContextJefeNormalizacion(), CATEGORY_SCHEME_6_V1);
    }

    @Test
    public void testPublishInternallyCategorySchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.publishCategorySchemeInternally(ctx, CATEGORY_SCHEME_6_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testPublishExternallyCategorySchemeJefeNormalizacion() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_7_V2;
        srmCoreServiceFacade.publishCategorySchemeExternally(getServiceContextJefeNormalizacion(), categorySchemeUrn);
    }

    @Test
    public void testPublishExternallyCategorySchemeJefeProduccion() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_3_V1;
        srmCoreServiceFacade.publishCategorySchemeExternally(getServiceContextJefeNormalizacion(), categorySchemeUrn);
    }

    @Test
    public void testPublishExternallyCategorySchemeError() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_3_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.publishCategorySchemeExternally(ctx, categorySchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testVersioningCategoryScheme() throws Exception {
        srmCoreServiceFacade.versioningCategoryScheme(getServiceContextJefeNormalizacion(), CATEGORY_SCHEME_7_V1, VersionTypeEnum.MAJOR);
    }

    @Test
    public void testVersioningCategorySchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.versioningCategoryScheme(ctx, CATEGORY_SCHEME_7_V1, VersionTypeEnum.MAJOR);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testEndCategorySchemeValidityJefeNormalizacion() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_7_V1;
        srmCoreServiceFacade.endCategorySchemeValidity(getServiceContextJefeNormalizacion(), categorySchemeUrn);
    }

    @Test
    public void testEndCategorySchemeValidityJefeProduccion() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_7_V1;
        srmCoreServiceFacade.endCategorySchemeValidity(getServiceContextJefeNormalizacion(), categorySchemeUrn);
    }

    @Test
    public void testEndCategorySchemeValidityError() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        {
            ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : ctxs) {
                try {
                    srmCoreServiceFacade.endCategorySchemeValidity(ctx, categorySchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            try {
                srmCoreServiceFacade.endCategorySchemeValidity(getServiceContextJefeProduccion(), categorySchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testUpdateCategoryExhaustiveWithAccessAndWithoutAccess() throws Exception {
        // 1) CATEGORY SCHEME DRAFT
        {
            CategoryMetamacDto draftCategoryMetamacDto = srmCoreServiceFacade.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);

            // Access
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    draftCategoryMetamacDto = srmCoreServiceFacade.updateCategory(ctx, draftCategoryMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(),
                        getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateCategory(ctx, draftCategoryMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }

        // 2) CATEGORY SCHEME PRODUCTION_VALIDATION
        {
            CategoryMetamacDto prodValidationCategoryMetamacDto = srmCoreServiceFacade.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_5_V1_CATEGORY_1);
            prodValidationCategoryMetamacDto.setName(MetamacMocks.mockInternationalString());

            // Access
            {
                ServiceContext[] contexts = {getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    prodValidationCategoryMetamacDto = srmCoreServiceFacade.updateCategory(ctx, prodValidationCategoryMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoProduccion(),
                        getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateCategory(ctx, prodValidationCategoryMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }

        // 3) CATEGORY SCHEME DIFFUSION_VALIDATION
        {
            CategoryMetamacDto difValidationCategoryMetamacDto = srmCoreServiceFacade.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_6_V1_CATEGORY_1);
            difValidationCategoryMetamacDto.setName(MetamacMocks.mockInternationalString());

            // Access
            {
                ServiceContext[] contexts = {getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    difValidationCategoryMetamacDto = srmCoreServiceFacade.updateCategory(ctx, difValidationCategoryMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoProduccion(),
                        getServiceContextJefeProduccion(), getServiceContextTecnicoNormalizacion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                        getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateCategory(ctx, difValidationCategoryMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }

        // 4) CATEGORY SCHEME INTERNALLY_PUBLISHED
        {
            CategoryMetamacDto internallyPublishedCategoryMetamacDto = srmCoreServiceFacade.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_7_V2_CATEGORY_1);

            // Error (wrong procStatus)
            try {
                srmCoreServiceFacade.updateCategory(getServiceContextJefeNormalizacion(), internallyPublishedCategoryMetamacDto);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCreateCategory() throws Exception {

        // JefeNormalizacion
        {
            CategorySchemeMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
            CategorySchemeMetamacDto[] categorySchemeMetamacDtos = {draftSchemeVersion};

            CategoryMetamacDto categoryMetamacDto = CategoriesMetamacDtoMocks.mockCategoryDto();
            for (CategorySchemeMetamacDto categorySchemeMetamacDto : categorySchemeMetamacDtos) {
                categoryMetamacDto.setItemSchemeVersionUrn(categorySchemeMetamacDto.getUrn());
                srmCoreServiceFacade.createCategory(getServiceContextJefeNormalizacion(), categoryMetamacDto);
            }
        }
        // Note: no more tests because security is same that update category
    }

    @Test
    public void testCreateCategoryError() throws Exception {
        CategorySchemeMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        CategorySchemeMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_5_V1);
        CategorySchemeMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_6_V1);

        CategoryMetamacDto categoryMetamacDto = CategoriesMetamacDtoMocks.mockCategoryDto();

        {
            // TECNICO_PRODUCCION
            CategorySchemeMetamacDto[] categorySchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextTecnicoProduccion();

            for (CategorySchemeMetamacDto categorySchemeMetamacDto : categorySchemeMetamacDtos) {
                try {
                    categoryMetamacDto.setItemSchemeVersionUrn(categorySchemeMetamacDto.getUrn());
                    srmCoreServiceFacade.createCategory(ctx, categoryMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // JEFE_PRODUCCION
            CategorySchemeMetamacDto[] categorySchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextJefeProduccion();

            for (CategorySchemeMetamacDto categorySchemeMetamacDto : categorySchemeMetamacDtos) {
                try {
                    categoryMetamacDto.setItemSchemeVersionUrn(categorySchemeMetamacDto.getUrn());
                    srmCoreServiceFacade.createCategory(ctx, categoryMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // NOT_EXISTS
            CategorySchemeMetamacDto[] categorySchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            for (CategorySchemeMetamacDto categorySchemeMetamacDto : categorySchemeMetamacDtos) {
                try {
                    categoryMetamacDto.setItemSchemeVersionUrn(categorySchemeMetamacDto.getUrn());
                    srmCoreServiceFacade.createCategory(getServiceContextWithoutSrmRole(), categoryMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testRetrieveCategoryByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCategoryByUrn(ctx, CATEGORY_SCHEME_1_V1_CATEGORY_1);
        }
    }

    @Test
    public void testRetrieveCategoryByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCategoryByUrn(ctx, CATEGORY_SCHEME_1_V1_CATEGORY_1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testDeleteCategoryJefeNormalizacion() throws Exception {
        srmCoreServiceFacade.deleteCategory(getServiceContextJefeNormalizacion(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
        // Note: no more tests because security is same that update category
    }

    @Test
    public void testDeleteCategoryError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextTecnicoProduccion()};
        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.deleteCategory(ctx, CATEGORY_SCHEME_1_V2_CATEGORY_1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
        // Note: no more tests because security is same that update category
    }

    @Test
    public void testRetrieveCategoriesByCategorySchemeUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCategoriesByCategorySchemeUrn(ctx, CATEGORY_SCHEME_1_V1);
        }
    }

    @Test
    public void testRetrieveCategoriesByCategorySchemeUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCategoriesByCategorySchemeUrn(ctx, CATEGORY_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testFindCategoriesByCondition() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.findCategoriesByCondition(ctx, getMetamacCriteria());
        }
    }

    @Test
    public void testFindCategoriesByConditionError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.findCategoriesByCondition(ctx, getMetamacCriteria());
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    private MetamacCriteria getMetamacCriteria() {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(CategorySchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        return metamacCriteria;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }

}
