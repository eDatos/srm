package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDtoMocks;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

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
        srmCoreServiceFacade.retrieveConceptSchemeByUrn(getServiceContextTecnicoProduccion(), CONCEPT_SCHEME_1_V1);
    }

    @Test
    public void testRetrieveConceptSchemeByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveConceptSchemeByUrn(ctx, CONCEPT_SCHEME_1_V1);
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
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCreateConceptScheme() throws Exception {
        srmCoreServiceFacade.createConceptScheme(getServiceContextAdministrador(), ConceptsMetamacDtoMocks.mockConceptSchemeDtoGlossaryType());
    }

    @Test
    public void testCreateConceptSchemeError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole(), getServiceContextJefeProduccion(),
                getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoProduccion(), getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.createConceptScheme(ctx, ConceptsMetamacDtoMocks.mockConceptSchemeDtoGlossaryType());
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

    }

    @Test
    public void testDeleteConceptScheme() throws Exception {

    }

    @Test
    public void testFindConceptSchemesByCondition() throws Exception {

    }

    @Test
    public void testSendConceptSchemeToProductionValidation() throws Exception {

    }

    @Test
    public void testSendConceptSchemeToDiffusionValidation() throws Exception {

    }

    @Test
    public void testRejectConceptSchemeProductionValidation() throws Exception {

    }

    @Test
    public void testRejectConceptSchemeDiffusionValidation() throws Exception {

    }

    @Test
    public void testPublishInternallyConceptScheme() throws Exception {

    }

    @Test
    public void testPublishExternallyConceptScheme() throws Exception {

    }

    @Test
    public void testVersioningConceptScheme() throws Exception {

    }

    @Test
    public void testCancelConceptSchemeValidity() throws Exception {

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

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptSchemeTest.xml";
    }

}