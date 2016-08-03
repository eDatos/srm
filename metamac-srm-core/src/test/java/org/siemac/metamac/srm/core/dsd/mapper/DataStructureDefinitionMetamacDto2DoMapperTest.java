package org.siemac.metamac.srm.core.dsd.mapper;

import static org.junit.Assert.assertTrue;

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

import com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts.MapperEnum;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataAttribute;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataAttributeDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DataStructureDefinitionMetamacDto2DoMapperTest extends SrmBaseTest {

    @Autowired
    private DataStructureDefinitionDto2DoMapper dataStructureDefinitionDto2DoMapper;

    @Test
    public void testDataStructureDefinitionMetamacDtoToDo() throws MetamacException {
        DataStructureDefinitionMetamacDto dto = DataStructureDefinitionMetamacDtoMocks.mockDataStructureDefinitionMetamacDto();
        DataStructureDefinitionMetamacDtoMocks.mockHeading(dto, DSD_6_V1_TIME_DIMENSION_1, DSD_6_V1_MEASURE_DIMENSION_1);
        DataStructureDefinitionMetamacDtoMocks.mockStub(dto, DSD_6_V1_DIMENSION_1);
        DataStructureDefinitionMetamacDtoMocks.mockShowDecimalsPrecision(dto, CONCEPT_SCHEME_4_V1_CONCEPT_1, CONCEPT_SCHEME_5_V1_CONCEPT_1);

        dto.setUrn(DSD_6_V1);

        DataStructureDefinitionVersionMetamac entity = dataStructureDefinitionDto2DoMapper.dataStructureDefinitionDtoToDataStructureDefinition(dto);

        DataStructureDefinitionsMetamacAsserts.assertEqualsDataStructureDefinition(dto, entity, MapperEnum.DTO2DO);
    }

    @Test
    public void testComponentDtoToDo() throws MetamacException {

        DataAttributeDto dataAttributeDto = DataStructureDefinitionMetamacDtoMocks.createDataAttributeDtoTypeDimensionMock("dataAttribute-01");

        dataAttributeDto.getLocalRepresentation().setEnumeration(DataStructureDefinitionMetamacDtoMocks.mockRelatedResourceToConceptSchemeDto());

        DataAttribute dataAttribute = dataStructureDefinitionDto2DoMapper.componentDtoToComponent(dataAttributeDto);

        assertTrue(dataAttribute.getLocalRepresentation().getIsExtended());
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }
}