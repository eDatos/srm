package org.siemac.metamac.srm.core.concept.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ConceptsDto2DoMapperTest extends SrmBaseTest {

    @Autowired
    private org.siemac.metamac.srm.core.concept.mapper.ConceptsDto2DoMapper conceptsDto2DoMapper;

    @Test
    public void testConceptSchemeMetamacDtoToDo() throws MetamacException {
        ConceptSchemeMetamacDto dto = ConceptsMetamacDtoMocks.mockConceptSchemeDtoOperationType(AGENCY_ROOT_1_V1_CODE, AGENCY_ROOT_1_V1);
        ConceptSchemeVersionMetamac entity = conceptsDto2DoMapper.conceptSchemeDtoToDo(dto);
        ConceptsMetamacAsserts.assertEqualsConceptScheme(dto, entity);
    }

    @Test
    public void testConceptMetamacDoToDto() throws MetamacException {
        ConceptMetamacDto dto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.ENUMERATION);
        dto.setVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE01", VARIABLE_1));

        ConceptMetamac entity = conceptsDto2DoMapper.conceptDtoToDo(dto);
        ConceptsMetamacAsserts.assertEqualsConcept(dto, entity);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptsTest.xml";
    }
}