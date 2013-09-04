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
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumer;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumerSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataConsumers;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProvider;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviders;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ProcStatus;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation.OrganisationsDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm-rest-internal/applicationContext-test.xml"})
public class OrganisationsDo2RestMapperTest {

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
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEMES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/" + SrmRestConstants.LINK_SUBPATH_AGENCY_SCHEMES + "/" + agencyID + "/" + resourceID + "?query=" + query
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
            assertEqualsResource(source.get(i), SrmRestConstants.KIND_AGENCY_SCHEME, SrmRestConstants.LINK_SUBPATH_AGENCY_SCHEMES, target.getAgencySchemes().get(i));
        }
    }

    @Test
    public void testToOrganisationUnitSchemes() throws MetamacException {

        // test only specific for this organisation scheme type (full test in testToAgencySchemes)

        List<OrganisationSchemeVersionMetamac> source = new ArrayList<OrganisationSchemeVersionMetamac>();
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME));
        PagedResult<OrganisationSchemeVersionMetamac> sources = new PagedResult<OrganisationSchemeVersionMetamac>(source, 0, source.size(), 1, source.size() * 5, 0);

        // Transform
        OrganisationUnitSchemes target = do2RestInternalMapper.toOrganisationUnitSchemes(sources, null, null, null, null, 1);

        // Validate
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEMES, target.getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/organisationunitschemes?limit=1&offset=0", target.getSelfLink());

        assertEquals(source.size(), target.getOrganisationUnitSchemes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME, SrmRestConstants.LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES, target.getOrganisationUnitSchemes()
                    .get(i));
        }
    }

    @Test
    public void testToDataConsumerSchemes() throws MetamacException {

        // test only specific for this organisation scheme type (full test in testToAgencySchemes)

        List<OrganisationSchemeVersionMetamac> source = new ArrayList<OrganisationSchemeVersionMetamac>();
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME));
        PagedResult<OrganisationSchemeVersionMetamac> sources = new PagedResult<OrganisationSchemeVersionMetamac>(source, 0, source.size(), 1, source.size() * 5, 0);

        // Transform
        DataConsumerSchemes target = do2RestInternalMapper.toDataConsumerSchemes(sources, null, null, null, null, 1);

        // Validate
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMER_SCHEMES, target.getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/dataconsumerschemes?limit=1&offset=0", target.getSelfLink());

        assertEquals(source.size(), target.getDataConsumerSchemes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), SrmRestConstants.KIND_DATA_CONSUMER_SCHEME, SrmRestConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES, target.getDataConsumerSchemes().get(i));
        }
    }

    @Test
    public void testToDataProviderSchemes() throws MetamacException {

        // test only specific for this organisation scheme type (full test in testToAgencySchemes)

        List<OrganisationSchemeVersionMetamac> source = new ArrayList<OrganisationSchemeVersionMetamac>();
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME));
        PagedResult<OrganisationSchemeVersionMetamac> sources = new PagedResult<OrganisationSchemeVersionMetamac>(source, 0, source.size(), 1, source.size() * 5, 0);

        // Transform
        DataProviderSchemes target = do2RestInternalMapper.toDataProviderSchemes(sources, null, null, null, null, 1);

        // Validate
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDER_SCHEMES, target.getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes?limit=1&offset=0", target.getSelfLink());

        assertEquals(source.size(), target.getDataProviderSchemes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), SrmRestConstants.KIND_DATA_PROVIDER_SCHEME, SrmRestConstants.LINK_SUBPATH_DATA_PROVIDER_SCHEMES, target.getDataProviderSchemes().get(i));
        }
    }

    @Test
    public void testToAgencyScheme() throws MetamacException {

        OrganisationSchemeVersionMetamac source = mockOrganisationSchemeWithOrganisations("agencyID1", "AGENCIES", "01.000", OrganisationSchemeTypeEnum.AGENCY_SCHEME);

        // Transform
        AgencyScheme target = do2RestInternalMapper.toAgencyScheme(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, target.getKind());
        assertEquals("AGENCIES", target.getId());
        assertEquals("01.000", target.getVersion());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=agencyID1:AGENCIES(01.000)", target.getUrn());
        assertEquals(null, target.getUrnProvider());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes/agencyID1/AGENCIES/01.000";
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=AGENCY_SCHEME;id=agencyID1:AGENCIES(01.000)",
                target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_AGENCY_SCHEMES, target.getParentLink().getKind());
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
        assertEquals(SrmRestConstants.KIND_AGENCIES, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/agencies", target.getChildLinks().getChildLinks().get(0).getHref());
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
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.AgencyScheme=agencyID1:AGENCIES(01.000)", target.getUrn());
        assertEquals("urnProvider", target.getUrnProvider());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToDataConsumerScheme() throws MetamacException {

        // test only specific for this organisation scheme type (full test in testToAgencyScheme)

        OrganisationSchemeVersionMetamac source = mockOrganisationSchemeWithOrganisations("agencyID1", "DATACONSUMERS", "01.000", OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);

        // Transform
        DataConsumerScheme target = do2RestInternalMapper.toDataConsumerScheme(source);

        // Validate
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMER_SCHEME, target.getKind());
        assertEquals("DATACONSUMERS", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.DataConsumerScheme=agencyID1:DATACONSUMERS(01.000)", target.getUrn());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/dataconsumerschemes/agencyID1/DATACONSUMERS/01.000";
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMER_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=DATA_CONSUMER_SCHEME;id=agencyID1:DATACONSUMERS(01.000)",
                target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMER_SCHEMES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/dataconsumerschemes", target.getParentLink().getHref());
        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMERS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/dataconsumers", target.getChildLinks().getChildLinks().get(0).getHref());
    }

    @Test
    public void testToDataProviderScheme() throws MetamacException {

        // test only specific for this organisation scheme type (full test in testToAgencyScheme)

        OrganisationSchemeVersionMetamac source = mockOrganisationSchemeWithOrganisations("agencyID1", "DATAPROVIDERS", "01.000", OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);

        // Transform
        DataProviderScheme target = do2RestInternalMapper.toDataProviderScheme(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDER_SCHEME, target.getKind());
        assertEquals("DATAPROVIDERS", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.DataProviderScheme=agencyID1:DATAPROVIDERS(01.000)", target.getUrn());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes/agencyID1/DATAPROVIDERS/01.000";
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDER_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=DATA_PROVIDER_SCHEME;id=agencyID1:DATAPROVIDERS(01.000)",
                target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDER_SCHEMES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes", target.getParentLink().getHref());

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDERS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/dataproviders", target.getChildLinks().getChildLinks().get(0).getHref());
    }

    @Test
    public void testToOrganisationUnitScheme() throws MetamacException {

        // test only specific for this organisation scheme type (full test in testToAgencyScheme)

        OrganisationSchemeVersionMetamac source = mockOrganisationSchemeWithOrganisations("agencyID1", "resourceID1", "01.000", OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);

        // Transform
        OrganisationUnitScheme target = do2RestInternalMapper.toOrganisationUnitScheme(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME, target.getKind());
        assertEquals("resourceID1", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.OrganisationUnitScheme=agencyID1:resourceID1(01.000)", target.getUrn());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/organisationunitschemes/agencyID1/resourceID1/01.000";
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=ORGANISATION_UNIT_SCHEME;id=agencyID1:resourceID1(01.000)",
                target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT_SCHEMES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/organisationunitschemes", target.getParentLink().getHref());

        assertEquals(BigInteger.ONE, target.getChildLinks().getTotal());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNITS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/organisationunits", target.getChildLinks().getChildLinks().get(0).getHref());
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
        assertEquals(SrmRestConstants.KIND_AGENCIES, targets.getKind());

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
            assertEqualsResource(source.getItemSchemeVersion(), source, null, SrmRestConstants.KIND_AGENCY, SrmRestConstants.LINK_SUBPATH_AGENCY_SCHEMES,
                    SrmRestConstants.LINK_SUBPATH_AGENCIES, target);
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
        assertEquals(SrmRestConstants.KIND_AGENCIES, targets.getKind());

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
            assertEqualsResource(organisationScheme, null, source, SrmRestConstants.KIND_AGENCY, SrmRestConstants.LINK_SUBPATH_AGENCY_SCHEMES, SrmRestConstants.LINK_SUBPATH_AGENCIES,
                    target);
        }
    }

    @Test
    public void testToDataConsumers() throws MetamacException {

        String agencyID = WILDCARD_ALL;
        String organisationSchemeID = WILDCARD_ALL;
        String version = WILDCARD_ALL;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(0);
        OrganisationSchemeVersionMetamac organisationScheme1 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);
        List<OrganisationMetamac> sources = new ArrayList<OrganisationMetamac>();
        sources.add(mockOrganisation(ITEM_1_CODE, organisationScheme1, null, OrganisationTypeEnum.DATA_CONSUMER));
        PagedResult<OrganisationMetamac> sourcesPagedResult = new PagedResult<OrganisationMetamac>(sources, offset, sources.size(), limit, sources.size() * 5, 0);

        // Transform
        DataConsumers targets = do2RestInternalMapper.toDataConsumers(sourcesPagedResult, agencyID, organisationSchemeID, version, null, null, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMERS, targets.getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/dataconsumerschemes/~all/~all/~all/dataconsumers?limit=4&offset=0", targets.getSelfLink());

        assertEquals(sources.size(), targets.getDataConsumers().size());
        for (int i = 0; i < sources.size(); i++) {
            OrganisationMetamac source = sourcesPagedResult.getValues().get(i);
            ResourceInternal target = targets.getDataConsumers().get(i);
            assertEqualsResource(source.getItemSchemeVersion(), source, null, SrmRestConstants.KIND_DATA_CONSUMER, SrmRestConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES,
                    SrmRestConstants.LINK_SUBPATH_DATA_CONSUMERS, target);
        }
    }

    @Test
    public void testToDataProviders() throws MetamacException {

        String agencyID = WILDCARD_ALL;
        String organisationSchemeID = WILDCARD_ALL;
        String version = WILDCARD_ALL;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(0);
        OrganisationSchemeVersionMetamac organisationScheme1 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
        List<OrganisationMetamac> sources = new ArrayList<OrganisationMetamac>();
        sources.add(mockOrganisation(ITEM_1_CODE, organisationScheme1, null, OrganisationTypeEnum.DATA_PROVIDER));
        PagedResult<OrganisationMetamac> sourcesPagedResult = new PagedResult<OrganisationMetamac>(sources, offset, sources.size(), limit, sources.size() * 5, 0);

        // Transform
        DataProviders targets = do2RestInternalMapper.toDataProviders(sourcesPagedResult, agencyID, organisationSchemeID, version, null, null, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDERS, targets.getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes/~all/~all/~all/dataproviders?limit=4&offset=0", targets.getSelfLink());

        assertEquals(sources.size(), targets.getDataProviders().size());
        for (int i = 0; i < sources.size(); i++) {
            OrganisationMetamac source = sourcesPagedResult.getValues().get(i);
            ResourceInternal target = targets.getDataProviders().get(i);
            assertEqualsResource(source.getItemSchemeVersion(), source, null, SrmRestConstants.KIND_DATA_PROVIDER, SrmRestConstants.LINK_SUBPATH_DATA_PROVIDER_SCHEMES,
                    SrmRestConstants.LINK_SUBPATH_DATA_PROVIDERS, target);
        }
    }

    @Test
    public void testToOrganisationUnits() throws MetamacException {

        String agencyID = WILDCARD_ALL;
        String organisationSchemeID = WILDCARD_ALL;
        String version = WILDCARD_ALL;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(0);
        OrganisationSchemeVersionMetamac organisationScheme1 = mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
        List<OrganisationMetamac> sources = new ArrayList<OrganisationMetamac>();
        sources.add(mockOrganisation(ITEM_1_CODE, organisationScheme1, null, OrganisationTypeEnum.ORGANISATION_UNIT));
        PagedResult<OrganisationMetamac> sourcesPagedResult = new PagedResult<OrganisationMetamac>(sources, offset, sources.size(), limit, sources.size() * 5, 0);

        // Transform
        OrganisationUnits targets = do2RestInternalMapper.toOrganisationUnits(sourcesPagedResult, agencyID, organisationSchemeID, version, null, null, limit);

        // Validate
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNITS, targets.getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/organisationunitschemes/~all/~all/~all/organisationunits?limit=4&offset=0", targets.getSelfLink());

        assertEquals(sources.size(), targets.getOrganisationUnits().size());
        for (int i = 0; i < sources.size(); i++) {
            OrganisationMetamac source = sourcesPagedResult.getValues().get(i);
            ResourceInternal target = targets.getOrganisationUnits().get(i);
            assertEqualsResource(source.getItemSchemeVersion(), source, null, SrmRestConstants.KIND_ORGANISATION_UNIT, SrmRestConstants.LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES,
                    SrmRestConstants.LINK_SUBPATH_ORGANISATION_UNITS, target);
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
        assertEquals(SrmRestConstants.KIND_AGENCY, target.getKind());
        assertEquals("organisation2", target.getId());
        assertEquals("idAsMaintainerorganisation2", target.getNestedId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=agencyID1:AGENCIES(01.000).organisation2", target.getUrn());
        assertEquals(null, target.getUrnProvider());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/agencyschemes/agencyID1/AGENCIES/01.000/agencies";
        String selfLink = parentLink + "/organisation2";
        assertEquals(SrmRestConstants.KIND_AGENCY, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(
                "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=AGENCY_SCHEME;id=agencyID1:AGENCIES(01.000)/organisation;id=organisation2",
                target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(SrmRestConstants.KIND_AGENCIES, target.getParentLink().getKind());
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
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.Agency=agencyID1:AGENCIES(01.000).organisation2", target.getUrn());
        assertEquals("urnProvider", target.getUrnProvider());
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToDataConsumer() throws MetamacException {

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "DATACONSUMERS", "01.000", OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, OrganisationTypeEnum.DATA_CONSUMER);

        // Transform
        DataConsumer target = do2RestInternalMapper.toDataConsumer(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMER, target.getKind());
        assertEquals("organisation2", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.DataConsumer=agencyID1:DATACONSUMERS(01.000).organisation2", target.getUrn());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/dataconsumerschemes/agencyID1/DATACONSUMERS/01.000/dataconsumers";
        String selfLink = parentLink + "/organisation2";
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMER, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(
                "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=DATA_CONSUMER_SCHEME;id=agencyID1:DATACONSUMERS(01.000)/organisation;id=organisation2",
                target.getManagementAppLink());
        assertEquals(SrmRestConstants.KIND_DATA_CONSUMERS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
    }

    @Test
    public void testToDataProvider() throws MetamacException {

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "DATAPROVIDERS", "01.000", OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, OrganisationTypeEnum.DATA_PROVIDER);

        // Transform
        DataProvider target = do2RestInternalMapper.toDataProvider(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDER, target.getKind());
        assertEquals("organisation2", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.DataProvider=agencyID1:DATAPROVIDERS(01.000).organisation2", target.getUrn());
        assertEquals(null, target.getUrnProvider());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes/agencyID1/DATAPROVIDERS/01.000/dataproviders";
        String selfLink = parentLink + "/organisation2";
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDER, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(
                "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=DATA_PROVIDER_SCHEME;id=agencyID1:DATAPROVIDERS(01.000)/organisation;id=organisation2",
                target.getManagementAppLink());
        assertEquals(SrmRestConstants.KIND_DATA_PROVIDERS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
    }

    @Test
    public void testToOrganisationUnit() throws MetamacException {

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "resourceID1", "01.000", OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, OrganisationTypeEnum.ORGANISATION_UNIT);

        // Transform
        OrganisationUnit target = do2RestInternalMapper.toOrganisationUnit(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT, target.getKind());
        assertEquals("organisation2", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.OrganisationUnit=agencyID1:resourceID1(01.000).organisation2", target.getUrn());
        assertEquals(null, target.getUrnProvider());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/organisationunitschemes/agencyID1/resourceID1/01.000/organisationunits";
        String selfLink = parentLink + "/organisation2";
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNIT, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(
                "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=ORGANISATION_UNIT_SCHEME;id=agencyID1:resourceID1(01.000)/organisation;id=organisation2",
                target.getManagementAppLink());
        assertEquals(SrmRestConstants.KIND_ORGANISATION_UNITS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());
    }

}