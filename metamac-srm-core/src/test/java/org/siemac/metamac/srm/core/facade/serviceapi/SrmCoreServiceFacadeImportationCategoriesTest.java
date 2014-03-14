package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
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

import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;
import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.srm.core.task.serviceapi.utils.TasksDtoMocks;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeImportationCategoriesTest extends SrmBaseTest {

    private static Logger                logger                               = LoggerFactory.getLogger(SrmCoreServiceFacadeImportationCategoriesTest.class);

    @Autowired
    protected SrmCoreServiceFacade       srmCoreServiceFacade;

    @Autowired
    protected PlatformTransactionManager transactionManager;

    // Categories
    private final String                 CATEGORYSCHEME_SDW_ECONOMIC_CONCEPTS = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:SDW_ECONOMIC_CONCEPTS(1.0)";

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }

    @Test
    @DirtyDatabase
    public void testImport_ECB_CATEGORIES() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.ECB_CATEGORIES)));
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
        CategorySchemeMetamacDto categorySchemeMetamacDto = null;
        categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORYSCHEME_SDW_ECONOMIC_CONCEPTS);
        assertNotNull(categorySchemeMetamacDto.getId());
        assertEquals(CATEGORYSCHEME_SDW_ECONOMIC_CONCEPTS, categorySchemeMetamacDto.getUrn());
    }

    @Test
    @DirtyDatabase
    public void testImport_ECB_CATEGORIES_InBackground() throws Exception {
        Long previousBackgroundLimit = SdmxConstants.NUM_BYTES_TO_PLANNIFY;
        SdmxConstants.NUM_BYTES_TO_PLANNIFY = Long.valueOf(100);

        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsg(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.ECB_CATEGORIES)));
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
        SdmxConstants.NUM_BYTES_TO_PLANNIFY = previousBackgroundLimit;

        CategorySchemeMetamacDto categorySchemeMetamacDto = null;
        categorySchemeMetamacDto = srmCoreServiceFacade.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORYSCHEME_SDW_ECONOMIC_CONCEPTS);
        assertNotNull(categorySchemeMetamacDto.getId());
        assertEquals(CATEGORYSCHEME_SDW_ECONOMIC_CONCEPTS, categorySchemeMetamacDto.getUrn());
    }
}
