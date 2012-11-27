package org.siemac.metamac.srm.rest.internal.v1_0.organisation.mapper;

import static org.junit.Assert.assertEquals;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsAsserts.assertEqualsDataConsumer;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsAsserts.assertEqualsDataConsumerScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks.mockOrganisation;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks.mockOrganisationScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.AGENCY_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_1_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_2_VERSION_2;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_CODE;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ITEM_SCHEME_3_VERSION_1;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.ORDER_BY_ID_DESC;
import static org.siemac.metamac.srm.rest.internal.v1_0.utils.RestTestConstants.QUERY_ID_LIKE_1_NAME_LIKE_2;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumer;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumerScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumerSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.DataConsumers;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation.OrganisationsDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-internal/applicationContext-test.xml"})
public class OrganisationsTypeDataConsumerDo2RestMapperTest {

    @Autowired
    private OrganisationsDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToDataConsumerSchemes() {

        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);
        OrganisationSchemeTypeEnum type = OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME;

        List<OrganisationSchemeVersionMetamac> source = new ArrayList<OrganisationSchemeVersionMetamac>();
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, type));
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1, type));
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_2, type));
        source.add(mockOrganisationScheme(AGENCY_2, ITEM_SCHEME_3_CODE, ITEM_SCHEME_3_VERSION_1, type));

        Integer totalRows = source.size() * 5;
        PagedResult<OrganisationSchemeVersionMetamac> sources = new PagedResult<OrganisationSchemeVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        DataConsumerSchemes target = do2RestInternalMapper.toDataConsumerSchemes(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_DATA_CONSUMER_SCHEMES, target.getKind());

        String baseLink = "http://data.istac.es/apis/srm/v1.0/" + RestInternalConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES + "/" + agencyID + "/" + resourceID + "?query=" + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getDataConsumerSchemes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), RestInternalConstants.KIND_DATA_CONSUMER_SCHEME, RestInternalConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES, target.getDataConsumerSchemes().get(i));
        }
    }

    @Test
    public void testToDataConsumerScheme() {

        OrganisationSchemeVersionMetamac source = mockOrganisationSchemeWithOrganisations("agencyID1", "resourceID1", "01.123", OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);

        // Transform
        DataConsumerScheme target = do2RestInternalMapper.toDataConsumerScheme(source);

        // Validate
        assertEqualsDataConsumerScheme(source, target);
    }

    @Test
    public void testToDataConsumers() {

        String agencyID = WILDCARD;
        String organisationSchemeID = WILDCARD;
        String version = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.DATA_CONSUMER;
        OrganisationSchemeVersionMetamac organisationScheme1 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, ITEM_SCHEME_1_VERSION_1, organisationSchemeTypeEnum);
        OrganisationSchemeVersionMetamac organisationScheme2 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, ITEM_SCHEME_2_VERSION_1, organisationSchemeTypeEnum);
        List<OrganisationMetamac> source = new ArrayList<OrganisationMetamac>();
        source.add(mockOrganisation(ITEM_1_CODE, organisationScheme1, null, organisationTypeEnum));
        source.add(mockOrganisation(ITEM_2_CODE, organisationScheme1, null, organisationTypeEnum));
        source.add(mockOrganisation(ITEM_3_CODE, organisationScheme1, null, organisationTypeEnum));
        source.add(mockOrganisation(ITEM_1_CODE, organisationScheme2, null, organisationTypeEnum));

        Integer totalRows = source.size() * 5;
        PagedResult<OrganisationMetamac> sources = new PagedResult<OrganisationMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        DataConsumers target = do2RestInternalMapper.toDataConsumers(sources, agencyID, organisationSchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_DATA_CONSUMERS, target.getKind());

        String baseLink = "http://data.istac.es/apis/srm/v1.0/dataconsumerschemes" + "/" + agencyID + "/" + organisationSchemeID + "/" + version + "/dataconsumers?query=" + query + "&orderBy=" + orderBy;
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getDataConsumers().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), RestInternalConstants.KIND_DATA_CONSUMER, RestInternalConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES, RestInternalConstants.LINK_SUBPATH_DATA_CONSUMERS, target
                    .getDataConsumers().get(i));
        }
    }

    @Test
    public void testToDataConsumer() {
        
        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.DATA_CONSUMER;

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "resourceID1", "01.123", organisationSchemeTypeEnum);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, organisationTypeEnum);

        // Transform
        DataConsumer target = do2RestInternalMapper.toDataConsumer(source);

        // Validate
        assertEqualsDataConsumer(source, target);
    }
}