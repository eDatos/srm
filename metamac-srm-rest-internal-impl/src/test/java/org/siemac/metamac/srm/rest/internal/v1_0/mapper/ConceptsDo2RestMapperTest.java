package org.siemac.metamac.srm.rest.internal.v1_0.mapper;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ORDER_BY_CONCEPT_SCHEME_ID;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmCoreMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-internal/applicationContext-test.xml"})
public class ConceptsDo2RestMapperTest {

    @Autowired
    private ConceptsDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testConceptSchemes() {
        
        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
        String query = QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_CONCEPT_SCHEME_ID;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<ConceptSchemeVersionMetamac> source = new ArrayList<ConceptSchemeVersionMetamac>();
        source.add(SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1));
        source.add(SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_2_CODE, CONCEPT_SCHEME_2_VERSION_1));
        source.add(SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_2_CODE, CONCEPT_SCHEME_2_VERSION_2));
        source.add(SrmCoreMocks.mockConceptScheme(AGENCY_2, CONCEPT_SCHEME_3_CODE, CONCEPT_SCHEME_3_VERSION_1));

        Integer totalRows = source.size() * 5;
        PagedResult<ConceptSchemeVersionMetamac> sources = new PagedResult<ConceptSchemeVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        ConceptSchemes target = do2RestInternalMapper.toConceptSchemes(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, target.getKind());

        String parentLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes";
        String baseLink = parentLink + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + ORDER_BY_CONCEPT_SCHEME_ID;

        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getConceptSchemes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getConceptSchemes().get(i));
        }
    }
    
    @Test
    public void testConceptScheme() {
        
        String agencyID = "agencyID1";
        String resourceID = "resourceID1";
        String version = "01.123";
        ConceptSchemeVersionMetamac source = SrmCoreMocks.mockConceptScheme(agencyID, resourceID, version);

        // only test metamac metadata
        ConceptScheme target = do2RestInternalMapper.toConceptScheme(source);

        // Validate
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes";
        String selfLink = parentLink + "/" + source.getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + resourceID + "/" + version;
        assertEquals(selfLink, target.getSelfLink());
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, target.getParent().getKind());
        assertEquals(parentLink, target.getParent().getSelfLink());
        assertEquals(source.getType().toString(), target.getType().toString());
        MetamacAsserts.assertEqualsNullability(source.getRelatedOperation(), target.getRelatedOperation());
        if (source.getRelatedOperation() != null) {
            assertEquals(source.getRelatedOperation().getUrn(), target.getRelatedOperation().getUrn());
        }
        assertEquals(source.getMaintainableArtefact().getReplaceTo(), target.getReplaceTo());
        assertEquals(source.getMaintainableArtefact().getReplacedBy(), target.getReplacedBy());
        assertEquals(BigInteger.ONE, target.getChildren().getTotal());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getChildren().getChildren().get(0).getKind());
        assertEquals(selfLink + "/concepts", target.getChildren().getChildren().get(0).getSelfLink());

        // TODO concepts
    }

    /**
     * TODO concept
     * <xs:sequence>
     * <xs:element name="pluralName" type="common:InternationalString" minOccurs="0" />
     * <xs:element name="acronym" type="common:InternationalString" minOccurs="0" />
     * <xs:element name="descriptionSource" type="common:InternationalString" minOccurs="0" />
     * <xs:element name="context" type="common:InternationalString" minOccurs="0" />
     * <xs:element name="docMethod" type="common:InternationalString" minOccurs="0" />
     * <xs:element name="type" type="common:Item" minOccurs="0" />
     * <xs:element name="derivation" type="common:InternationalString" minOccurs="0" />
     * <xs:element name="legalActs" type="common:InternationalString" minOccurs="0" />
     * <xs:element name="roles" type="tns:Urns" minOccurs="0" />
     * <xs:element name="extends" type="xs:string" minOccurs="0" />
     * <xs:element name="relatedConcepts" type="tns:Urns" minOccurs="0" />
     * </xs:sequence>
     * <xs:attribute name="kind" type="xs:string" use="required" />
     */
}
