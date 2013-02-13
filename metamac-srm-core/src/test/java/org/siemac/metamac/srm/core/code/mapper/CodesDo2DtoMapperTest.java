package org.siemac.metamac.srm.core.code.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeHierarchyDto;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.code.enume.domain.VariableElementOperationTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacAsserts;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
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
public class CodesDo2DtoMapperTest extends SrmBaseTest {

    @Autowired
    private CodesDo2DtoMapper codesDo2DtoMapper;

    @Test
    public void testCodelistMetamacDoToDto() {
        CodelistVersionMetamac entity = mockCodelistWithAllMetadata();
        CodelistMetamacDto dto = codesDo2DtoMapper.codelistMetamacDoToDto(entity);
        CodesMetamacAsserts.assertEqualsCodelist(entity, dto);
    }

    @Test
    public void testCodeMetamacDoToDto() {
        CodeMetamac entity = mockCodeWithAllMetadata();

        CodeMetamacDto dto = codesDo2DtoMapper.codeMetamacDoToDto(entity);
        CodesMetamacAsserts.assertEqualsCode(entity, dto);
    }

    @Test
    public void testCodeMetamacDoListToItemHierarchyDtoList() {
        List<CodeMetamac> entities = new ArrayList<CodeMetamac>();

        // -> Code1
        CodeMetamac entity1 = mockCodeWithAllMetadata();
        entities.add(entity1);
        // -> -> Code 1A
        CodeMetamac entity1A = mockCodeWithAllMetadata();
        entity1.addChildren(entity1A);
        // -> -> Code 1B
        CodeMetamac entity1B = mockCodeWithAllMetadata();
        entity1.addChildren(entity1B);
        // -> Code2
        CodeMetamac entity2 = mockCodeWithAllMetadata();
        entities.add(entity2);
        // -> Code3
        CodeMetamac entity3 = mockCodeWithAllMetadata();
        entities.add(entity3);
        // -> -> Code 3A
        CodeMetamac entity3A = mockCodeWithAllMetadata();
        entity3.addChildren(entity3A);
        // -> -> Code 3AA
        CodeMetamac entity3AA = mockCodeWithAllMetadata();
        entity3A.addChildren(entity3AA);

        List<CodeHierarchyDto> dtos = codesDo2DtoMapper.codeMetamacDoListToCodeHierarchyDtoList(entities, null);

        // Validate
        assertEquals(3, dtos.size());
        assertEquals(entity1.getNameableArtefact().getCode(), dtos.get(0).getItem().getCode());
        assertTrue(dtos.get(0).getItem() instanceof CodeMetamacDto);
        assertEquals(2, dtos.get(0).getChildren().size());
        assertEquals(entity1A.getNameableArtefact().getCode(), dtos.get(0).getChildren().get(0).getItem().getCode());
        assertEquals(0, dtos.get(0).getChildren().get(0).getChildren().size());
        assertEquals(entity1B.getNameableArtefact().getCode(), dtos.get(0).getChildren().get(1).getItem().getCode());
        assertEquals(0, dtos.get(0).getChildren().get(1).getChildren().size());

        assertEquals(entity2.getNameableArtefact().getCode(), dtos.get(1).getItem().getCode());
        assertEquals(0, dtos.get(1).getChildren().size());

        assertEquals(entity3.getNameableArtefact().getCode(), dtos.get(2).getItem().getCode());
        assertEquals(1, dtos.get(2).getChildren().size());
        assertEquals(entity3A.getNameableArtefact().getCode(), dtos.get(2).getChildren().get(0).getItem().getCode());
        assertEquals(1, dtos.get(2).getChildren().get(0).getChildren().size());
        assertEquals(entity3AA.getNameableArtefact().getCode(), dtos.get(2).getChildren().get(0).getChildren().get(0).getItem().getCode());
        assertEquals(0, dtos.get(2).getChildren().get(0).getChildren().get(0).getChildren().size());
    }

    @Test
    public void testCodelistFamilyDoToDto() {
        CodelistFamily entity = mockCodelistFamilyWithAllMetadata();

        CodelistFamilyDto dto = codesDo2DtoMapper.codelistFamilyDoToDto(entity);
        CodesMetamacAsserts.assertEqualsCodelistFamily(entity, dto);
    }

    @Test
    public void testVariableFamilyDoToDto() {
        VariableFamily entity = mockVariableFamilyWithAllMetadata();

        VariableFamilyDto dto = codesDo2DtoMapper.variableFamilyDoToDto(entity);
        CodesMetamacAsserts.assertEqualsVariableFamily(entity, dto);
    }

    @Test
    public void testVariableDoToDto() {
        Variable entity = mockVariableWithAllMetadata();
        entity.setReplacedByVariable(mockVariableWithAllMetadata());
        entity.addReplaceToVariable(mockVariableWithAllMetadata());
        entity.addReplaceToVariable(mockVariableWithAllMetadata());

        VariableDto dto = codesDo2DtoMapper.variableDoToDto(entity);
        CodesMetamacAsserts.assertEqualsVariable(entity, dto);
    }

    @Test
    public void testVariableElementDoToDto() {
        VariableElement entity = mockVariableElementWithAllMetadata();
        entity.setReplacedByVariableElement(mockVariableElementWithAllMetadata());
        entity.addReplaceToVariableElement(mockVariableElementWithAllMetadata());
        entity.addReplaceToVariableElement(mockVariableElementWithAllMetadata());

        VariableElementDto dto = codesDo2DtoMapper.variableElementDoToDto(entity);
        CodesMetamacAsserts.assertEqualsVariableElement(entity, dto);
    }

    @Test
    public void testVariableElementOperationDoToDto() {
        VariableElementOperation entity = mockVariableElementOperationWithAllMetadata();

        VariableElementOperationDto dto = codesDo2DtoMapper.variableElementOperationDoToDto(entity);
        CodesMetamacAsserts.assertEqualsVariableElementOperation(entity, dto);
    }

    // TODO test mapper visualisations

    private CodelistVersionMetamac mockCodelistWithAllMetadata() {
        CodelistVersionMetamac entity = CodesMetamacDoMocks.mockCodelistFixedValues("agency01", "codelist01", "01.000");
        entity.setFamily(mockCodelistFamilyWithAllMetadata());
        entity.setVariable(mockVariableWithAllMetadata());
        entity.setReplacedByCodelist(CodesMetamacDoMocks.mockCodelistFixedValues("agency01", "codelist02", "01.000"));
        entity.addReplaceToCodelist(CodesMetamacDoMocks.mockCodelistFixedValues("agency01", "codelist03", "03.000"));
        entity.addReplaceToCodelist(CodesMetamacDoMocks.mockCodelistFixedValues("agency01", "codelist04", "04.000"));
        entity.setDefaultOrderVisualisation(mockCodelistOrderVisualisationWithAllMetadata());
        return entity;
    }

    private CodeMetamac mockCodeWithAllMetadata() {
        CodelistVersionMetamac codelist = CodesMetamacDoMocks.mockCodelistFixedValues("agency01", "codelist01", "01.000");
        CodeMetamac entity = CodesMetamacDoMocks.mockCodeFixedValues("code01", codelist, null);
        entity.setVariableElement(CodesMetamacDoMocks.mockVariableElementFixedValues("variableElement01"));
        return entity;
    }

    private CodelistFamily mockCodelistFamilyWithAllMetadata() {
        CodelistFamily entity = CodesMetamacDoMocks.mockCodelistFamilyFixedValues("codelistFamily01");
        return entity;
    }

    private VariableFamily mockVariableFamilyWithAllMetadata() {
        VariableFamily entity = CodesMetamacDoMocks.mockVariableFamilyFixedValues("variableFamily01");
        return entity;
    }

    private Variable mockVariableWithAllMetadata() {
        Variable entity = CodesMetamacDoMocks.mockVariableFixedValues("variable01");
        entity.addFamily(mockVariableFamilyWithAllMetadata());
        entity.addFamily(mockVariableFamilyWithAllMetadata());
        return entity;
    }

    private VariableElement mockVariableElementWithAllMetadata() {
        VariableElement entity = CodesMetamacDoMocks.mockVariableElementFixedValues("variableElement01");
        return entity;
    }

    private VariableElementOperation mockVariableElementOperationWithAllMetadata() {
        VariableElementOperation entity = new VariableElementOperation();
        entity.setCode(MetamacMocks.mockString(10));
        entity.setOperationType(VariableElementOperationTypeEnum.FUSION);
        entity.setVariable(mockVariableWithAllMetadata());
        entity.addSource(mockVariableElementWithAllMetadata());
        entity.addSource(mockVariableElementWithAllMetadata());
        entity.addTarget(mockVariableElementWithAllMetadata());

        entity.setCreatedBy("user" + MetamacMocks.mockString(10));
        entity.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        entity.setLastUpdatedBy("user" + MetamacMocks.mockString(10));
        entity.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        entity.setVersion(Long.valueOf(3));
        return entity;
    }

    private CodelistOrderVisualisation mockCodelistOrderVisualisationWithAllMetadata() {
        CodelistOrderVisualisation entity = CodesMetamacDoMocks.mockCodelistOrderVisualisationFixedValues("order01");
        return entity;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }
}
