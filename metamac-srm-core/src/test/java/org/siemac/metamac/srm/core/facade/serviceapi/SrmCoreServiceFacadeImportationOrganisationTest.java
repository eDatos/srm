package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.srm.core.task.serviceapi.utils.TasksDtoMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeImportationOrganisationTest extends SrmBaseTest {

    private static Logger                 logger                                             = LoggerFactory.getLogger(SrmCoreServiceFacadeImportationOrganisationTest.class);

    // AGENCIES SCHEMES
    private final String                  AGENCYSCHEME_SDMX01_AGENCIES_V1                    = "urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=SDMX01:AGENCIES(1.0)";
    private final String                  AGENCYSCHEME_SDMX01_DATA_CONSUMERS_V1              = "urn:sdmx:org.sdmx.infomodel.base.DataConsumerScheme=SDMX01:DATA_CONSUMERS(1.0)";
    private final String                  AGENCYSCHEME_SDMX01_DATA_PROVIDERS_V1              = "urn:sdmx:org.sdmx.infomodel.base.DataProviderScheme=SDMX01:DATA_PROVIDERS(1.0)";
    private final String                  AGENCYSCHEME_SDMX01_ORGANISATION_UNIT_SCHEME_01_V1 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATION_UNIT_SCHEME_01(1.0)";
    private final String                  AGENCYSCHEME_SDMX01_ORGANISATION_UNIT_SCHEME_01_V2 = "urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=SDMX01:ORGANISATION_UNIT_SCHEME_01(2.0)";

    @Autowired
    protected SrmCoreServiceFacade        srmCoreServiceFacade;

    @Autowired
    protected CodesMetamacService         codesMetamacService;

    @Autowired
    protected OrganisationsMetamacService organisationsMetamacService;

    @Autowired
    protected ConceptsMetamacService      conceptsMetamacService;

    @Autowired
    protected PlatformTransactionManager  transactionManager;

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }

    @Test
    @DirtyDatabase
    public void testImport_EXAMPLE_ORGANISATIONS() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.EXAMPLE_ORGANISATIONS)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });

        waitUntilJobFinished();

        OrganisationSchemeVersionMetamac organisationSchemeVersion = null;

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_AGENCIES_V1);
        assertEquals(1, organisationSchemeVersion.getItems().size());

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_DATA_CONSUMERS_V1);
        assertEquals(1, organisationSchemeVersion.getItems().size());

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_DATA_PROVIDERS_V1);
        assertEquals(1, organisationSchemeVersion.getItems().size());

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_ORGANISATION_UNIT_SCHEME_01_V1);
        assertEquals(2, organisationSchemeVersion.getItems().size());
        assertFalse(organisationSchemeVersion.getMaintainableArtefact().getIsLastVersion());

        organisationSchemeVersion = organisationsMetamacService.retrieveOrganisationSchemeByUrn(getServiceContextAdministrador(), AGENCYSCHEME_SDMX01_ORGANISATION_UNIT_SCHEME_01_V2);
        assertEquals(1, organisationSchemeVersion.getItems().size());
        assertTrue(organisationSchemeVersion.getMaintainableArtefact().getIsLastVersion());

        assertEquals(2, organisationSchemeVersion.getItemScheme().getVersions().size());

        // TODO testear las condiciones de importacion de METAMAC, así como la herencia en el versionado
    }

    @Test
    @DirtyDatabase
    public void testImport_EXAMPLE_ORGANISATIONS2() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.ORG_AGENCYSC_SDMX01)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });

        waitUntilJobFinished();
    }

}
