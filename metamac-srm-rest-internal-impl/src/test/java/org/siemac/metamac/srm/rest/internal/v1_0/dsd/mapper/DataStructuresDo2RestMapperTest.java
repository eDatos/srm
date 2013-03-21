package org.siemac.metamac.srm.rest.internal.v1_0.dsd.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils.DataStructuresAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils.DataStructuresDoMocks.mockDataStructure;
import static org.siemac.metamac.srm.rest.internal.v1_0.dsd.utils.DataStructuresDoMocks.mockDataStructureWithComponents;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts.assertEqualsInternationalString;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ARTEFACT_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Attribute;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AttributeQualifierType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.dsd.DataStructuresDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-internal/applicationContext-test.xml"})
public class DataStructuresDo2RestMapperTest {

    @Autowired
    private DataStructuresDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToDataStructures() {

        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
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
        assertEquals(RestInternalConstants.KIND_DATA_STRUCTURES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/datastructures" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

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

        // Transform
        DataStructure target = do2RestInternalMapper.toDataStructure(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_DATA_STRUCTURE, target.getKind());
        assertEquals("urn:resourceID1:01.123", target.getUrn());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/datastructures/idAsMaintaineragencyID1/resourceID1/01.123";
        assertEquals(RestInternalConstants.KIND_DATA_STRUCTURE, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_DATA_STRUCTURES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/datastructures", target.getParentLink().getHref());
        assertEqualsInternationalString("es", "comment-resourceID1v01.123 en Español", "en", "comment-resourceID1v01.123 in English", target.getComment());
        assertEquals(Boolean.TRUE, target.isAutoOpen());
        assertEquals(3, target.getShowDecimals().intValue());
        assertEquals("urn:operation-resourceID1", target.getStatisticalOperation().getUrn());
        assertEquals("statisticalOperations#operation", target.getStatisticalOperation().getKind());
        assertEquals("operation-resourceID1", target.getStatisticalOperation().getId());
        assertNull(target.getStatisticalOperation().getTitle());
        // show decimals
        assertEquals(2, target.getShowDecimalsPrecisions().getTotal().intValue());
        assertEquals("urn:concept01", target.getShowDecimalsPrecisions().getShowDecimalPrecisions().get(0).getConcept().getURN());
        assertEquals(2, target.getShowDecimalsPrecisions().getShowDecimalPrecisions().get(0).getShowDecimals().intValue());
        assertEquals("urn:concept02", target.getShowDecimalsPrecisions().getShowDecimalPrecisions().get(1).getConcept().getURN());
        assertEquals(5, target.getShowDecimalsPrecisions().getShowDecimalPrecisions().get(1).getShowDecimals().intValue());
        // heading
        assertEquals(2, target.getHeading().getTotal().intValue());
        assertEquals("urn:dimension01", target.getHeading().getDimensions().get(0).getURN());
        assertEquals("urn:dimension02", target.getHeading().getDimensions().get(1).getURN());
        // stub
        assertEquals(3, target.getStub().getTotal().intValue());
        assertEquals("urn:dimension03", target.getStub().getDimensions().get(0).getURN());
        assertEquals("urn:dimension04", target.getStub().getDimensions().get(1).getURN());
        assertEquals("urn:dimension05", target.getStub().getDimensions().get(2).getURN());
        // attribute
        assertTrue(target.getDataStructureComponents().getAttributeList().getAttributesAndReportingYearStartDaies().get(0) instanceof Attribute);
        assertEquals(AttributeQualifierType.SPATIAL, ((Attribute) target.getDataStructureComponents().getAttributeList().getAttributesAndReportingYearStartDaies().get(0)).getType());
        // others
        assertEquals("replaceTo", target.getReplaceToVersion());
        assertEquals("replacedBy", target.getReplacedByVersion());
        assertEquals(ProcStatus.EXTERNALLY_PUBLISHED, target.getLifeCycle().getProcStatus());
        assertEqualsDate(new DateTime(2009, 9, 1, 1, 1, 1, 1), target.getLifeCycle().getProductionValidationDate());
        assertEquals("production-user", target.getLifeCycle().getProductionValidationUser());
        assertEqualsDate(new DateTime(2010, 10, 2, 1, 1, 1, 1), target.getLifeCycle().getDiffusionValidationDate());
        assertEquals("diffusion-user", target.getLifeCycle().getDiffusionValidationUser());
        assertEqualsDate(new DateTime(2011, 11, 3, 1, 1, 1, 1), target.getLifeCycle().getInternalPublicationDate());
        assertEquals("internal-publication-user", target.getLifeCycle().getInternalPublicationUser());
        assertEqualsDate(new DateTime(2012, 12, 4, 1, 1, 1, 1), target.getLifeCycle().getExternalPublicationDate());
        assertEquals("external-publication-user", target.getLifeCycle().getExternalPublicationUser());
        assertEquals(Boolean.FALSE, target.getLifeCycle().isIsExternalPublicationFailed());
        assertEqualsDate(new DateTime(2013, 8, 2, 1, 1, 1, 1), target.getLifeCycle().getExternalPublicationFailedDate());
        // TODO metadatos de visualización de codelist en dimensión (pte Core)
        assertEquals(null, target.getChildLinks());
    }

    @Test
    public void testToDataStructureImported() {

        DataStructureDefinitionVersionMetamac source = mockDataStructure("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");

        // Transform
        DataStructure target = do2RestInternalMapper.toDataStructure(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
    }

}