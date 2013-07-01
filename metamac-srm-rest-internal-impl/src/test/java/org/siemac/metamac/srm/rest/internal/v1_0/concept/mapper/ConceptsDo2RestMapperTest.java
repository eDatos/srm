package org.siemac.metamac.srm.rest.internal.v1_0.concept.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsAsserts.assertEqualsRelatedConcepts;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsAsserts.assertEqualsRoleConcepts;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsDoMocks.mockConcept;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsDoMocks.mockConceptScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsDoMocks.mockConceptSchemeWithConcepts;
import static org.siemac.metamac.srm.rest.internal.v1_0.concept.utils.ConceptsDoMocks.mockConceptWithConceptRelations;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts.assertEqualsInternationalString;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.VERSION_2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityChangeRate;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityUnitSymbolPosition;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
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
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<ConceptSchemeVersionMetamac> source = new ArrayList<ConceptSchemeVersionMetamac>();
        source.add(mockConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1));
        source.add(mockConceptScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1));
        source.add(mockConceptScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2));
        source.add(mockConceptScheme(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1));

        Integer totalRows = source.size() * 5;
        PagedResult<ConceptSchemeVersionMetamac> sources = new PagedResult<ConceptSchemeVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        ConceptSchemes target = do2RestInternalMapper.toConceptSchemes(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/conceptschemes" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
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
    public void testToConceptScheme() throws Exception {

        ConceptSchemeVersionMetamac source = mockConceptSchemeWithConcepts("agencyID1", "resourceID1", "01.123");

        // Transform
        ConceptScheme target = do2RestInternalMapper.toConceptScheme(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, target.getKind());
        assertEquals("resourceID1", target.getId());
        assertEquals("01.123", target.getVersion());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=agencyID1:resourceID1(01.123)", target.getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=agencyID1:resourceID1(01.123)", target.getUrnInternal());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/conceptschemes/agencyID1/resourceID1/01.123";
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/conceptSchemes/conceptScheme;id=agencyID1:resourceID1(01.123)", target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_CONCEPT_SCHEMES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/conceptschemes", target.getParentLink().getHref());
        assertEquals(ConceptSchemeType.OPERATION, target.getType());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.statisticaloperations.Operation=operation-resourceID1", target.getStatisticalOperation().getUrn());
        assertEquals("statisticalOperations#operation", target.getStatisticalOperation().getKind());
        assertEquals("operation-resourceID1", target.getStatisticalOperation().getId());
        assertNull(target.getStatisticalOperation().getName());
        assertEqualsInternationalString("es", "comment-resourceID1v01.123 en Español", "en", "comment-resourceID1v01.123 in English", target.getComment());
        // replaceX no tested, because it is necessary a repository access
        // assertEquals("replaceTo", target.getReplaceToVersion());
        // assertEquals("replacedBy", target.getReplacedByVersion());
        assertEquals(ProcStatus.EXTERNALLY_PUBLISHED, target.getLifeCycle().getProcStatus());
        assertEqualsDate(new DateTime(2009, 9, 1, 1, 1, 1, 1), target.getLifeCycle().getProductionValidationDate());
        assertEquals("production-user", target.getLifeCycle().getProductionValidationUser());
        assertEqualsDate(new DateTime(2010, 10, 2, 1, 1, 1, 1), target.getLifeCycle().getDiffusionValidationDate());
        assertEquals("diffusion-user", target.getLifeCycle().getDiffusionValidationUser());
        assertEqualsDate(new DateTime(2011, 11, 3, 1, 1, 1, 1), target.getLifeCycle().getInternalPublicationDate());
        assertEquals("internal-publication-user", target.getLifeCycle().getInternalPublicationUser());
        assertEqualsDate(new DateTime(2012, 12, 4, 1, 1, 1, 1), target.getLifeCycle().getExternalPublicationDate());
        assertEquals("external-publication-user", target.getLifeCycle().getExternalPublicationUser());
        assertEqualsDate(new DateTime(2013, 10, 1, 10, 12, 13, 14), target.getCreatedDate());

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/concepts", target.getChildLinks().getChildLinks().get(0).getHref());
    }

    @Test
    public void testToConceptSchemeImported() throws Exception {

        ConceptSchemeVersionMetamac source = mockConceptSchemeWithConcepts("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");
        source.getMaintainableArtefact().setUrnProvider("urnProvider");

        // Transform
        ConceptScheme target = do2RestInternalMapper.toConceptScheme(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.ConceptScheme=agencyID1:resourceID1(01.123)", target.getUrnInternal());
        assertEquals("urnProvider", target.getUrn());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToConcepts() {

        String agencyID = WILDCARD;
        String conceptSchemeID = WILDCARD;
        String version = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        ConceptSchemeVersionMetamac conceptScheme1 = mockConceptScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1);
        ConceptSchemeVersionMetamac conceptScheme2 = mockConceptScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1);
        List<ConceptMetamac> source = new ArrayList<ConceptMetamac>();
        source.add(mockConcept(ITEM_1_CODE, conceptScheme1, null));
        source.add(mockConcept(ITEM_2_CODE, conceptScheme1, null));
        source.add(mockConcept(ITEM_3_CODE, conceptScheme1, null));
        source.add(mockConcept(ITEM_1_CODE, conceptScheme2, null));

        Integer totalRows = source.size() * 5;
        PagedResult<ConceptMetamac> sources = new PagedResult<ConceptMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Concepts target = do2RestInternalMapper.toConcepts(sources, agencyID, conceptSchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/conceptschemes" + "/" + agencyID + "/" + conceptSchemeID + "/" + version + "/concepts?query=" + query
                + "&orderBy=" + orderBy;
        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
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
        ConceptSchemeVersionMetamac conceptScheme = mockConceptScheme("agencyID1", "resourceID1", "01.123");
        ConceptMetamac parent = mockConcept("conceptParent1", conceptScheme, null);
        ConceptMetamac source = mockConceptWithConceptRelations("concept2", conceptScheme, parent);

        // Transform
        Concept target = do2RestInternalMapper.toConcept(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_CONCEPT, target.getKind());
        assertEquals("concept2", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agencyID1:resourceID1(01.123).concept2", target.getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agencyID1:resourceID1(01.123).concept2", target.getUrnInternal());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/conceptschemes/agencyID1/resourceID1/01.123/concepts";
        String selfLink = parentLink + "/concept2";
        assertEquals(RestInternalConstants.KIND_CONCEPT, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_CONCEPTS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/conceptSchemes/conceptScheme;id=agencyID1:resourceID1(01.123)/concept;id=concept2", target.getManagementAppLink());

        assertEqualsInternationalString("es", "comment-concept2 en Español", "en", "comment-concept2 in English", target.getComment());
        assertEqualsInternationalString("es", "pluralName-concept2 en Español", "en", "pluralName-concept2 in English", target.getPluralName());
        assertEqualsInternationalString("es", "acronym-concept2 en Español", "en", "acronym-concept2 in English", target.getAcronym());
        assertEqualsInternationalString("es", "descriptionSource-concept2 en Español", "en", "descriptionSource-concept2 in English", target.getDescriptionSource());
        assertEqualsInternationalString("es", "context-concept2 en Español", "en", "context-concept2 in English", target.getContext());
        assertEqualsInternationalString("es", "docMethod-concept2 en Español", "en", "docMethod-concept2 in English", target.getDocMethod());
        assertEqualsInternationalString("es", "derivation-concept2 en Español", "en", "derivation-concept2 in English", target.getDerivation());
        assertEqualsInternationalString("es", "legalActs-concept2 en Español", "en", "legalActs-concept2 in English", target.getLegalActs());

        assertEquals("variable1", target.getVariable().getId());
        assertEquals("urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=variable1", target.getVariable().getUrn());
        assertEqualsInternationalString("es", "name-variable1 en Español", "en", "name-variable1 in English", target.getVariable().getName());
        assertEquals(RestInternalConstants.KIND_VARIABLE, target.getVariable().getSelfLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/variables/variable1", target.getVariable().getSelfLink().getHref());

        assertEquals("conceptType1", target.getType().getId());
        assertEqualsInternationalString("es", "description-conceptType1 en Español", "en", "description-conceptType1 in English", target.getType().getName());
        assertEquals("conceptParent1", target.getParent().getRef().getId());
        assertEqualsRoleConcepts(source.getRoleConcepts(), target.getRoles());
        assertEqualsRelatedConcepts(source.getRelatedConcepts(), target.getRelatedConcepts());
        assertEqualsResource(source.getConceptExtends(), target.getExtends());

        assertTrue(target.getQuantity() instanceof QuantityChangeRate);
        QuantityChangeRate quantityChangeRate = (QuantityChangeRate) target.getQuantity();
        assertEquals("urn:sdmx:org.sdmx.infomodel.codelist.Code=agency01:codelist01(01.000).unitCode01", quantityChangeRate.getUnitCode().getUrn());
        assertEquals(QuantityUnitSymbolPosition.START, quantityChangeRate.getUnitSymbolPosition());
        assertEquals(BigInteger.valueOf(5), quantityChangeRate.getSignificantDigits());
        assertEquals(BigInteger.valueOf(2), quantityChangeRate.getDecimalPlaces());
        assertEquals(BigInteger.valueOf(10), quantityChangeRate.getUnitMultiplier());
        assertEquals(BigInteger.valueOf(1000), quantityChangeRate.getMin());
        assertEquals(BigInteger.valueOf(2000), quantityChangeRate.getMax());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agency01:conceptScheme02(01.000).numerator1", quantityChangeRate.getNumerator().getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agency01:conceptScheme03(01.000).denominator1", quantityChangeRate.getDenominator().getUrn());
        assertEquals(false, quantityChangeRate.isIsPercentage());
        assertEqualsInternationalString("es", "percentageOf-concept2 en Español", "en", "percentageOf-concept2 in English", quantityChangeRate.getPercentageOf());
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agency01:conceptScheme04(01.000).baseQuantity1", quantityChangeRate.getBaseQuantity().getUrn());
    }

    @Test
    public void testToConceptImported() {
        ConceptSchemeVersionMetamac conceptScheme = mockConceptScheme("agencyID1", "resourceID1", "01.123");
        conceptScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        ConceptMetamac source = mockConcept("concept2", conceptScheme, null);
        source.getNameableArtefact().setUrnProvider("urnProvider");
        source.getNameableArtefact().setUriProvider("uriProviderDb");

        // Transform
        Concept target = do2RestInternalMapper.toConcept(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.conceptscheme.Concept=agencyID1:resourceID1(01.123).concept2", target.getUrnInternal());
        assertEquals("urnProvider", target.getUrn());
        assertEquals("uriProviderDb", target.getUri());
    }
}