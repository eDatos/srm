package org.siemac.metamac.srm.rest.internal.v1_0.organisation.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.srm.rest.internal.RestInternalConstants.WILDCARD;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks.mockOrganisation;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks.mockOrganisationScheme;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks.mockOrganisationSchemeWithOrganisations;
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
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.AgencyType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
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
public class OrganisationsTypeAgenciesDo2RestMapperTest {

    @Autowired
    private OrganisationsDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToAgencySchemes() {

        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);
        OrganisationSchemeTypeEnum type = OrganisationSchemeTypeEnum.AGENCY_SCHEME;

        List<OrganisationSchemeVersionMetamac> source = new ArrayList<OrganisationSchemeVersionMetamac>();
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, type));
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1, type));
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2, type));
        source.add(mockOrganisationScheme(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1, type));

        Integer totalRows = source.size() * 5;
        PagedResult<OrganisationSchemeVersionMetamac> sources = new PagedResult<OrganisationSchemeVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        AgencySchemes target = do2RestInternalMapper.toAgencySchemes(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEMES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/" + RestInternalConstants.LINK_SUBPATH_AGENCY_SCHEMES + "/" + agencyID + "/" + resourceID + "?query=" + query
                + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getAgencySchemes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), RestInternalConstants.KIND_AGENCY_SCHEME, RestInternalConstants.LINK_SUBPATH_AGENCY_SCHEMES, target.getAgencySchemes().get(i));
        }
    }

    @Test
    public void testToAgencyScheme() {

        OrganisationSchemeVersionMetamac source = mockOrganisationSchemeWithOrganisations("agencyID1", "AGENCIES", "01.000", OrganisationSchemeTypeEnum.AGENCY_SCHEME);

        // Transform
        AgencyScheme target = do2RestInternalMapper.toAgencyScheme(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, target.getKind());
        assertEquals("urn:AGENCIES:01.000", target.getUrn());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes/idAsMaintaineragencyID1/AGENCIES/01.000";
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEMES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes", target.getParentLink().getHref());
        assertEqualsInternationalString("es", "comment-AGENCIESv01.000 en Español", "en", "comment-AGENCIESv01.000 in English", target.getComment());
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
        assertEquals(Boolean.FALSE, target.getLifeCycle().isIsExternalPublicationFailed());
        assertEqualsDate(new DateTime(2013, 8, 2, 1, 1, 1, 1), target.getLifeCycle().getExternalPublicationFailedDate());

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_AGENCIES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/agencies", target.getChildLinks().getChildLinks().get(0).getHref());

        // Agencies (SDMX type)
        assertEquals(2, target.getAgencies().size());
        int i = 0;
        {
            AgencyType agency = target.getAgencies().get(i++);
            assertTrue(agency instanceof AgencyType);
            assertFalse(agency instanceof Agency);
            assertEquals("urn:organisation1", agency.getUrn());
        }
        {
            AgencyType agency = target.getAgencies().get(i++);
            assertTrue(agency instanceof AgencyType);
            assertFalse(agency instanceof Agency);
            assertEquals("urn:organisation2", agency.getUrn());
        }
        assertEquals(i, target.getAgencies().size());
    }

    @Test
    public void testToAgencySchemeImported() {

        OrganisationSchemeVersionMetamac source = mockOrganisationScheme("agencyID1", "resourceID1", "01.123", OrganisationSchemeTypeEnum.AGENCY_SCHEME);
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");

        // Transform
        AgencyScheme target = do2RestInternalMapper.toAgencyScheme(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToAgencies() {

        String agencyID = WILDCARD;
        String organisationSchemeID = WILDCARD;
        String version = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.AGENCY_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.AGENCY;
        OrganisationSchemeVersionMetamac organisationScheme1 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, organisationSchemeTypeEnum);
        OrganisationSchemeVersionMetamac organisationScheme2 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1, organisationSchemeTypeEnum);
        List<OrganisationMetamac> source = new ArrayList<OrganisationMetamac>();
        source.add(mockOrganisation(ITEM_1_CODE, organisationScheme1, null, organisationTypeEnum));
        source.add(mockOrganisation(ITEM_2_CODE, organisationScheme1, null, organisationTypeEnum));
        source.add(mockOrganisation(ITEM_3_CODE, organisationScheme1, null, organisationTypeEnum));
        source.add(mockOrganisation(ITEM_1_CODE, organisationScheme2, null, organisationTypeEnum));

        Integer totalRows = source.size() * 5;
        PagedResult<OrganisationMetamac> sources = new PagedResult<OrganisationMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        Agencies target = do2RestInternalMapper.toAgencies(sources, agencyID, organisationSchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_AGENCIES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes" + "/" + agencyID + "/" + organisationSchemeID + "/" + version + "/agencies?query=" + query
                + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getAgencies().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), RestInternalConstants.KIND_AGENCY, RestInternalConstants.LINK_SUBPATH_AGENCY_SCHEMES, RestInternalConstants.LINK_SUBPATH_AGENCIES, target.getAgencies()
                    .get(i));
        }
    }

    @Test
    public void testToAgency() {

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.AGENCY_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.AGENCY;

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "AGENCIES", "01.000", organisationSchemeTypeEnum);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, organisationTypeEnum);

        // Transform
        Agency target = do2RestInternalMapper.toAgency(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_AGENCY, target.getKind());
        assertEquals("urn:organisation2", target.getUrn());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes/idAsMaintaineragencyID1/AGENCIES/01.000/agencies";
        String selfLink = parentLink + "/organisation2";
        assertEquals(RestInternalConstants.KIND_AGENCY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_AGENCIES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        assertEqualsInternationalString("es", "comment-organisation2 en Español", "en", "comment-organisation2 in English", target.getComment());
    }

    @Test
    public void testToAgencyImported() {

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.AGENCY_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.AGENCY;

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "resourceID1", "01.123", organisationSchemeTypeEnum);
        organisationScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, organisationTypeEnum);
        source.getNameableArtefact().setUriProvider("uriProviderDb");

        // Transform
        Agency target = do2RestInternalMapper.toAgency(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
    }
}