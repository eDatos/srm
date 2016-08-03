package org.siemac.metamac.srm.rest.external.v1_0.dsd.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.srm.rest.external.v1_0.dsd.utils.DataStructuresAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.external.v1_0.dsd.utils.DataStructuresDoMocks.mockDataStructure;
import static org.siemac.metamac.srm.rest.external.v1_0.dsd.utils.DataStructuresDoMocks.mockDataStructureWithComponents;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.Asserts.assertEqualsInternationalString;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.Asserts.assertListContainsItemResource;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ARTEFACT_1_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ARTEFACT_2_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ARTEFACT_3_CODE;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.external.v1_0.utils.RestTestConstants.VERSION_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AttributeBase;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AttributeQualifierType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AttributeUsageStatusType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DimensionBase;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DimensionVisualisation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Group;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.dsd.DataStructuresDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.Dimension;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionDescriptor;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-external/applicationContext-test.xml"})
public class DataStructuresDo2RestMapperTest {

    @Autowired
    private DataStructuresDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToDataStructures() {

        String agencyID = WILDCARD_ALL;
        String resourceID = WILDCARD_ALL;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<DataStructureDefinitionVersionMetamac> source = new ArrayList<DataStructureDefinitionVersionMetamac>();
        source.add(mockDataStructure(AGENCY_1, ARTEFACT_1_CODE, VERSION_1));
        source.add(mockDataStructure(AGENCY_1, ARTEFACT_2_CODE, VERSION_1));
        source.add(mockDataStructure(AGENCY_1, ARTEFACT_2_CODE, VERSION_2));
        source.add(mockDataStructure(AGENCY_2, ARTEFACT_3_CODE, VERSION_1));

        Integer totalRows = source.size() * 5;
        PagedResult<DataStructureDefinitionVersionMetamac> sources = new PagedResult<DataStructureDefinitionVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        DataStructures target = do2RestInternalMapper.toDataStructures(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources/v1.0/datastructures" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getDataStructures().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getDataStructures().get(i));
        }
    }

    @Test
    public void testToDataStructure() {

        DataStructureDefinitionVersionMetamac source = mockDataStructureWithComponents("agencyID1", "resourceID1", "01.123");
        source.getStatisticalOperation().setCodeNested("codeNested");

        // Transform
        DataStructure target = do2RestInternalMapper.toDataStructure(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURE, target.getKind());
        assertEquals("resourceID1", target.getId());
        assertEquals("01.123", target.getVersion());
        assertEquals("urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=agencyID1:resourceID1(01.123)", target.getUrn());
        assertEquals("agencyID1", target.getAgencyID());
        assertEquals(null, target.getUrnProvider());
        String selfLink = "http://data.istac.es/apis/structural-resources/v1.0/datastructures/agencyID1/resourceID1/01.123";
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURE, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_DATA_STRUCTURES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources/v1.0/datastructures", target.getParentLink().getHref());
        assertNull(target.getManagementAppLink());
        assertEqualsInternationalString("es", "name-resourceID1v01.123 en Español", "en", "name-resourceID1v01.123 in English", target.getName());
        assertEqualsInternationalString("es", "description-resourceID1v01.123 en Español", "en", "description-resourceID1v01.123 in English", target.getDescription());

        assertEquals(true, target.isIsFinal());
        assertEquals(false, target.isIsExternalReference());
        assertEquals("serviceUrl-resourceID1", target.getServiceUrl());
        assertEquals("structureUrl-resourceID1", target.getStructureUrl());

        assertEquals("DimensionDescriptor", target.getDataStructureComponents().getDimensions().getId());
        assertEquals(2, target.getDataStructureComponents().getDimensions().getDimensions().size());
        {
            org.siemac.metamac.rest.structural_resources.v1_0.domain.Dimension dimension = (org.siemac.metamac.rest.structural_resources.v1_0.domain.Dimension) getDimension(target, "dimension01");
            assertEquals("dimension01", dimension.getId());
            assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Codelist=agency01:codelist01(01.000)", dimension.getLocalRepresentation().getEnumerationCodelist().getUrn());
            assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agency01:conceptScheme01(01.000).concept01", dimension.getConceptIdentity().getUrn());

            assertEquals(2, dimension.getRoleConcepts().getRoles().size());
            assertListContainsItemResource("concept02a", dimension.getRoleConcepts().getRoles());
            assertListContainsItemResource("concept02b", dimension.getRoleConcepts().getRoles());
        }
        {
            org.siemac.metamac.rest.structural_resources.v1_0.domain.TimeDimension dimension = (org.siemac.metamac.rest.structural_resources.v1_0.domain.TimeDimension) getDimension(target,
                    "TIME_PERIOD");
            assertEquals("TIME_PERIOD", dimension.getId());
            assertEquals(DataType.REPORTING_YEAR, dimension.getLocalRepresentation().getTextFormat().getTextType());
        }

        assertEquals(1, target.getDataStructureComponents().getGroups().getGroups().size());
        {
            Group group = target.getDataStructureComponents().getGroups().getGroups().get(0);
            assertEquals(1, group.getDimensions().getDimensions().size());
            assertTrue(group.getDimensions().getDimensions().contains("dimension01"));
        }

        assertEquals("MeasureDescriptor", target.getDataStructureComponents().getMeasure().getId());
        assertEquals("OBS_VALUE", target.getDataStructureComponents().getMeasure().getPrimaryMeasure().getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agency01:conceptScheme01(01.000).concept01", target.getDataStructureComponents().getMeasure().getPrimaryMeasure()
                .getConceptIdentity().getUrn());

        assertEquals("AttributeDescriptor", target.getDataStructureComponents().getAttributes().getId());
        assertEquals(1, target.getDataStructureComponents().getAttributes().getAttributes().size());
        {
            Attribute attribute = (Attribute) getAttribute(target, "dataAttribute01");
            assertEquals("dataAttribute01", attribute.getId());
            assertEquals(AttributeUsageStatusType.CONDITIONAL, attribute.getAssignmentStatus());
            assertEquals(AttributeQualifierType.SPATIAL, attribute.getType());
            assertEquals("codelist01", attribute.getLocalRepresentation().getEnumerationCodelist().getId());
            assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agency01:conceptScheme01(01.000).concept01", attribute.getConceptIdentity().getUrn());

            assertEquals(2, attribute.getRoleConcepts().getRoles().size());
            assertListContainsItemResource("concept02a", attribute.getRoleConcepts().getRoles());
            assertListContainsItemResource("concept02b", attribute.getRoleConcepts().getRoles());
        }

        assertEquals(Boolean.TRUE, target.isAutoOpen());
        assertEquals(3, target.getShowDecimals().intValue());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=operation-resourceID1", target.getStatisticalOperation().getUrn());
        assertEquals("statisticalOperations#operation", target.getStatisticalOperation().getKind());
        assertEquals("operation-resourceID1", target.getStatisticalOperation().getId());
        assertEquals(source.getStatisticalOperation().getCodeNested(), target.getStatisticalOperation().getNestedId());
        assertNull(target.getStatisticalOperation().getName());
        // show decimals
        assertEquals(2, target.getShowDecimalsPrecisions().getTotal().intValue());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agency01:conceptScheme01(01.000).concept01", target.getShowDecimalsPrecisions().getShowDecimalPrecisions().get(0).getConcept()
                .getUrn());
        assertEquals(2, target.getShowDecimalsPrecisions().getShowDecimalPrecisions().get(0).getShowDecimals());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agency01:conceptScheme01(01.000).concept02", target.getShowDecimalsPrecisions().getShowDecimalPrecisions().get(1).getConcept()
                .getUrn());
        assertEquals(5, target.getShowDecimalsPrecisions().getShowDecimalPrecisions().get(1).getShowDecimals());
        // heading
        assertEquals(2, target.getHeading().getTotal().intValue());
        assertEquals("dimension01", target.getHeading().getDimensions().get(0));
        assertEquals("dimension02", target.getHeading().getDimensions().get(1));
        // stub
        assertEquals(3, target.getStub().getTotal().intValue());
        assertEquals("dimension03", target.getStub().getDimensions().get(0));
        assertEquals("dimension04", target.getStub().getDimensions().get(1));
        assertEquals("dimension05", target.getStub().getDimensions().get(2));
        // attribute
        assertTrue(target.getDataStructureComponents().getAttributes().getAttributes().get(0) instanceof Attribute);
        assertEquals(AttributeQualifierType.SPATIAL, ((Attribute) target.getDataStructureComponents().getAttributes().getAttributes().get(0)).getType());
        // visualisation
        assertEquals(3, target.getDimensionVisualisations().getTotal().intValue());
        {
            DimensionVisualisation dimensionVisualisation = target.getDimensionVisualisations().getDimensionVisualisations().get(0);
            assertEquals("dimension01", dimensionVisualisation.getDimension());
        }
        {
            DimensionVisualisation dimensionVisualisation = target.getDimensionVisualisations().getDimensionVisualisations().get(1);
            assertEquals("dimension02", dimensionVisualisation.getDimension());
        }
        {
            DimensionVisualisation dimensionVisualisation = target.getDimensionVisualisations().getDimensionVisualisations().get(2);
            assertEquals("dimension05", dimensionVisualisation.getDimension());
        }

        // others
        // replaceX no tested, because it is necessary a repository access
        // assertEquals("replaceTo", target.getReplaceToVersion());
        // assertEquals("replacedBy", target.getReplacedByVersion());
        assertEqualsDate(new DateTime(2012, 12, 4, 1, 1, 1, 1), target.getLifeCycle().getLastUpdatedDate());

        assertEqualsDate(new DateTime(2012, 11, 5, 10, 12, 13, 14), target.getValidFrom());
        assertEqualsDate(new DateTime(2013, 10, 4, 10, 12, 13, 14), target.getValidTo());

        assertEquals(null, target.getChildLinks());
    }
    @Test
    public void testToDataStructureImported() {

        DataStructureDefinitionVersionMetamac source = mockDataStructure("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUrnProvider("urnProvider");
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");

        // Transform
        DataStructure target = do2RestInternalMapper.toDataStructure(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=agencyID1:resourceID1(01.123)", target.getUrn());
        assertEquals("urnProvider", target.getUrnProvider());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToDataStructureWithDimensionsSpatial() {

        DataStructureDefinitionVersionMetamac source = mockDataStructure("agencyID1", "resourceID1", "01.123");
        ComponentList dimensionDescriptor = new DimensionDescriptor();
        source.addGrouping(dimensionDescriptor);

        Dimension dimension1 = new Dimension();
        dimension1.setCode("dimension01");
        dimension1.setSpecialDimensionType(null);
        dimensionDescriptor.addComponent(dimension1);

        Dimension dimension2 = new Dimension();
        dimension2.setCode("dimension02");
        dimension2.setSpecialDimensionType(SpecialDimensionTypeEnum.SPATIAL);
        dimensionDescriptor.addComponent(dimension2);

        Dimension dimension3 = new Dimension();
        dimension3.setCode("dimension03");
        dimension3.setSpecialDimensionType(SpecialDimensionTypeEnum.SPATIAL);
        dimensionDescriptor.addComponent(dimension3);

        // Transform
        DataStructure target = do2RestInternalMapper.toDataStructure(source);
        assertEquals(3, target.getDataStructureComponents().getDimensions().getDimensions().size());
        assertEquals(null, ((org.siemac.metamac.rest.structural_resources.v1_0.domain.Dimension) getDimension(target, "dimension01")).isIsSpatial());
        assertEquals(true, ((org.siemac.metamac.rest.structural_resources.v1_0.domain.Dimension) getDimension(target, "dimension02")).isIsSpatial());
        assertEquals(true, ((org.siemac.metamac.rest.structural_resources.v1_0.domain.Dimension) getDimension(target, "dimension03")).isIsSpatial());
    }

    private org.siemac.metamac.rest.structural_resources.v1_0.domain.DimensionBase getDimension(DataStructure target, String dimensionId) {
        for (DimensionBase dimension : target.getDataStructureComponents().getDimensions().getDimensions()) {
            if (dimension.getId().equals(dimensionId)) {
                return dimension;
            }
        }
        fail("dimension not found");
        return null;
    }

    private org.siemac.metamac.rest.structural_resources.v1_0.domain.AttributeBase getAttribute(DataStructure target, String attributeId) {
        for (AttributeBase attribute : target.getDataStructureComponents().getAttributes().getAttributes()) {
            if (attribute.getId().equals(attributeId)) {
                return attribute;
            }
        }
        fail("attribute not found");
        return null;
    }

}