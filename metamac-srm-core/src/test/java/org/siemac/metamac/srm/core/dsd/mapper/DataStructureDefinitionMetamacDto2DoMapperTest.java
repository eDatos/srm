package org.siemac.metamac.srm.core.dsd.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionMetamacDtoMocks;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionsMetamacAsserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DataStructureDefinitionMetamacDto2DoMapperTest extends SrmBaseTest {

    private static String                       DSD_06_URN                       = "urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=SDMX01:DATASTRUCTUREDEFINITION06(01.000)";
    private static String                       DSD_06_DIM01_URN                 = "urn:sdmx:org.sdmx.infomodel.datastructure.Dimension=SDMX01:DATASTRUCTUREDEFINITION06(01.000).dim-01";
    private static String                       DSD_06_TIMEDIM01_URN             = "urn:sdmx:org.sdmx.infomodel.datastructure.TimeDimension=SDMX01:DATASTRUCTUREDEFINITION06(01.000).timeDimension-01";
    private static String                       DSD_06_MEASUREDIM01_URN          = "urn:sdmx:org.sdmx.infomodel.datastructure.MeasureDimension=SDMX01:DATASTRUCTUREDEFINITION06(01.000).measureDimension-01";

    private static String                       CONCEPT_SCHEME_13_CONCEPT_03_URN = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME13(01.000).CONCEPT03";
    private static String                       CONCEPT_SCHEME_13_CONCEPT_02_URN = "urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=SDMX01:CONCEPTSCHEME13(01.000).CONCEPT02";

    @Autowired
    private DataStructureDefinitionDto2DoMapper dataStructureDefinitionDto2DoMapper;

    @Test
    public void testDataStructureDefinitionMetamacDtoToDo() throws MetamacException {
        DataStructureDefinitionMetamacDto dto = DataStructureDefinitionMetamacDtoMocks.mockDataStructureDefinitionMetamacDto();
        DataStructureDefinitionMetamacDtoMocks.mockHeading(dto, DSD_06_TIMEDIM01_URN, DSD_06_MEASUREDIM01_URN);
        DataStructureDefinitionMetamacDtoMocks.mockStub(dto, DSD_06_DIM01_URN);
        DataStructureDefinitionMetamacDtoMocks.mockShowDecimalsPrecision(dto, CONCEPT_SCHEME_13_CONCEPT_03_URN, CONCEPT_SCHEME_13_CONCEPT_02_URN);

        dto.setUrn(DSD_06_URN);

        DataStructureDefinitionVersionMetamac entity = dataStructureDefinitionDto2DoMapper.dataStructureDefinitionDtoToDataStructureDefinition(dto);

        DataStructureDefinitionsMetamacAsserts.assertEqualsDataStructureDefinition(dto, entity);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }
}