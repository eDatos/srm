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
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.criteria.OrganisationSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationMetamacDto;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.core.organisation.serviceapi.utils.OrganisationsMetamacDtoMocks;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeOrganisationsSecurityTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    @Test
    public void testErrorPrincipalNotFound() throws Exception {
        try {
            ServiceContext ctx = getServiceContextAdministrador();
            ctx.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, null);
            srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(ctx, ORGANISATION_SCHEME_1_V1);
            fail("principal required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveOrganisationSchemeByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(ctx, ORGANISATION_SCHEME_1_V1);
        }
    }

    @Test
    public void testRetrieveOrganisationSchemeByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(ctx, ORGANISATION_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRetrieveOrganisationSchemeVersions() throws Exception {
        srmCoreServiceFacade.retrieveOrganisationSchemeVersions(getServiceContextTecnicoApoyoProduccion(), ORGANISATION_SCHEME_1_V1);

    }

    @Test
    public void testRetrieveOrganisationSchemeVersionsError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveOrganisationSchemeVersions(ctx, ORGANISATION_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCreateOrganisationScheme() throws Exception {
        srmCoreServiceFacade.createOrganisationScheme(getServiceContextJefeNormalizacion(), OrganisationsMetamacDtoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME));
    }

    @Test
    public void testCreateOrganisationSchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.createOrganisationScheme(ctx, OrganisationsMetamacDtoMocks.mockOrganisationScheme(OrganisationSchemeTypeEnum.AGENCY_SCHEME));
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testUpdateOrganisationSchemeJefeNormalizacion() throws Exception {
        OrganisationSchemeMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2);
        OrganisationSchemeMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_5_V1);
        OrganisationSchemeMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_6_V1);

        OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

        for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
            srmCoreServiceFacade.updateOrganisationScheme(getServiceContextJefeNormalizacion(), organisationSchemeMetamacDto);
        }
    }

    @Test
    public void testUpdatePublishedAgencyScheme() throws Exception {
        OrganisationSchemeMetamacDto externallyPublishedVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_8_V1);
        OrganisationSchemeMetamacDto internallyPublishedVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_9_V1);
        srmCoreServiceFacade.updateOrganisationScheme(getServiceContextJefeNormalizacion(), externallyPublishedVersion);
        srmCoreServiceFacade.updateOrganisationScheme(getServiceContextJefeNormalizacion(), internallyPublishedVersion);
    }

    @Test
    public void testUpdateOrganisationSchemeError() throws Exception {
        OrganisationSchemeMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2);
        OrganisationSchemeMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_5_V1);
        OrganisationSchemeMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_6_V1);
        OrganisationSchemeMetamacDto internallyPublishedSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_8_V1);
        {
            // TECNICO_PRODUCCION
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextTecnicoProduccion();

            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateOrganisationScheme(ctx, organisationSchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // JEFE_PRODUCCION
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextJefeProduccion();

            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateOrganisationScheme(ctx, organisationSchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // NOT_EXISTS
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateOrganisationScheme(getServiceContextWithoutSrmRole(), organisationSchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // PUBLISHED
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {internallyPublishedSchemeVersion};

            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.updateOrganisationScheme(getServiceContextWithoutSrmRole(), organisationSchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testDeleteOrganisationSchemeJefeNormalizacion() throws Exception {
        String[] urns = {ORGANISATION_SCHEME_2_V1};

        for (String urn : urns) {
            srmCoreServiceFacade.deleteOrganisationScheme(getServiceContextJefeNormalizacion(), urn);
        }
    }

    @Test
    public void testDeleteOrganisationSchemeError() {
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
        {
            ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : ctxs) {
                try {
                    srmCoreServiceFacade.deleteOrganisationScheme(ctx, organisationSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            try {
                srmCoreServiceFacade.deleteOrganisationScheme(getServiceContextJefeProduccion(), organisationSchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testFindOrganisationSchemesByCondition() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.findOrganisationSchemesByCondition(ctx, getMetamacCriteria());
        }
    }

    @Test
    public void testFindOrganisationSchemesByConditionError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.findOrganisationSchemesByCondition(ctx, getMetamacCriteria());
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testSendOrganisationSchemeToProductionValidationTecnicoApoyoNormalizacion() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
        srmCoreServiceFacade.sendOrganisationSchemeToProductionValidation(getServiceContextTecnicoApoyoNormalizacion(), organisationSchemeUrn);

    }

    @Test
    public void testSendOrganisationSchemeToProductionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
        srmCoreServiceFacade.sendOrganisationSchemeToProductionValidation(ctx, organisationSchemeUrn);

    }

    @Test
    public void testSendOrganisationSchemeToProductionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
        srmCoreServiceFacade.sendOrganisationSchemeToProductionValidation(ctx, organisationSchemeUrn);
    }

    @Test
    public void testSendOrganisationSchemeToProductionValidationError() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(),
                getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.sendOrganisationSchemeToProductionValidation(ctx, organisationSchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testSendOrganisationSchemeToDiffusionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String organisationSchemeUrn = ORGANISATION_SCHEME_5_V1;
        srmCoreServiceFacade.sendOrganisationSchemeToDiffusionValidation(ctx, organisationSchemeUrn);
    }

    @Test
    public void testSendOrganisationSchemeToDiffusionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String organisationSchemeUrn = ORGANISATION_SCHEME_5_V1;
        srmCoreServiceFacade.sendOrganisationSchemeToDiffusionValidation(ctx, organisationSchemeUrn);
    }

    @Test
    public void testSendOrganisationSchemeToDiffusionValidationError() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_5_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextTecnicoApoyoNormalizacion(),
                getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.sendOrganisationSchemeToDiffusionValidation(ctx, organisationSchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRejectOrganisationSchemeToProductionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        String organisationSchemeUrn = ORGANISATION_SCHEME_5_V1;
        srmCoreServiceFacade.rejectOrganisationSchemeProductionValidation(ctx, organisationSchemeUrn);
    }

    @Test
    public void testRejectOrganisationSchemeToProductionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        String organisationSchemeUrn = ORGANISATION_SCHEME_5_V1;
        srmCoreServiceFacade.rejectOrganisationSchemeProductionValidation(ctx, organisationSchemeUrn);
    }

    @Test
    public void testRejectOrganisationSchemeToProductionValidationError() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_5_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextTecnicoApoyoNormalizacion(),
                getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.rejectOrganisationSchemeProductionValidation(ctx, organisationSchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRejectOrganisationSchemeDiffusionValidation() throws Exception {
        srmCoreServiceFacade.rejectOrganisationSchemeDiffusionValidation(getServiceContextJefeNormalizacion(), ORGANISATION_SCHEME_6_V1);
    }

    @Test
    public void testRejectOrganisationSchemeDiffusionValidationError() throws Exception {
        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.rejectOrganisationSchemeDiffusionValidation(ctx, ORGANISATION_SCHEME_6_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testPublishInternallyOrganisationScheme() throws Exception {
        srmCoreServiceFacade.publishOrganisationSchemeInternally(getServiceContextJefeNormalizacion(), ORGANISATION_SCHEME_6_V1);
    }

    @Test
    public void testPublishInternallyOrganisationSchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.publishOrganisationSchemeInternally(ctx, ORGANISATION_SCHEME_6_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testPublishExternallyOrganisationSchemeJefeNormalizacion() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_7_V2;
        srmCoreServiceFacade.publishOrganisationSchemeExternally(getServiceContextJefeNormalizacion(), organisationSchemeUrn);
    }

    @Test
    public void testPublishExternallyOrganisationSchemeJefeProduccion() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_3_V1;
        srmCoreServiceFacade.publishOrganisationSchemeExternally(getServiceContextJefeNormalizacion(), organisationSchemeUrn);
    }

    @Test
    public void testPublishExternallyOrganisationSchemeError() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_3_V1;

        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.publishOrganisationSchemeExternally(ctx, organisationSchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testVersioningOrganisationScheme() throws Exception {
        srmCoreServiceFacade.versioningOrganisationScheme(getServiceContextJefeNormalizacion(), ORGANISATION_SCHEME_7_V1, VersionTypeEnum.MAJOR);
    }

    @Test
    public void testVersioningOrganisationSchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.versioningOrganisationScheme(ctx, ORGANISATION_SCHEME_7_V1, VersionTypeEnum.MAJOR);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testEndOrganisationSchemeValidityJefeNormalizacion() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_7_V1;
        srmCoreServiceFacade.endOrganisationSchemeValidity(getServiceContextJefeNormalizacion(), organisationSchemeUrn);
    }

    @Test
    public void testEndOrganisationSchemeValidityJefeProduccion() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_7_V1;
        srmCoreServiceFacade.endOrganisationSchemeValidity(getServiceContextJefeNormalizacion(), organisationSchemeUrn);
    }

    @Test
    public void testEndOrganisationSchemeValidityError() throws Exception {
        String organisationSchemeUrn = ORGANISATION_SCHEME_1_V2;
        {
            ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : ctxs) {
                try {
                    srmCoreServiceFacade.endOrganisationSchemeValidity(ctx, organisationSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            try {
                srmCoreServiceFacade.endOrganisationSchemeValidity(getServiceContextJefeProduccion(), organisationSchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testUpdateOrganisationExhaustiveWithAccessAndWithoutAccess() throws Exception {
        // 1) ORGANISATION SCHEME DRAFT
        {
            OrganisationMetamacDto draftOrganisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2_ORGANISATION_1);

            // Access
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    draftOrganisationMetamacDto = srmCoreServiceFacade.updateOrganisation(ctx, draftOrganisationMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(),
                        getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateOrganisation(ctx, draftOrganisationMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }

        // 2) ORGANISATION SCHEME PRODUCTION_VALIDATION
        {
            OrganisationMetamacDto prodValidationOrganisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_5_V1_ORGANISATION_1);
            prodValidationOrganisationMetamacDto.setName(MetamacMocks.mockInternationalStringDto());

            // Access
            {
                ServiceContext[] contexts = {getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    prodValidationOrganisationMetamacDto = srmCoreServiceFacade.updateOrganisation(ctx, prodValidationOrganisationMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoProduccion(),
                        getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateOrganisation(ctx, prodValidationOrganisationMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }

        // 3) ORGANISATION SCHEME DIFFUSION_VALIDATION
        {
            OrganisationMetamacDto difValidationOrganisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_6_V1_ORGANISATION_1);
            difValidationOrganisationMetamacDto.setName(MetamacMocks.mockInternationalStringDto());

            // Access
            {
                ServiceContext[] contexts = {getServiceContextJefeNormalizacion()};
                for (ServiceContext ctx : contexts) {
                    difValidationOrganisationMetamacDto = srmCoreServiceFacade.updateOrganisation(ctx, difValidationOrganisationMetamacDto);
                }
            }

            // Error
            {
                ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoProduccion(),
                        getServiceContextJefeProduccion(), getServiceContextTecnicoNormalizacion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                        getServiceContextWithoutSrmRole()};
                for (ServiceContext ctx : contexts) {
                    try {
                        srmCoreServiceFacade.updateOrganisation(ctx, difValidationOrganisationMetamacDto);
                        fail("action not allowed");
                    } catch (MetamacException e) {
                        assertEquals(1, e.getExceptionItems().size());
                        assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                    }
                }
            }
        }
    }

    @Test
    public void testUpdateAgencyInPublishedScheme() throws Exception {
        {
            // INTERNALLY PUBLISHED SCHEME
            OrganisationMetamacDto organisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_9_V1_ORGANISATION_1);
            srmCoreServiceFacade.updateOrganisation(getServiceContextJefeNormalizacion(), organisationMetamacDto);
        }
        {
            // EXTERNALLY PUBLISHED SCHEME
            OrganisationMetamacDto organisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_8_V1_ORGANISATION_1);
            srmCoreServiceFacade.updateOrganisation(getServiceContextJefeNormalizacion(), organisationMetamacDto);
        }
    }

    @Test
    public void testUpdateOrganisationInPublishedScheme() throws Exception {
        try {
            OrganisationMetamacDto organisationMetamacDto = srmCoreServiceFacade.retrieveOrganisationByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V1_ORGANISATION_1);
            srmCoreServiceFacade.updateOrganisation(getServiceContextJefeNormalizacion(), organisationMetamacDto);
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testCreateOrganisation() throws Exception {

        // JefeNormalizacion
        {
            OrganisationSchemeMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2);
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {draftSchemeVersion};

            OrganisationMetamacDto organisationMetamacDto = OrganisationsMetamacDtoMocks.mockOrganisationDto(OrganisationTypeEnum.ORGANISATION_UNIT);
            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                organisationMetamacDto.setItemSchemeVersionUrn(organisationSchemeMetamacDto.getUrn());
                srmCoreServiceFacade.createOrganisation(getServiceContextJefeNormalizacion(), organisationMetamacDto);
            }
        }
        // Note: no more tests because security is same that update organisation
    }

    @Test
    public void testCreateAgencyInPublishedScheme() throws Exception {
        {
            // INTERNALLY PUBLISHED SCHEME
            OrganisationMetamacDto organisationMetamacDto = OrganisationsMetamacDtoMocks.mockOrganisationDto(OrganisationTypeEnum.AGENCY);
            organisationMetamacDto.setItemSchemeVersionUrn(ORGANISATION_SCHEME_9_V1);
            srmCoreServiceFacade.createOrganisation(getServiceContextJefeNormalizacion(), organisationMetamacDto);
        }
        {
            // EXTERNALLY PUBLISHED SCHEME
            OrganisationMetamacDto organisationMetamacDto = OrganisationsMetamacDtoMocks.mockOrganisationDto(OrganisationTypeEnum.AGENCY);
            organisationMetamacDto.setItemSchemeVersionUrn(ORGANISATION_SCHEME_8_V1);
            srmCoreServiceFacade.createOrganisation(getServiceContextJefeNormalizacion(), organisationMetamacDto);
        }
    }

    @Test
    public void testCreateOrganisationError() throws Exception {
        OrganisationSchemeMetamacDto draftSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V2);
        OrganisationSchemeMetamacDto prodValidationSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_5_V1);
        OrganisationSchemeMetamacDto diffValidationSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_6_V1);
        OrganisationSchemeMetamacDto internallyPublishedSchemeVersion = srmCoreServiceFacade.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), ORGANISATION_SCHEME_1_V1);

        OrganisationMetamacDto organisationMetamacDto = OrganisationsMetamacDtoMocks.mockOrganisationDto(OrganisationTypeEnum.AGENCY);

        {
            // TECNICO_PRODUCCION
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextTecnicoProduccion();

            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                try {
                    organisationMetamacDto.setItemSchemeVersionUrn(organisationSchemeMetamacDto.getUrn());
                    srmCoreServiceFacade.createOrganisation(ctx, organisationMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // JEFE_PRODUCCION
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            ServiceContext ctx = getServiceContextJefeProduccion();

            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                try {
                    organisationMetamacDto.setItemSchemeVersionUrn(organisationSchemeMetamacDto.getUrn());
                    srmCoreServiceFacade.createOrganisation(ctx, organisationMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // NOT_EXISTS
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {draftSchemeVersion, prodValidationSchemeVersion, diffValidationSchemeVersion};

            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                try {
                    organisationMetamacDto.setItemSchemeVersionUrn(organisationSchemeMetamacDto.getUrn());
                    srmCoreServiceFacade.createOrganisation(getServiceContextWithoutSrmRole(), organisationMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // SCHEME PUBLISHED
            OrganisationSchemeMetamacDto[] organisationSchemeMetamacDtos = {internallyPublishedSchemeVersion};

            for (OrganisationSchemeMetamacDto organisationSchemeMetamacDto : organisationSchemeMetamacDtos) {
                try {
                    organisationMetamacDto.setItemSchemeVersionUrn(organisationSchemeMetamacDto.getUrn());
                    srmCoreServiceFacade.createOrganisation(getServiceContextJefeNormalizacion(), organisationMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testRetrieveOrganisationByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveOrganisationByUrn(ctx, ORGANISATION_SCHEME_1_V1_ORGANISATION_1);
        }
    }

    @Test
    public void testRetrieveOrganisationByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveOrganisationByUrn(ctx, ORGANISATION_SCHEME_1_V1_ORGANISATION_1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testDeleteOrganisationJefeNormalizacion() throws Exception {
        srmCoreServiceFacade.deleteOrganisation(getServiceContextJefeNormalizacion(), ORGANISATION_SCHEME_1_V2_ORGANISATION_4_1_1);
        // Note: no more tests because security is same that update organisation
    }

    @Test
    public void testDeleteOrganisationError() throws Exception {
        {
            ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextTecnicoProduccion()};
            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.deleteOrganisation(ctx, ORGANISATION_SCHEME_1_V2_ORGANISATION_1);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        // Note: no more tests because security is same that update organisation
    }

    @Test
    public void testRetrieveOrganisationsByOrganisationSchemeUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveOrganisationsByOrganisationSchemeUrn(ctx, ORGANISATION_SCHEME_1_V1);
        }
    }

    @Test
    public void testRetrieveOrganisationsByOrganisationSchemeUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveOrganisationsByOrganisationSchemeUrn(ctx, ORGANISATION_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testFindOrganisationsByCondition() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.findOrganisationsByCondition(ctx, getMetamacCriteria());
        }
    }

    @Test
    public void testFindOrganisationsByConditionError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.findOrganisationsByCondition(ctx, getMetamacCriteria());
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
        order.setPropertyName(OrganisationSchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        return metamacCriteria;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmOrganisationsTest.xml";
    }

}
