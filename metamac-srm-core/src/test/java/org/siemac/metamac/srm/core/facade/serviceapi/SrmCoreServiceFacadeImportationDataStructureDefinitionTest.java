package org.siemac.metamac.srm.core.facade.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
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

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.CodesService;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;
import com.arte.statistic.sdmx.srm.core.facade.serviceapi.utils.SdmxResources;
import com.arte.statistic.sdmx.srm.core.task.serviceapi.utils.TasksDtoMocks;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class SrmCoreServiceFacadeImportationDataStructureDefinitionTest extends SrmBaseTest {

    private static Logger                 logger                                      = LoggerFactory.getLogger(SrmCoreServiceFacadeImportationDataStructureDefinitionTest.class);

    @Autowired
    protected SrmCoreServiceFacade        srmCoreServiceFacade;

    @Autowired
    protected PlatformTransactionManager  transactionManager;

    @Autowired
    protected ItemSchemeVersionRepository itemSchemeVersionRepository;

    @Autowired
    protected CodesService                codesService;

    @Autowired
    protected ConceptsService             conceptsService;

    // CODELISTS
    private final String                  CODELIST_SDMX_CL_DECIMALS_V1                = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_DECIMALS(1.0)";
    private final String                  CODELIST_SDMX_CL_FREQ_V1                    = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_FREQ(1.0)";
    private final String                  CODELIST_SDMX_CL_CONF_STATUS_V1             = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_CONF_STATUS(1.0)";
    private final String                  CODELIST_SDMX_CL_OBS_STATUS_V1              = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_OBS_STATUS(1.0)";
    private final String                  CODELIST_SDMX_CL_UNIT_MULT_V1               = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=SDMX:CL_UNIT_MULT(1.0)";
    private final String                  CODELIST_ECB_CL_EXR_TYPE_V1                 = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ECB:CL_EXR_TYPE(1.0)";
    private final String                  CODELIST_ECB_CL_EXR_VAR_V1                  = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ECB:CL_EXR_VAR(1.0)";
    private final String                  CODELIST_ISO_CL_CURRENCY_V1                 = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISO:CL_CURRENCY(1.0)";

    // CONCEPTS SCHEMES
    private final String                  CONCEPTSCHEME_SDMX_CROSS_DOMAIN_CONCEPTS_V1 = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=SDMX:CROSS_DOMAIN_CONCEPTS(1.0)";
    private final String                  CONCEPTSCHEME_ECB_CONCEPTS_V1               = "urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=ECB:ECB_CONCEPTS(1.0)";

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }

    @Test
    @DirtyDatabase
    public void testImport_DSD_ECB_EXR_NG_FULL() throws Exception {

        // CODELIST
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_NG_CODELISTS)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // Wait until the job is finished
        waitUntilJobFinished();

        // Save to force incorrect metadata in codelist
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {

                        // CL_DECIMALS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_DECIMALS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_FREQ
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_FREQ_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_CONF_STATUS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_CONF_STATUS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_OBS_STATUS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_OBS_STATUS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_UNIT_MULT
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_UNIT_MULT_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_EXR_TYPE
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ECB_CL_EXR_TYPE_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_EXR_VAR
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ECB_CL_EXR_VAR_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_CURRENCY
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ISO_CL_CURRENCY_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }
                    } catch (MetamacException e) {
                        e.printStackTrace();
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // CONCEPTS SCHEMES
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_NG_CONCEPTS)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // Wait until the job is finished
        waitUntilJobFinished();

        // Save to force incorrect metadata in codelist
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {

                        // CROSS_DOMAIN_CONCEPTS
                        {
                            ConceptSchemeVersion conceptSchemeByUrn = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX_CROSS_DOMAIN_CONCEPTS_V1);
                            conceptSchemeByUrn.getMaintainableArtefact().setFinalLogic(true);
                            conceptSchemeByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(conceptSchemeByUrn);
                        }

                        // ECB_CONCEPTS
                        {
                            ConceptSchemeVersion conceptSchemeByUrn = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_ECB_CONCEPTS_V1);
                            conceptSchemeByUrn.getMaintainableArtefact().setFinalLogic(true);
                            conceptSchemeByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(conceptSchemeByUrn);
                        }

                    } catch (MetamacException e) {
                        e.printStackTrace();
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // DSD
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_NG_FULL)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        waitUntilJobFinished();

        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_NG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_NG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());

        List<DescriptorDto> dimDescriptor = srmCoreServiceFacade.findDescriptorsForDataStructureDefinition(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_NG_FULL_URN,
                TypeComponentList.DIMENSION_DESCRIPTOR);
        assertTrue(dimDescriptor.iterator().next().getAnnotations().isEmpty());
    }

    @Test
    @DirtyDatabase
    public void testImport_DSD_ECB_EXR_SG_FULL() throws Exception {

        // CODELIST
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_SG_CODELISTS)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // Wait until the job is finished
        waitUntilJobFinished();

        // Save to force incorrect metadata in codelist
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {

                        // CL_DECIMALS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_DECIMALS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_FREQ
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_FREQ_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_CONF_STATUS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_CONF_STATUS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_OBS_STATUS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_OBS_STATUS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_UNIT_MULT
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_UNIT_MULT_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_EXR_TYPE
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ECB_CL_EXR_TYPE_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_EXR_VAR
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ECB_CL_EXR_VAR_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_CURRENCY
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ISO_CL_CURRENCY_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }
                    } catch (MetamacException e) {
                        e.printStackTrace();
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // CONCEPTS SCHEMES
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_SG_CONCEPTS)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // Wait until the job is finished
        waitUntilJobFinished();

        // Save to force incorrect metadata in codelist
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {

                        // CROSS_DOMAIN_CONCEPTS
                        {
                            ConceptSchemeVersion conceptSchemeByUrn = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX_CROSS_DOMAIN_CONCEPTS_V1);
                            conceptSchemeByUrn.getMaintainableArtefact().setFinalLogic(true);
                            conceptSchemeByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(conceptSchemeByUrn);
                        }

                        // ECB_CONCEPTS
                        {
                            ConceptSchemeVersion conceptSchemeByUrn = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_ECB_CONCEPTS_V1);
                            conceptSchemeByUrn.getMaintainableArtefact().setFinalLogic(true);
                            conceptSchemeByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(conceptSchemeByUrn);
                        }

                    } catch (MetamacException e) {
                        e.printStackTrace();
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_SG_FULL)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        waitUntilJobFinished();

        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_SG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_SG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Test
    @DirtyDatabase
    public void testImport_DSD_ECB_EXR_RG_FULL() throws Exception {

        // CODELIST
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_RG_CODELISTS)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // Wait until the job is finished
        waitUntilJobFinished();

        // Save to force incorrect metadata in codelist
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {

                        // CL_DECIMALS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_DECIMALS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_FREQ
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_FREQ_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_CONF_STATUS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_CONF_STATUS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_OBS_STATUS
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_OBS_STATUS_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_UNIT_MULT
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_SDMX_CL_UNIT_MULT_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_EXR_TYPE
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ECB_CL_EXR_TYPE_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_EXR_VAR
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ECB_CL_EXR_VAR_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }

                        // CL_CURRENCY
                        {
                            CodelistVersion codelistByUrn = codesService.retrieveCodelistByUrn(getServiceContextAdministrador(), CODELIST_ISO_CL_CURRENCY_V1);
                            codelistByUrn.getMaintainableArtefact().setFinalLogic(true);
                            codelistByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(codelistByUrn);
                        }
                    } catch (MetamacException e) {
                        e.printStackTrace();
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // CONCEPTS SCHEMES
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_RG_CONCEPTS)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        // Wait until the job is finished
        waitUntilJobFinished();

        // Save to force incorrect metadata in codelist
        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {

                        // CROSS_DOMAIN_CONCEPTS
                        {
                            ConceptSchemeVersion conceptSchemeByUrn = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_SDMX_CROSS_DOMAIN_CONCEPTS_V1);
                            conceptSchemeByUrn.getMaintainableArtefact().setFinalLogic(true);
                            conceptSchemeByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(conceptSchemeByUrn);
                        }

                        // ECB_CONCEPTS
                        {
                            ConceptSchemeVersion conceptSchemeByUrn = conceptsService.retrieveConceptSchemeByUrn(getServiceContextAdministrador(), CONCEPTSCHEME_ECB_CONCEPTS_V1);
                            conceptSchemeByUrn.getMaintainableArtefact().setFinalLogic(true);
                            conceptSchemeByUrn.getMaintainableArtefact().setPublicLogic(true);
                            itemSchemeVersionRepository.save(conceptSchemeByUrn);
                        }

                    } catch (MetamacException e) {
                        e.printStackTrace();
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        {
            // New Transaction: Because the job needs persisted data
            final TransactionTemplate tt = new TransactionTemplate(transactionManager);
            tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            tt.execute(new TransactionCallbackWithoutResult() {

                @Override
                public void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_ECB_EXR_RG_FULL)));
                    } catch (MetamacException e) {
                        logger.error("Job thread failed: ", e);
                    } catch (FileNotFoundException e) {
                        logger.error("Job thread failed: ", e);
                    }
                    logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
                }
            });
        }

        waitUntilJobFinished();

        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_ECB_EXR_RG_FULL_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_ECB_EXR_RG_FULL_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_DPOP() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_DPOP)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_DPOP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_DPOP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_EPOP() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_EPOP)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_EPOP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_EPOP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_IDB() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_IDB)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IDB_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IDB_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_IPC() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_IPC)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IPC_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IPC_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_IPCA() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_IPCA)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_IPCA_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_IPCA_URN, dataStructureDefinitionMetamacDto.getUrn());
    }

    @Ignore
    @Test
    @DirtyDatabase
    public void testImport_DSD_INE_MNP() throws Exception {
        // New Transaction: Because the job needs persisted data
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    srmCoreServiceFacade.importSDMXStructureMsgInBackground(getServiceContextAdministrador(), TasksDtoMocks.createContentInput(new File(SdmxResources.DSD_INE_MNP)));
                } catch (MetamacException e) {
                    logger.error("Job thread failed: ", e);
                } catch (FileNotFoundException e) {
                    logger.error("Job thread failed: ", e);
                }
                logger.info("-- doInTransactionWithoutResult -- expects transaction commit");
            }
        });
        waitUntilJobFinished();
        DataStructureDefinitionMetamacDto dataStructureDefinitionMetamacDto = null;
        dataStructureDefinitionMetamacDto = srmCoreServiceFacade.retrieveDataStructureDefinitionByUrn(getServiceContextAdministrador(), SdmxResources.DSD_INE_MNP_URN);
        assertNotNull(dataStructureDefinitionMetamacDto.getId());
        assertEquals(SdmxResources.DSD_INE_MNP_URN, dataStructureDefinitionMetamacDto.getUrn());
    }
}
