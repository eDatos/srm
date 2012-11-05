package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
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
public class SrmCoreServiceFacadeCategorisationsSecurityTest extends SrmBaseTest {

    @Autowired
    protected SrmCoreServiceFacade srmCoreServiceFacade;

    private String                 CATEGORISATION_4 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)";

    @Test
    public void testErrorPrincipalNotFound() throws Exception {
        try {
            ServiceContext ctx = getServiceContextAdministrador();
            ctx.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, null);
            srmCoreServiceFacade.retrieveCategorisationByUrn(ctx, CATEGORISATION_4);
            fail("principal required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    public void testRetrieveCategorisationByUrn() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoNormalizacion(), getServiceContextJefeNormalizacion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCategorisationByUrn(ctx, CATEGORISATION_4);
        }
    }

    @Test
    public void testRetrieveCategorisationByUrnError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCategorisationByUrn(ctx, CATEGORISATION_4);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testRetrieveCategorisationsByArtefact() throws Exception {
        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
                getServiceContextTecnicoProduccion(), getServiceContextJefeNormalizacion(), getServiceContextJefeProduccion(), getServiceContextAdministrador()};

        for (ServiceContext ctx : ctxs) {
            srmCoreServiceFacade.retrieveCategorisationsByArtefact(ctx, CONCEPT_SCHEME_1_V1);
        }
    }

    @Test
    public void testRetrieveCategorisationsByArtefactError() throws Exception {
        ServiceContext[] contexts = {getServiceContextWithoutAccesses(), getServiceContextWithoutAccessToApplication(), getServiceContextWithoutSrmRole()};

        for (ServiceContext ctx : contexts) {
            try {
                srmCoreServiceFacade.retrieveCategorisationsByArtefact(ctx, CONCEPT_SCHEME_1_V1);
                fail("action not allowed");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
            }
        }
    }

    @Test
    public void testCreateCategorisationForOrganisationScheme() throws Exception {

        String artefactCategorisedUrn = ORGANISATION_SCHEME_100_V1;
        String categoryUrn = CATEGORY_SCHEME_1_V1_CATEGORY_1;
        String maintainerUrn = AGENCY_ROOT_1_V1;

        srmCoreServiceFacade.createCategorisation(getServiceContextJefeNormalizacion(), categoryUrn, artefactCategorisedUrn, maintainerUrn);
        // Note: no more tests because security is similar to security in "update operations"
    }

    @Test
    public void testDeleteCategorisationForOrganisationSchemeError() {
        // TODO descomentar cuando se aclare duda de seguridad de categorizaciones
//        ServiceContext[] ctxs = {getServiceContextTecnicoApoyoNormalizacion(), getServiceContextTecnicoApoyoProduccion(), getServiceContextTecnicoNormalizacion(),
//                getServiceContextTecnicoProduccion(), getServiceContextJefeProduccion(), getServiceContextWithoutSrmRole()};
//
//        for (ServiceContext ctx : ctxs) {
//            try {
//                srmCoreServiceFacade.deleteCategorisation(ctx, CATEGORISATION_4);
//                fail("action not allowed");
//            } catch (MetamacException e) {
//                assertEquals(1, e.getExceptionItems().size());
//                assertEquals(ServiceExceptionType.SECURITY_ACTION_NOT_ALLOWED.getCode(), e.getExceptionItems().get(0).getCode());
//            }
//        }
        // Note: no more tests because security is similar to security in "update operations"
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategorisationsTest.xml";
    }
}