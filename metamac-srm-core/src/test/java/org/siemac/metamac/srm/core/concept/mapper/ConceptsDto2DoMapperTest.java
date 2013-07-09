package org.siemac.metamac.srm.core.concept.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.serviceapi.utils.CodesMetamacDtoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacAsserts;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDtoMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.common.error.ServiceExceptionType;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
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
    public void testConceptMetamacRateDtoToDo() throws MetamacException {
        ConceptMetamacDto dto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.ENUMERATION);
        dto.setVariable(CodesMetamacDtoMocks.mockVariableRelatedResourceDto("VARIABLE01", VARIABLE_1));
        // transform
        ConceptMetamac entity = conceptsDto2DoMapper.conceptDtoToDo(dto);
        ConceptsMetamacAsserts.assertEqualsConcept(dto, entity);
    }

    @Test
    public void testConceptMetamacChangeRateDtoToDo() throws MetamacException {
        ConceptMetamacDto dto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.ENUMERATION);
        // quantity
        RelatedResourceDto unitCode = CodesMetamacDtoMocks.mockCodeRelatedResourceDto("CODE01", CODELIST_7_V2_CODE_1);
        RelatedResourceDto numerator = ConceptsMetamacDtoMocks.mockConceptRelatedResourceDto("CONCEPT0201", CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        RelatedResourceDto denominator = CodesMetamacDtoMocks.mockCodeRelatedResourceDto("CONCEPT03", CONCEPT_SCHEME_1_V2_CONCEPT_3);
        RelatedResourceDto baseQuantity = ConceptsMetamacDtoMocks.mockConceptRelatedResourceDto("CONCEPT04", CONCEPT_SCHEME_1_V2_CONCEPT_4);
        dto.setQuantity(ConceptsMetamacDtoMocks.mockQuantityDtoTypeChangeRate(unitCode, numerator, denominator, baseQuantity));

        // transform
        ConceptMetamac entity = conceptsDto2DoMapper.conceptDtoToDo(dto);
        assertEquals(QuantityTypeEnum.CHANGE_RATE, entity.getQuantity().getQuantityType());
        ConceptsMetamacAsserts.assertEqualsConcept(dto, entity);
    }

    @Test
    public void testConceptMetamacIndexDtoToDo() throws MetamacException {
        ConceptMetamacDto dto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.ENUMERATION);
        // quantity
        RelatedResourceDto unitCode = CodesMetamacDtoMocks.mockCodeRelatedResourceDto("CODE01", CODELIST_7_V2_CODE_1);
        RelatedResourceDto numerator = ConceptsMetamacDtoMocks.mockConceptRelatedResourceDto("CONCEPT0201", CONCEPT_SCHEME_1_V2_CONCEPT_2_1);
        RelatedResourceDto denominator = CodesMetamacDtoMocks.mockCodeRelatedResourceDto("CONCEPT03", CONCEPT_SCHEME_1_V2_CONCEPT_3);
        RelatedResourceDto baseLocation = CodesMetamacDtoMocks.mockCodeRelatedResourceDto("CODE01", CODELIST_7_V2_CODE_1);
        dto.setQuantity(ConceptsMetamacDtoMocks.mockQuantityDtoTypeIndex(unitCode, numerator, denominator, baseLocation));
        dto.getQuantity().setBaseTime("2010");
        dto.getQuantity().setBaseValue(33);

        // transform
        ConceptMetamac entity = conceptsDto2DoMapper.conceptDtoToDo(dto);
        assertNotNull(entity.getQuantity().getBaseLocation());
        assertEquals(QuantityTypeEnum.INDEX, entity.getQuantity().getQuantityType());
        ConceptsMetamacAsserts.assertEqualsConcept(dto, entity);
    }

    @Test
    public void testConceptMetamacDtoToDoErrorTranslations() throws MetamacException {
        ConceptMetamacDto dto = ConceptsMetamacDtoMocks.mockConceptDto(RepresentationTypeEnum.ENUMERATION);
        dto.setLegalActs(new InternationalStringDto());
        LocalisedStringDto localeEn = new LocalisedStringDto();
        localeEn.setLocale("xx");
        localeEn.setLabel("Label");
        dto.getLegalActs().addText(localeEn);
        try {
            conceptsDto2DoMapper.conceptDtoToDo(dto);
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.CONCEPT_LEGAL_ACTS, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmConceptsTest.xml";
    }
}