package org.siemac.metamac.srm.core.concept.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemHierarchyDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ConceptsDo2DtoMapperTest extends SrmBaseTest {

    @Autowired
    private ConceptsDo2DtoMapper conceptsDo2DtoMapper;

    @Test
    public void testConceptSchemeMetamacDoToDto() {
        ConceptSchemeVersionMetamac entity = ConceptsMetamacDoMocks.mockConceptSchemeFixedValues("agency01", "conceptScheme01", "01.000");

        ConceptSchemeMetamacDto dto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(entity);
        ConceptsMetamacAsserts.assertEqualsConceptScheme(entity, dto);
    }

    @Test
    public void testConceptMetamacDoToDto() {
        ConceptMetamac entity = mockConceptWithAllMetadata();

        ConceptMetamacDto dto = conceptsDo2DtoMapper.conceptMetamacDoToDto(entity);
        ConceptsMetamacAsserts.assertEqualsConcept(entity, dto);
    }

    @Test
    public void testConceptTypeDoToDto() {
        ConceptType entity = new ConceptType();
        entity.setIdentifier("conceptType-" + MetamacMocks.mockString(10));
        entity.setDescription(ConceptsMetamacDoMocks.mockInternationalString());
        ConceptTypeDto dto = conceptsDo2DtoMapper.conceptTypeDoToDto(entity);
        ConceptsMetamacAsserts.assertEqualsConceptType(entity, dto);
    }

    @Test
    public void testConceptMetamacDoListToItemHierarchyDtoList() {
        List<ConceptMetamac> entities = new ArrayList<ConceptMetamac>();

        // -> Concept1
        ConceptMetamac entity1 = mockConceptWithAllMetadata();
        entities.add(entity1);
        // -> -> Concept 1A
        ConceptMetamac entity1A = mockConceptWithAllMetadata();
        entity1.addChildren(entity1A);
        // -> -> Concept 1B
        ConceptMetamac entity1B = mockConceptWithAllMetadata();
        entity1.addChildren(entity1B);
        // -> Concept2
        ConceptMetamac entity2 = mockConceptWithAllMetadata();
        entities.add(entity2);
        // -> Concept3
        ConceptMetamac entity3 = mockConceptWithAllMetadata();
        entities.add(entity3);
        // -> -> Concept 3A
        ConceptMetamac entity3A = mockConceptWithAllMetadata();
        entity3.addChildren(entity3A);
        // -> -> Concept 3AA
        ConceptMetamac entity3AA = mockConceptWithAllMetadata();
        entity3A.addChildren(entity3AA);

        List<ItemHierarchyDto> dtos = conceptsDo2DtoMapper.conceptMetamacDoListToItemHierarchyDtoList(entities);

        // Validate
        assertEquals(3, dtos.size());
        assertEquals(entity1.getNameableArtefact().getCode(), dtos.get(0).getItem().getCode());
        assertTrue(dtos.get(0).getItem() instanceof ConceptMetamacDto);
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

    private ConceptMetamac mockConceptWithAllMetadata() {
        ConceptSchemeVersionMetamac conceptScheme = ConceptsMetamacDoMocks.mockConceptSchemeFixedValues("agency01", "conceptScheme01", "01.000");
        ConceptMetamac entity = ConceptsMetamacDoMocks.mockConceptFixedValues("concept01", conceptScheme, null);
        entity.setVariable(CodesMetamacDoMocks.mockVariableFixedValues("variable01"));
        return entity;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptsTest.xml";
    }
}
