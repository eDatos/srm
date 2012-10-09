package org.siemac.metamac.srm.core.concept.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
//@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
//@Transactional
public class ConceptsDo2DtoMapperTest {

    @Autowired
    private ConceptsDo2DtoMapper conceptsDo2DtoMapper;

    @Test
    public void testConceptSchemeMetamacDoToDto() {
        ConceptSchemeVersionMetamac entity = ConceptsMetamacDoMocks.mockConceptScheme();
        entity.setLifecycleMetadata(BaseDoMocks.mockLifeCycle());

        ConceptSchemeMetamacDto dto = conceptsDo2DtoMapper.conceptSchemeMetamacDoToDto(entity);
        ConceptsMetamacAsserts.assertEqualsConceptScheme(entity, dto);
    }

    @Test
    public void testConceptMetamacDoToDto() {
        ConceptMetamac entity = ConceptsMetamacDoMocks.mockConcept(new ConceptType());
        entity.setItemSchemeVersion(ConceptsMetamacDoMocks.mockConceptScheme());
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
}
