package org.siemac.metamac.srm.rest.internal.v1_0.code.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCode;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCodelist;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCodelistWithCodes;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts.assertEqualsInternationalString;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AccessType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.code.CodesDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-internal/applicationContext-test.xml"})
public class CodesDo2RestMapperTest {

    @Autowired
    private CodesDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToCodelists() {

        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        List<CodelistVersionMetamac> source = new ArrayList<CodelistVersionMetamac>();
        source.add(mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1));
        source.add(mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_VERSION_1));
        source.add(mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_VERSION_2));
        source.add(mockCodelist(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_VERSION_1));

        Integer totalRows = source.size() * 5;
        PagedResult<CodelistVersionMetamac> sources = new PagedResult<CodelistVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Codelists target = do2RestInternalMapper.toCodelists(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CODELISTS, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCodelists().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getCodelists().get(i));
        }
    }

    @Test
    public void testToCodelist() {

        CodelistVersionMetamac source = mockCodelistWithCodes("agencyID1", "resourceID1", "01.123");

        // Transform
        Codelist target = do2RestInternalMapper.toCodelist(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_CODELIST, target.getKind());
        assertEquals("urn:resourceID1:01.123", target.getUrn());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/idAsMaintaineragencyID1/resourceID1/01.123";
        assertEquals(RestInternalConstants.KIND_CODELIST, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_CODELISTS, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/codelists", target.getParentLink().getHref());
        assertEqualsInternationalString("es", "comment-resourceID1v01.123 en Español", "en", "comment-resourceID1v01.123 in English", target.getComment());
        assertEqualsInternationalString("es", "shortName-resourceID1v01.123 en Español", "en", "shortName-resourceID1v01.123 in English", target.getShortName());
        assertTrue(target.isIsRecommended());
        assertEquals(AccessType.PUBLIC, target.getAccessType());
        // assertEquals("defaultOrderVisualisation1", target.getDefaultOrderVisualisation()); // TODO order visualisation
        assertEquals("family1", target.getFamily().getId());
        assertEqualsInternationalString("es", "name-family1 en Español", "en", "name-family1 in English", target.getFamily().getTitle());
        assertEquals("variable1", target.getVariable().getId());
        assertEqualsInternationalString("es", "name-variable1 en Español", "en", "name-variable1 in English", target.getVariable().getTitle());
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

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_CODES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/codes", target.getChildLinks().getChildLinks().get(0).getHref());
    }

    @Test
    public void testToCodelistImported() {

        CodelistVersionMetamac source = mockCodelistWithCodes("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");

        // Transform
        Codelist target = do2RestInternalMapper.toCodelist(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToCodes() {

        String agencyID = WILDCARD;
        String codeSchemeID = WILDCARD;
        String version = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        CodelistVersionMetamac codeScheme1 = mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);
        CodelistVersionMetamac codeScheme2 = mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_VERSION_1);
        List<CodeMetamac> source = new ArrayList<CodeMetamac>();
        source.add(mockCode(ITEM_1_CODE, codeScheme1, null));
        source.add(mockCode(ITEM_2_CODE, codeScheme1, null));
        source.add(mockCode(ITEM_3_CODE, codeScheme1, null));
        source.add(mockCode(ITEM_1_CODE, codeScheme2, null));

        Integer totalRows = source.size() * 5;
        PagedResult<CodeMetamac> sources = new PagedResult<CodeMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Codes target = do2RestInternalMapper.toCodes(sources, agencyID, codeSchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CODES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists" + "/" + agencyID + "/" + codeSchemeID + "/" + version + "/codes?query=" + query + "&orderBy="
                + orderBy;
        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getCodes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), target.getCodes().get(i));
        }
    }

    @Test
    public void testToCode() {
        CodelistVersionMetamac codeScheme = mockCodelist("agencyID1", "resourceID1", "01.123");
        CodeMetamac parent = mockCode("codeParent1", codeScheme, null);
        CodeMetamac source = mockCode("code2", codeScheme, parent);

        // Transform
        Code target = do2RestInternalMapper.toCode(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_CODE, target.getKind());
        assertEquals("urn:code2", target.getUrn());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/codelists/idAsMaintaineragencyID1/resourceID1/01.123/codes";
        String selfLink = parentLink + "/code2";
        assertEquals(RestInternalConstants.KIND_CODE, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_CODES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        assertEqualsInternationalString("es", "comment-code2 en Español", "en", "comment-code2 in English", target.getComment());
        assertEqualsInternationalString("es", "shortName-variableElement1 en Español", "en", "shortName-variableElement1 in English", target.getShortName());
        assertEquals("variableElement1", target.getVariableElement().getId());
        assertEqualsInternationalString("es", "name-variableElement1 en Español", "en", "name-variableElement1 in English", target.getVariableElement().getTitle());
        assertEquals("codeParent1", target.getParent().getRef().getId());
    }

    @Test
    public void testToCodeImported() {
        CodelistVersionMetamac codeScheme = mockCodelist("agencyID1", "resourceID1", "01.123");
        codeScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CodeMetamac source = mockCode("code2", codeScheme, null);
        source.getNameableArtefact().setUriProvider("uriProviderDb");

        // Transform
        Code target = do2RestInternalMapper.toCode(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToCodeWithoutVariableElement() {
        CodelistVersionMetamac codeScheme = mockCodelist("agencyID1", "resourceID1", "01.123");
        codeScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        CodeMetamac source = mockCode("code2", codeScheme, null);
        source.setVariableElement(null);

        // Transform
        Code target = do2RestInternalMapper.toCode(source);

        // Validate
        assertEqualsInternationalString("es", "shortName-code2 en Español", "en", "shortName-code2 in English", target.getShortName());
    }
}