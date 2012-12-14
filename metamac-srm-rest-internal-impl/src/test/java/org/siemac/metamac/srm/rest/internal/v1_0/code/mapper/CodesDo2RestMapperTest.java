package org.siemac.metamac.srm.rest.internal.v1_0.code.mapper;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesAsserts.assertEqualsCode;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesAsserts.assertEqualsCodelist;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCode;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCodelist;
import static org.siemac.metamac.srm.rest.internal.v1_0.code.utils.CodesDoMocks.mockCodelistWithCodes;
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

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Codes;
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

        String baseLink = "http://data.istac.es/apis/srm/v1.0/codelists" + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

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
        // Validate
        assertEqualsCodelist(source, target);
    }

    @Test
    public void testToCodelistImported() {

        CodelistVersionMetamac source = mockCodelistWithCodes("agencyID1", "resourceID1", "01.123");
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);

        // Transform
        Codelist target = do2RestInternalMapper.toCodelist(source);
        // Validate
        assertEqualsCodelist(source, target);
    }

    @Test
    public void testToCodes() {

        String agencyID = WILDCARD;
        String codelistID = WILDCARD;
        String version = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        CodelistVersionMetamac codelist1 = mockCodelist(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_VERSION_1);
        CodelistVersionMetamac codelist2 = mockCodelist(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_VERSION_1);
        List<CodeMetamac> source = new ArrayList<CodeMetamac>();
        source.add(mockCode(ITEM_1_CODE, codelist1, null));
        source.add(mockCode(ITEM_2_CODE, codelist1, null));
        source.add(mockCode(ITEM_3_CODE, codelist1, null));
        source.add(mockCode(ITEM_1_CODE, codelist2, null));

        Integer totalRows = source.size() * 5;
        PagedResult<CodeMetamac> sources = new PagedResult<CodeMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Codes target = do2RestInternalMapper.toCodes(sources, agencyID, codelistID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_CODES, target.getKind());

        String baseLink = "http://data.istac.es/apis/srm/v1.0/codelists" + "/" + agencyID + "/" + codelistID + "/" + version + "/codes?query=" + query + "&orderBy=" + orderBy;
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
        CodelistVersionMetamac codelist = mockCodelist("agencyID1", "resourceID1", "01.123");
        CodeMetamac parent = mockCode("codeParent1", codelist, null);
        CodeMetamac source = mockCode("code2", codelist, parent);

        // Transform
        Code target = do2RestInternalMapper.toCode(source);

        // Validate
        assertEqualsCode(source, target);
    }

    @Test
    public void testToCodeImported() {
        CodelistVersionMetamac codelist = mockCodelist("agencyID1", "resourceID1", "01.123");
        codelist.getMaintainableArtefact().setIsImported(Boolean.TRUE);

        CodeMetamac parent = mockCode("codeParent1", codelist, null);
        CodeMetamac source = mockCode("code2", codelist, parent);

        // Transform
        Code target = do2RestInternalMapper.toCode(source);

        // Validate
        assertEqualsCode(source, target);
    }
}