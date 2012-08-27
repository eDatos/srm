package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaOrder.OrderTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDtoMocks;
import org.siemac.metamac.srm.core.criteria.ConceptSchemeVersionMetamacCriteriaPropertyEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;
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
public class SrmCoreServiceFacadeConceptSchemeSecurityTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    private static String          NOT_EXISTS = "not-exists";

    @Test
    public void testErrorPrincipalNotFound() throws Exception {
        try {
            ServiceContext ctx = getServiceContextAdministrador();
            ctx.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, null);
            srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_1_V1);
            fail("principal required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveConceptSchemeByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_1_V1);
        }
    }

    @Test
    public void testRetrieveConceptSchemeByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRetrieveConceptSchemeVersions() throws Exception {
        srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextTecnicoApoyoProduccion(), CONCEPT_SCHEME_1_V1);

    }

    @Test
    public void testRetrieveConceptSchemeVersionsError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveConceptSchemeVersions(ctx, CONCEPT_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        srmCoreServiceFacade.createConceptScheme(getServiceContextJefeNormalizacion(), ConceptsMetamacDtoMocks.mockConceptSchemeDtoGlossaryType());
    }

    @Test
    public void testCreateConceptSchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.createConceptScheme(ctx, ConceptsMetamacDtoMocks.mockConceptSchemeDtoGlossaryType());
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testUpdateConceptSchemeTecnicoProduccion() throws Exception {
        ConceptSchemeMetamacDto draftOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_8_V1);
        ConceptSchemeMetamacDto prodValidationOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_10_V3);

        ConceptSchemeMetamacDto[] conceptSchemeMetamacDtos = {draftOperationSchemeVersion, prodValidationOperationSchemeVersion};
        for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : conceptSchemeMetamacDtos) {
            srmCoreServiceFacade.updateConceptScheme(getServiceContextTecnicoProduccion(), conceptSchemeMetamacDto);
        }
    }

    @Test
    public void testUpdateConceptSchemeJefeNormalizacion() throws Exception {
        ConceptSchemeMetamacDto draftNonOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2);
        ConceptSchemeMetamacDto draftOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_8_V1);
        ConceptSchemeMetamacDto prodValidationNonOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_5_V1);
        ConceptSchemeMetamacDto prodValidationOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_10_V3);
        ConceptSchemeMetamacDto diffValidationNonOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_6_V1);
        ConceptSchemeMetamacDto diffValidationOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_11_V1);

        ConceptSchemeMetamacDto[] conceptSchemeMetamacDtos = {draftNonOperationSchemeVersion, draftOperationSchemeVersion, prodValidationNonOperationSchemeVersion,
                prodValidationOperationSchemeVersion, diffValidationNonOperationSchemeVersion, diffValidationOperationSchemeVersion};

        for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : conceptSchemeMetamacDtos) {
            srmCoreServiceFacade.updateConceptScheme(getServiceContextJefeNormalizacion(), conceptSchemeMetamacDto);
        }
    }

    @Test
    public void testUpdateConceptSchemeError() throws Exception {
        ConceptSchemeMetamacDto draftNonOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_1_V2);
        ConceptSchemeMetamacDto draftOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_8_V1);
        ConceptSchemeMetamacDto prodValidationNonOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_5_V1);
        ConceptSchemeMetamacDto prodValidationOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_10_V3);
        ConceptSchemeMetamacDto diffValidationNonOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_6_V1);
        ConceptSchemeMetamacDto diffValidationOperationSchemeVersion = srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPT_SCHEME_11_V1);
        {
            // TECNICO_PRODUCCION
            ConceptSchemeMetamacDto[] conceptSchemeMetamacDtos = {draftNonOperationSchemeVersion, prodValidationNonOperationSchemeVersion, prodValidationOperationSchemeVersion,
                    diffValidationNonOperationSchemeVersion, diffValidationOperationSchemeVersion};

            ServiceContext ctx = getServiceContextTecnicoProduccion();

            for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : conceptSchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.createConceptScheme(ctx, conceptSchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // JEFE_PRODUCCION
            ConceptSchemeMetamacDto[] conceptSchemeMetamacDtos = {draftNonOperationSchemeVersion, prodValidationNonOperationSchemeVersion, diffValidationNonOperationSchemeVersion};

            ServiceContext ctx = getServiceContextJefeProduccion();

            for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : conceptSchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.createConceptScheme(ctx, conceptSchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            // NOT_EXISTS
            ConceptSchemeMetamacDto[] conceptSchemeMetamacDtos = {draftNonOperationSchemeVersion, draftOperationSchemeVersion, prodValidationNonOperationSchemeVersion,
                    prodValidationOperationSchemeVersion, diffValidationNonOperationSchemeVersion, diffValidationOperationSchemeVersion};

            for (ConceptSchemeMetamacDto conceptSchemeMetamacDto : conceptSchemeMetamacDtos) {
                try {
                    srmCoreServiceFacade.createConceptScheme(getServiceContextWithoutSrmRole(), conceptSchemeMetamacDto);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testDeleteConceptSchemeJefeNormalizacion() throws Exception {
        String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_2_V1;
        String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;

        String[] urns = {nonOperationConceptSchemeUrn, operationConceptSchemeUrn};

        for (String urn : urns) {
            srmCoreServiceFacade.deleteConceptScheme(getServiceContextJefeNormalizacion(), urn);
        }
    }

    @Test
    public void testDeleteConceptSchemeJefeProduccion() throws Exception {
        String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;

        srmCoreServiceFacade.deleteConceptScheme(getServiceContextJefeProduccion(), operationConceptSchemeUrn);
    }

    @Test
    public void testDeleteConceptSchemeError() {
        String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;
        {
            ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : ctxs) {
                try {
                    srmCoreServiceFacade.deleteConceptScheme(ctx, nonOperationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
                try {
                    srmCoreServiceFacade.deleteConceptScheme(ctx, operationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            try {
                srmCoreServiceFacade.deleteConceptScheme(getServiceContextJefeProduccion(), nonOperationConceptSchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testFindConceptSchemesByCondition() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.findConceptSchemesByCondition(ctx, getMetamacCriteria());
        }
    }

    @Test
    public void testFindConceptSchemesByConditionError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.findConceptSchemesByCondition(ctx, getMetamacCriteria());
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationTecnicoApoyoNormalizacion() throws Exception {
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_1_V2;
            srmCoreServiceFacade.sendConceptSchemeToProductionValidation(getServiceContextTecnicoApoyoNormalizacion(), nonOperationConceptSchemeUrn);
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;
            srmCoreServiceFacade.sendConceptSchemeToProductionValidation(getServiceContextTecnicoApoyoNormalizacion(), operationConceptSchemeUrn);
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_1_V2;
            srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, nonOperationConceptSchemeUrn);
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;
            srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, operationConceptSchemeUrn);
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_1_V2;
            srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, nonOperationConceptSchemeUrn);
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;
            srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, operationConceptSchemeUrn);
        }
    }

    @Test
    public void testSendConceptSchemeToProductionValidationTecnicoProduccion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoProduccion();
        String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;
        srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, operationConceptSchemeUrn);
    }

    @Test
    public void testSendConceptSchemeToProductionValidationTecnicoApoyoProduccion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoApoyoProduccion();
        String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;
        srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, operationConceptSchemeUrn);
    }

    @Test
    public void testSendConceptSchemeToProductionValidationJefeProduccion() throws Exception {
        ServiceContext ctx = getServiceContextJefeProduccion();
        String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;
        srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, operationConceptSchemeUrn);
    }

    @Test
    public void testSendConceptSchemeToProductionValidationError() throws Exception {
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_1_V2;

            ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutAccesses(),
                    getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, nonOperationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;

            ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.sendConceptSchemeToProductionValidation(ctx, operationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_5_V1;
            srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, nonOperationConceptSchemeUrn);
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;
            srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, operationConceptSchemeUrn);
        }
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_5_V1;
            srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, nonOperationConceptSchemeUrn);
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;
            srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, operationConceptSchemeUrn);
        }
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidationTecnicoProduccion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoProduccion();
        String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;
        srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, operationConceptSchemeUrn);
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidationJefeProduccion() throws Exception {
        ServiceContext ctx = getServiceContextJefeProduccion();
        String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;
        srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, operationConceptSchemeUrn);
    }

    @Test
    public void testSendConceptSchemeToDiffusionValidationError() throws Exception {
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_5_V1;

            ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(),
                    getServiceContextTecnicoApoyoNormalizacion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, nonOperationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;

            ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextWithoutAccesses(),
                    getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.sendConceptSchemeToDiffusionValidation(ctx, operationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testRejectConceptSchemeToProductionValidationTecnicoNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoNormalizacion();
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_5_V1;
            srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, nonOperationConceptSchemeUrn);
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;
            srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, operationConceptSchemeUrn);
        }
    }

    @Test
    public void testRejectConceptSchemeToProductionValidationJefeNormalizacion() throws Exception {
        ServiceContext ctx = getServiceContextJefeNormalizacion();
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_5_V1;
            srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, nonOperationConceptSchemeUrn);
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;
            srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, operationConceptSchemeUrn);
        }
    }

    @Test
    public void testRejectConceptSchemeToProductionValidationTecnicoProduccion() throws Exception {
        ServiceContext ctx = getServiceContextTecnicoProduccion();
        String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;
        srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, operationConceptSchemeUrn);
    }

    @Test
    public void testRejectConceptSchemeToProductionValidationJefeProduccion() throws Exception {
        ServiceContext ctx = getServiceContextJefeProduccion();
        String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;
        srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, operationConceptSchemeUrn);
    }

    @Test
    public void testRejectConceptSchemeToProductionValidationError() throws Exception {
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_5_V1;

            ServiceContext[] contexts = {getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(),
                    getServiceContextTecnicoApoyoNormalizacion(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, nonOperationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            String operationConceptSchemeUrn = CONCEPT_SCHEME_10_V3;

            ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextWithoutAccesses(),
                    getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.rejectConceptSchemeProductionValidation(ctx, operationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testRejectConceptSchemeDiffusionValidation() throws Exception {
        srmCoreServiceFacade.rejectConceptSchemeDiffusionValidation(getServiceContextJefeNormalizacion(), CONCEPT_SCHEME_11_V1);
    }

    @Test
    public void testRejectConceptSchemeDiffusionValidationError() throws Exception {
        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextAdministrador(), getServiceContextWithoutAccesses(),
                getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.rejectConceptSchemeDiffusionValidation(ctx, CONCEPT_SCHEME_11_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testPublishInternallyConceptScheme() throws Exception {
        srmCoreServiceFacade.publishInternallyConceptScheme(getServiceContextJefeNormalizacion(), CONCEPT_SCHEME_11_V1);
    }

    @Test
    public void testPublishInternallyConceptSchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextAdministrador(), getServiceContextWithoutAccesses(),
                getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.publishInternallyConceptScheme(ctx, CONCEPT_SCHEME_11_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testPublishExternallyConceptSchemeJefeNormalizacion() throws Exception {
        String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_3_V1;
        srmCoreServiceFacade.publishExternallyConceptScheme(getServiceContextJefeNormalizacion(), nonOperationConceptSchemeUrn);

        String operationConceptSchemeUrn = CONCEPT_SCHEME_1_V1;
        srmCoreServiceFacade.publishExternallyConceptScheme(getServiceContextJefeNormalizacion(), operationConceptSchemeUrn);
    }

    @Test
    public void testPublishExternallyConceptSchemeJefeProduccion() throws Exception {
        String operationConceptSchemeUrn = CONCEPT_SCHEME_1_V1;
        srmCoreServiceFacade.publishExternallyConceptScheme(getServiceContextJefeProduccion(), operationConceptSchemeUrn);
    }

    @Test
    public void testPublishExternallyConceptSchemeError() throws Exception {
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_3_V1;

            ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextAdministrador(), getServiceContextWithoutAccesses(),
                    getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.publishExternallyConceptScheme(ctx, nonOperationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_1_V1;

            ServiceContext[] contexts = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextAdministrador(), getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(),
                    getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : contexts) {
                try {
                    srmCoreServiceFacade.publishExternallyConceptScheme(ctx, nonOperationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
    }

    @Test
    public void testVersioningConceptScheme() throws Exception {
        srmCoreServiceFacade.versioningConceptScheme(getServiceContextJefeNormalizacion(), CONCEPT_SCHEME_7_V1, VersionTypeEnum.MAJOR);
    }

    @Test
    public void testVersioningConceptSchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.versioningConceptScheme(ctx, CONCEPT_SCHEME_7_V1, VersionTypeEnum.MAJOR);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCancelConceptSchemeValidityJefeNormalizacion() throws Exception {
        String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_7_V1;
        String operationConceptSchemeUrn = CONCEPT_SCHEME_12_V1;

        String[] urns = {nonOperationConceptSchemeUrn, operationConceptSchemeUrn};

        for (String urn : urns) {
            srmCoreServiceFacade.cancelConceptSchemeValidity(getServiceContextJefeNormalizacion(), urn);
        }
    }

    @Test
    public void testCancelConceptSchemeValidityJefeProduccion() throws Exception {
        String operationConceptSchemeUrn = CONCEPT_SCHEME_12_V1;

        srmCoreServiceFacade.cancelConceptSchemeValidity(getServiceContextJefeProduccion(), operationConceptSchemeUrn);
    }

    @Test
    public void testCancelConceptSchemeValidityError() throws Exception {
        String nonOperationConceptSchemeUrn = CONCEPT_SCHEME_1_V2;
        String operationConceptSchemeUrn = CONCEPT_SCHEME_8_V1;
        {
            ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                    getServiceContextTecnicoProduccion(), getServiceContextWithoutSrmRole()};

            for (ServiceContext ctx : ctxs) {
                try {
                    srmCoreServiceFacade.cancelConceptSchemeValidity(ctx, nonOperationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
                try {
                    srmCoreServiceFacade.cancelConceptSchemeValidity(ctx, operationConceptSchemeUrn);
                    fail("action not allowed");
                } catch (MetamacException e) {
                    assertEquals(1, e.getExceptionItems().size());
                    assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
                }
            }
        }
        {
            try {
                srmCoreServiceFacade.cancelConceptSchemeValidity(getServiceContextJefeProduccion(), nonOperationConceptSchemeUrn);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    private ServiceContext getServiceContextWithoutAccesses() {
        ServiceContext ctxWithoutAcceses = getServiceContextJefeNormalizacion();
        ((MetamacPrincipal) ctxWithoutAcceses.getProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE)).getAccesses().clear();
        return ctxWithoutAcceses;
    }

    private ServiceContext getServiceContextWithoutAccessToApplication() {
        ServiceContext ctxWithoutAccessToApplication = getServiceContextJefeNormalizacion();
        ((MetamacPrincipal) ctxWithoutAccessToApplication.getProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE)).getAccesses().get(0).setApplication(NOT_EXISTS);
        return ctxWithoutAccessToApplication;
    }

    private ServiceContext getServiceContextWithoutSrmRole() {
        ServiceContext ctxWithoutSrmRole = getServiceContextJefeNormalizacion();
        ((MetamacPrincipal) ctxWithoutSrmRole.getProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE)).getAccesses().get(0).setRole(NOT_EXISTS);
        return ctxWithoutSrmRole;
    }

    private MetamacCriteria getMetamacCriteria() {
        MetamacCriteria metamacCriteria = new MetamacCriteria();
        MetamacCriteriaOrder order = new MetamacCriteriaOrder();
        order.setType(OrderTypeEnum.ASC);
        order.setPropertyName(ConceptSchemeVersionMetamacCriteriaPropertyEnum.URN.name());
        metamacCriteria.setOrdersBy(new ArrayList<MetamacCriteriaOrder>());
        metamacCriteria.getOrdersBy().add(order);
        return metamacCriteria;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptSchemeTest.xml";
    }

}