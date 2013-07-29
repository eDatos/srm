package org.siemac.metamac.srm.rest.internal.v1_0.organisation.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.rest.api.constants.RestApiConstants.WILDCARD_ALL;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsAsserts.assertEqualsResource;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks.mockOrganisation;
import static org.siemac.metamac.srm.rest.internal.v1_0.organisation.utils.OrganisationsDoMocks.mockOrganisationItemResult;
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
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation.OrganisationsDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-internal/applicationContext-test.xml"})
public class OrganisationsTypeAgenciesDo2RestMapperTest {

    @Autowired
    private OrganisationsDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToAgencySchemes() throws MetamacException {

        String agencyID = WILDCARD_ALL;
        String resourceID = WILDCARD_ALL;
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
    public void testToAgencyScheme() throws MetamacException {

        OrganisationSchemeVersionMetamac source = mockOrganisationSchemeWithOrganisations("agencyID1", "AGENCIES", "01.000", OrganisationSchemeTypeEnum.AGENCY_SCHEME);

        // Transform
        AgencyScheme target = do2RestInternalMapper.toAgencyScheme(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, target.getKind());
        assertEquals("AGENCIES", target.getId());
        assertEquals("01.000", target.getVersion());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=agencyID1:AGENCIES(01.000)", target.getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=agencyID1:AGENCIES(01.000)", target.getUrnSiemac());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes/agencyID1/AGENCIES/01.000";
        assertEquals(RestInternalConstants.KIND_AGENCY_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=AGENCY_SCHEME;id=agencyID1:AGENCIES(01.000)",
                target.getManagementAppLink());
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
        assertEqualsDate(new DateTime(2013, 10, 1, 10, 12, 13, 14), target.getCreatedDate());

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(RestInternalConstants.KIND_AGENCIES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/agencies", target.getChildLinks().getChildLinks().get(0).getHref());

        // do not retrieve agencies
        assertEquals(0, target.getAgencies().size());
    }

    @Test
    public void testToAgencySchemeImported() throws MetamacException {

        OrganisationSchemeVersionMetamac source = mockOrganisationScheme("agencyID1", "AGENCIES", "01.000", OrganisationSchemeTypeEnum.AGENCY_SCHEME);
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUrnProvider("urnProvider");
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");

        // Transform
        AgencyScheme target = do2RestInternalMapper.toAgencyScheme(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=agencyID1:AGENCIES(01.000)", target.getUrnSiemac());
        assertEquals("urnProvider", target.getUrn());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToAgencies() throws MetamacException {

        String agencyID = WILDCARD_ALL;
        String organisationSchemeID = WILDCARD_ALL;
        String version = WILDCARD_ALL;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.AGENCY_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.AGENCY;
        OrganisationSchemeVersionMetamac organisationScheme1 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, organisationSchemeTypeEnum);
        OrganisationSchemeVersionMetamac organisationScheme2 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1, organisationSchemeTypeEnum);
        List<OrganisationMetamac> sources = new ArrayList<OrganisationMetamac>();
        sources.add(mockOrganisation(ITEM_1_CODE, organisationScheme1, null, organisationTypeEnum));
        sources.add(mockOrganisation(ITEM_2_CODE, organisationScheme1, null, organisationTypeEnum));
        sources.add(mockOrganisation(ITEM_3_CODE, organisationScheme1, null, organisationTypeEnum));
        sources.add(mockOrganisation(ITEM_1_CODE, organisationScheme2, null, organisationTypeEnum));

        Integer totalRows = sources.size() * 5;
        PagedResult<OrganisationMetamac> sourcesPagedList = new PagedResult<OrganisationMetamac>(sources, offset, sources.size(), limit, totalRows, 0);

        // Transform
        Agencies targets = do2RestInternalMapper.toAgencies(sourcesPagedList, agencyID, organisationSchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_AGENCIES, targets.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes" + "/" + agencyID + "/" + organisationSchemeID + "/" + version + "/agencies?query=" + query
                + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, targets.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", targets.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", targets.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", targets.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", targets.getLastLink());

        assertEquals(limit.intValue(), targets.getLimit().intValue());
        assertEquals(offset.intValue(), targets.getOffset().intValue());
        assertEquals(totalRows.intValue(), targets.getTotal().intValue());

        assertEquals(sources.size(), targets.getAgencies().size());
        for (int i = 0; i < sources.size(); i++) {
            OrganisationMetamac source = sources.get(i);
            ResourceInternal target = targets.getAgencies().get(i);
            assertEqualsResource(source.getItemSchemeVersion(), source, null, RestInternalConstants.KIND_AGENCY, RestInternalConstants.LINK_SUBPATH_AGENCY_SCHEMES,
                    RestInternalConstants.LINK_SUBPATH_AGENCIES, target);
        }
    }

    @Test
    public void testToAgenciesItemResult() throws MetamacException {

        String agencyID = AGENCY_1;
        String organisationSchemeID = "AGENCIES";
        String version = VERSION_1;

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme(agencyID, organisationSchemeID, version, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
        List<ItemResult> sources = new ArrayList<ItemResult>();
        sources.add(mockOrganisationItemResult(ITEM_1_CODE, null, OrganisationTypeEnum.AGENCY));
        sources.add(mockOrganisationItemResult(ITEM_2_CODE, null, OrganisationTypeEnum.AGENCY));
        sources.add(mockOrganisationItemResult(ITEM_3_CODE, null, OrganisationTypeEnum.AGENCY));

        // Transform
        Agencies targets = do2RestInternalMapper.toAgencies(sources, organisationScheme);

        // Validate
        assertEquals(RestInternalConstants.KIND_AGENCIES, targets.getKind());

        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes" + "/" + agencyID + "/" + organisationSchemeID + "/" + version + "/agencies", targets.getSelfLink());
        assertEquals(null, targets.getFirstLink());
        assertEquals(null, targets.getPreviousLink());
        assertEquals(null, targets.getNextLink());
        assertEquals(null, targets.getLastLink());

        assertEquals(null, targets.getLimit());
        assertEquals(null, targets.getOffset());
        assertEquals(sources.size(), targets.getTotal().intValue());

        assertEquals(sources.size(), targets.getAgencies().size());
        for (int i = 0; i < sources.size(); i++) {
            ItemResult source = sources.get(i);
            ResourceInternal target = targets.getAgencies().get(i);
            assertEqualsResource(organisationScheme, null, source, RestInternalConstants.KIND_AGENCY, RestInternalConstants.LINK_SUBPATH_AGENCY_SCHEMES, RestInternalConstants.LINK_SUBPATH_AGENCIES,
                    target);
        }
    }

    @Test
    public void testToAgency() throws MetamacException {

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.AGENCY_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.AGENCY;

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "AGENCIES", "01.000", organisationSchemeTypeEnum);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, organisationTypeEnum);

        // Transform
        Agency target = do2RestInternalMapper.toAgency(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_AGENCY, target.getKind());
        assertEquals("organisation2", target.getId());
        assertEquals("idAsMaintainerorganisation2", target.getNestedId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=agencyID1:AGENCIES(01.000).organisation2", target.getUrn());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=agencyID1:AGENCIES(01.000).organisation2", target.getUrnSiemac());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes/agencyID1/AGENCIES/01.000/agencies";
        String selfLink = parentLink + "/organisation2";
        assertEquals(RestInternalConstants.KIND_AGENCY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(
                "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=AGENCY_SCHEME;id=agencyID1:AGENCIES(01.000)/organisation;id=organisation2",
                target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_AGENCIES, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        assertEqualsInternationalString("es", "comment-organisation2 en Español", "en", "comment-organisation2 in English", target.getComment());
    }

    @Test
    public void testToAgencyImported() throws MetamacException {

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.AGENCY_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.AGENCY;

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "AGENCIES", "01.000", organisationSchemeTypeEnum);
        organisationScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, organisationTypeEnum);
        source.getNameableArtefact().setUriProvider("uriProviderDb");
        source.getNameableArtefact().setUrnProvider("urnProvider");

        // Transform
        Agency target = do2RestInternalMapper.toAgency(source);

        // Validate
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=agencyID1:AGENCIES(01.000).organisation2", target.getUrnSiemac());
        assertEquals("urnProvider", target.getUrn());
        assertEquals("uriProviderDb", target.getUri());
    }

}