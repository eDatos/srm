package org.siemac.metamac.srm.core.dsd.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.concept.serviceapi.utils.ConceptsMetamacDoMocks;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class DataStructureDefinitionMetamacDo2DtoMapperTest extends SrmBaseTest {

    @Autowired
    private DataStructureDefinitionDo2DtoMapper dataStructureDefinitionDo2DtoMapper;

    @Autowired
    private OrganisationMetamacRepository       organisationMetamacRepository;

    @Autowired
    private ConceptsMetamacService              conceptsService;

    @Test
    public void testDataStructureDefinitionMetamacDoToDto() throws MetamacException {

        ServiceContext ctx = getServiceContextAdministrador();

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        DataStructureDefinitionVersionMetamac dataStructureDefinitionVersionMetamac = DataStructureDefinitionMetamacDoMocks.mockDataStructureDefinitionVersionMetamac(organisationMetamac);

        // Metamac
        dataStructureDefinitionVersionMetamac.setAutoOpen(true);
        dataStructureDefinitionVersionMetamac.setShowDecimals(2);

        dataStructureDefinitionVersionMetamac.addHeadingDimension(DataStructureDefinitionMetamacDoMocks.mockDimensionOrder(1, DataStructureDefinitionMetamacDoMocks.mockDimension()));
        dataStructureDefinitionVersionMetamac.addHeadingDimension(DataStructureDefinitionMetamacDoMocks.mockDimensionOrder(2, DataStructureDefinitionMetamacDoMocks.mockTimeDimension()));

        dataStructureDefinitionVersionMetamac.addStubDimension(DataStructureDefinitionMetamacDoMocks.mockDimensionOrder(1, DataStructureDefinitionMetamacDoMocks.mockMeasureDimension()));

        ConceptType conceptType = conceptsService.retrieveConceptTypeByIdentifier(ctx, CONCEPT_TYPE_DIRECT);
        dataStructureDefinitionVersionMetamac.addShowDecimalsPrecision(DataStructureDefinitionMetamacDoMocks.mockMeasureDimensionPrecision(5, ConceptsMetamacDoMocks.mockConcept(conceptType)));
        dataStructureDefinitionVersionMetamac.addShowDecimalsPrecision(DataStructureDefinitionMetamacDoMocks.mockMeasureDimensionPrecision(4, ConceptsMetamacDoMocks.mockConcept(conceptType)));

        DataStructureDefinitionMetamacDto dto = dataStructureDefinitionDo2DtoMapper.dataStructureDefinitionMetamacDoToDto(dataStructureDefinitionVersionMetamac);
        DataStructureDefinitionsMetamacAsserts.assertEqualsDataStructureDefinition(dto, dataStructureDefinitionVersionMetamac);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmDsdTest.xml";
    }
}
