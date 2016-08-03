package org.siemac.metamac.srm.core.dsd.mapper;

import java.util.Arrays;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacBasicDto;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionMetamacDoMocks;
import org.siemac.metamac.srm.core.dsd.serviceapi.utils.DataStructureDefinitionsMetamacAsserts;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts.MapperEnum;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DataStructureDefinitionMetamacDo2DtoMapperTest extends SrmBaseTest {

    @Autowired
    private DataStructureDefinitionDo2DtoMapper   dataStructureDefinitionDo2DtoMapper;

    @Autowired
    private OrganisationMetamacRepository         organisationMetamacRepository;

    @Autowired
    private ConceptMetamacRepository              conceptMetamacRepository;

    @Autowired
    private ConceptSchemeVersionMetamacRepository conceptSchemeVersionMetamacRepository;

    @Autowired
    private CodelistVersionMetamacRepository      codelistVersionMetamacRepository;

    @Autowired
    private ConceptsMetamacService                conceptsService;

    @Test
    public void testDataStructureDefinitionMetamacDoToDto() throws MetamacException {

        ServiceContext ctx = getServiceContextAdministrador();

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac, "op8");

        // Metamac
        dataStructureDefinitionVersionMetamac.setAutoOpen(true);
        dataStructureDefinitionVersionMetamac.setShowDecimals(2);

        Concept concept01 = conceptMetamacRepository.findByUrn(CONCEPT_SCHEME_1_V2_CONCEPT_2);
        Concept concept02 = conceptMetamacRepository.findByUrn(CONCEPT_SCHEME_1_V2_CONCEPT_3);
        CodelistVersion codelist = codelistVersionMetamacRepository.findByUrn(CODELIST_7_V1);
        dataStructureDefinitionVersionMetamac.addHeadingDimension(DataStructureDefinitionMetamacDoMocks.mockDimensionOrder(1,
                DataStructureDefinitionMetamacDoMocks.mockDimension(concept01, Arrays.asList(concept01, concept02), codelist)));
        dataStructureDefinitionVersionMetamac.addHeadingDimension(DataStructureDefinitionMetamacDoMocks.mockDimensionOrder(2, DataStructureDefinitionMetamacDoMocks.mockTimeDimension(concept01)));

        ConceptSchemeVersion conceptScheme = conceptSchemeVersionMetamacRepository.findByUrn(CONCEPT_SCHEME_3_V1);
        dataStructureDefinitionVersionMetamac.addStubDimension(DataStructureDefinitionMetamacDoMocks.mockDimensionOrder(1,
                DataStructureDefinitionMetamacDoMocks.mockMeasureDimension(concept01, Arrays.asList(concept01, concept02), conceptScheme)));

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        dataStructureDefinitionVersionMetamac.addShowDecimalsPrecision(DataStructureDefinitionMetamacDoMocks.mockMeasureDimensionPrecision(5,
                ConceptsMetamacDoMocks.mockConcept(conceptType, codelist, ConceptRoleEnum.ATTRIBUTE)));
        dataStructureDefinitionVersionMetamac.addShowDecimalsPrecision(DataStructureDefinitionMetamacDoMocks.mockMeasureDimensionPrecision(4,
                ConceptsMetamacDoMocks.mockConcept(conceptType, codelist, ConceptRoleEnum.ATTRIBUTE)));

        dataStructureDefinitionVersionMetamac.addDimensionVisualisationInfo(DataStructureDefinitionMetamacDoMocks.mockDimensionVisualizationInfo(dataStructureDefinitionVersionMetamac
                .getStubDimensions().iterator().next().getDimension()));

        DataStructureDefinitionMetamacDto dto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        DataStructureDefinitionsMetamacAsserts.assertEqualsDataStructureDefinition(dto, dataStructureDefinitionVersionMetamac, MapperEnum.DO2DTO);
    }

    @Test
    public void testDataStructureDefinitionMetamacDoToBasicDto() throws MetamacException {
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamacFixedValues("agency01", "dsd01",
                "01.000");

        DataStructureDefinitionMetamacBasicDto dto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToBasicDto(dataStructureDefinitionVersionMetamac);
        DataStructureDefinitionsMetamacAsserts.assertEqualsDataStructureDefinition(dataStructureDefinitionVersionMetamac, dto);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }
}
