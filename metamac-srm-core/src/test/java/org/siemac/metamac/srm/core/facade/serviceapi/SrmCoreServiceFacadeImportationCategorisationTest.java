package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
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
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeImportationCategorisationTest extends SrmBaseTest {

    private static Logger                logger                           = LoggerFactory.getLogger(SrmCoreServiceFacadeImportationCategorisationTest.class);

    @Autowired
    protected SrmCoreServiceFacade       srmCoreServiceFacade;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    // Categorisation
    private final String                 CATEGORISATION_CATEGORISATION_01 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:CATEGORISATION_01(1.0)";

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategorisationsTest.xml";
    }

    @Test
    @DirtyDatabase
    public void testImport_CATEGORISATIONS() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.ECB_CATEGORISATIONS)));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MetamacException e) {
                    e.printStackTrace();
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });

        // Wait until the job is finished
        waitUntilJobFinished();

        CategorisationDto categorisationDto = null;
        categorisationDto = srmCoreServiceFacade.retrieveCategorisationByUrn(getServiceContextAdministrador(), CATEGORISATION_CATEGORISATION_01);
        assertNotNull(categorisationDto);
    }
}
