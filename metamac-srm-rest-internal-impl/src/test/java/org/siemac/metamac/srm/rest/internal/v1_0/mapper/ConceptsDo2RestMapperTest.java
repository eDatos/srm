package org.siemac.metamac.srm.rest.internal.v1_0.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertEqualsInternationalStringNotNull;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestAsserts.assertEqualsUrnsNotNull;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.CONCEPT_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ORDER_BY_CONCEPT_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.ORDER_BY_CONCEPT_SCHEME_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_CONCEPT_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.SrmRestTestConstants.QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
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
    public void testToConceptSchemes() {

        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
        String query = QUERY_CONCEPT_SCHEME_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_CONCEPT_SCHEME_ID_DESC;
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

        String baseLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

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
    public void testToConceptScheme() {

        String agencyID = "agencyID1";
        String resourceID = "resourceID1";
        String version = "01.123";
        ConceptSchemeVersionMetamac source = SrmCoreMocks.mockConceptSchemeWithConcepts(agencyID, resourceID, version);

        ConceptScheme target = do2RestInternalMapper.toConceptScheme(source);

        // Validate (only test metamac metadata)
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

        // TODO concepts (tipo SDMX)
    }

    @Test
    public void testToConcepts() {

        String agencyID = WILDCARD;
        String conceptSchemeID = WILDCARD;
        String version = WILDCARD;
        String query = QUERY_CONCEPT_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_CONCEPT_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        ConceptSchemeVersionMetamac conceptScheme1 = SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_1_CODE, CONCEPT_SCHEME_1_VERSION_1);
        ConceptSchemeVersionMetamac conceptScheme2 = SrmCoreMocks.mockConceptScheme(AGENCY_1, CONCEPT_SCHEME_2_CODE, CONCEPT_SCHEME_2_VERSION_1);
        List<ConceptMetamac> source = new ArrayList<ConceptMetamac>();
        source.add(SrmCoreMocks.mockConcept(CONCEPT_1_CODE, conceptScheme1, null));
        source.add(SrmCoreMocks.mockConcept(CONCEPT_2_CODE, conceptScheme1, null));
        source.add(SrmCoreMocks.mockConcept(CONCEPT_3_CODE, conceptScheme1, null));
        source.add(SrmCoreMocks.mockConcept(CONCEPT_1_CODE, conceptScheme2, null));

        Integer totalRows = source.size() * 5;
        PagedResult<ConceptMetamac> sources = new PagedResult<ConceptMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Concepts target = do2RestInternalMapper.toConcepts(sources, agencyID, conceptSchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getKind());

        String baseLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes" + "/" + agencyID + "/" + conceptSchemeID + "/concepts?query=" + query + "&orderBy=" + orderBy;
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getConcepts().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getConcepts().get(i));
        }
    }

    @Test
    public void testToConcept() {

        String agencyID = "agencyID1";
        String schemeID = "resourceID1";
        String version = "01.123";
        ConceptSchemeVersionMetamac conceptScheme = SrmCoreMocks.mockConceptScheme(agencyID, schemeID, version);
        ConceptMetamac parent = SrmCoreMocks.mockConcept("conceptParent1", conceptScheme, null);
        ConceptMetamac source = SrmCoreMocks.mockConceptWithConceptRelations("concept2", conceptScheme, parent);

        Concept target = do2RestInternalMapper.toConcept(source);

        // Validate (only test metamac metadata)
        assertEquals(RestInternalConstants.KIND_CONCEPT, target.getKind());
        String parentLink = "http://data.istac.es/apis/srm/v1.0/conceptschemes" + "/" + source.getItemSchemeVersion().getMaintainableArtefact().getMaintainer().getIdAsMaintainer() + "/" + schemeID
                + "/" + version + "/concepts";
        String selfLink = parentLink + "/" + source.getNameableArtefact().getCode();
        assertEquals(selfLink, target.getSelfLink());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getParentResource().getKind());
        assertEquals(parentLink, target.getParentResource().getSelfLink());
        assertNull(target.getChildren());
        
        assertEqualsInternationalStringNotNull(source.getPluralName(), target.getPluralName());
        assertEqualsInternationalStringNotNull(source.getAcronym(), target.getAcronym());
        assertEqualsInternationalStringNotNull(source.getDescriptionSource(), target.getDescriptionSource());
        assertEqualsInternationalStringNotNull(source.getContext(), target.getContext());
        assertEqualsInternationalStringNotNull(source.getDocMethod(), target.getDocMethod());
        assertEqualsInternationalStringNotNull(source.getDerivation(), target.getDerivation());
        assertEqualsInternationalStringNotNull(source.getLegalActs(), target.getLegalActs());

        assertEquals(source.getType().getIdentifier(), target.getType().getId());
        assertEqualsInternationalStringNotNull(source.getType().getDescription(), target.getType().getTitle());
        
        assertEqualsUrnsNotNull(source.getRoleConcepts(), target.getRoles());
        assertEqualsUrnsNotNull(source.getRelatedConcepts(), target.getRelatedConcepts());
        assertEquals(source.getConceptExtends().getNameableArtefact().getUrn(), target.getExtends());
    }
}