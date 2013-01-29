package org.siemac.metamac.srm.core.code.mapper;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
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
public class CodesDto2DoMapperTest extends SrmBaseTest {

    @Autowired
    private org.siemac.metamac.srm.core.code.mapper.CodesDto2DoMapper codesDto2DoMapper;

    @Test
    public void testCodelistMetamacDtoToDo() throws MetamacException {
        CodelistMetamacDto dto = CodesMetamacDtoMocks.mockCodelistDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        dto.addReplaceToCodelist(CodesMetamacDtoMocks.mockCodelistRelatedResourceDto("CODELIST10", CODELIST_10_V1));
        dto.addReplaceToCodelist(CodesMetamacDtoMocks.mockCodelistRelatedResourceDto("CODELIST11", CODELIST_11_V1));

        CodelistVersionMetamac entity = codesDto2DoMapper.codelistDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsCodelist(dto, entity);
    }

    @Test
    public void testCodelistDtoToDoWithLoad() throws MetamacException {
        CodelistMetamacDto dto = CodesMetamacDtoMocks.mockCodelistDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        dto.setUrn(CODELIST_1_V2);
        dto.setDefaultOrderVisualisation(CodesMetamacDtoMocks.mockCodelistOrderVisualisationRelatedResourceDto("VISUALISATION_02", CODELIST_1_V2_ORDER_VISUALISATION_02));
        dto.setVersionOptimisticLocking(1L);
        CodelistVersionMetamac entity = codesDto2DoMapper.codelistDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsCodelist(dto, entity);
    }

    @Test
    public void testCodelistMetamacDtoToDoCheckIsVariableUpdated() throws MetamacException {
        CodelistMetamacDto dto = CodesMetamacDtoMocks.mockCodelistDto(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        dto.setUrn(CODELIST_1_V2);
        dto.setVersionOptimisticLocking(1L);
        dto.setVariable(CodesMetamacDtoMocks.mockVariableElementRelatedResourceDto("VARIABLE_01", VARIABLE_1)); // actual is VARIABLE_2
        CodelistVersionMetamac entity = codesDto2DoMapper.codelistDtoToDo(dto);

        assertTrue(entity.getIsVariableUpdated());
    }

    @Test
    public void testCodeMetamacDoToDto() throws MetamacException {
        CodeMetamacDto dto = CodesMetamacDtoMocks.mockCodeDto();
        dto.setVariableElement(CodesMetamacDtoMocks.mockVariableElementRelatedResourceDto("VARIABLE_02_VARIABLE_ELEMENT_01", VARIABLE_2_VARIABLE_ELEMENT_1));

        CodeMetamac entity = codesDto2DoMapper.codeDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsCode(dto, entity);
    }

    @Test
    public void testCodelistFamilyDtoToDo() throws MetamacException {
        CodelistFamilyDto dto = CodesMetamacDtoMocks.mockCodelistFamilyDto();

        CodelistFamily entity = codesDto2DoMapper.codelistFamilyDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsCodelistFamily(dto, entity);
    }

    @Test
    public void testVariableFamilyDtoToDo() throws MetamacException {
        VariableFamilyDto dto = CodesMetamacDtoMocks.mockVariableFamilyDto();

        VariableFamily entity = codesDto2DoMapper.variableFamilyDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsVariableFamily(dto, entity);
    }

    @Test
    public void testVariableDtoToDo() throws MetamacException {
        VariableDto dto = CodesMetamacDtoMocks.mockVariableDto();
        dto.addFamily(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_FAMILY_01", VARIABLE_FAMILY_1));
        dto.addFamily(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_FAMILY_02", VARIABLE_FAMILY_2));
        dto.addReplaceToVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_01", VARIABLE_1));
        dto.addReplaceToVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_02", VARIABLE_2));

        Variable entity = codesDto2DoMapper.variableDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsVariable(dto, entity);
    }

    @Test
    public void testVariableElementDtoToDo() throws MetamacException {
        VariableElementDto dto = CodesMetamacDtoMocks.mockVariableElementDto();
        dto.setVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE_01", VARIABLE_1));
        dto.addReplaceToVariableElement(CodesMetamacDtoMocks.mockVariableElementRelatedResourceDto("VARIABLE_ELEMENT_01", VARIABLE_2_VARIABLE_ELEMENT_1));
        dto.addReplaceToVariableElement(CodesMetamacDtoMocks.mockVariableElementRelatedResourceDto("VARIABLE_ELEMENT_02", VARIABLE_2_VARIABLE_ELEMENT_2));

        VariableElement entity = codesDto2DoMapper.variableElementDtoToDo(dto);
        CodesMetamacAsserts.assertEqualsVariableElement(dto, entity);
    }

    // TODO test mapper visualisations

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }
}