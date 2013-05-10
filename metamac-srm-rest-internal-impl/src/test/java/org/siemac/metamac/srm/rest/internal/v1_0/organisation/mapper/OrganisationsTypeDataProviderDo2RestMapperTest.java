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
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.DataProviderType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProvider;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviderSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.DataProviders;
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
public class OrganisationsTypeDataProviderDo2RestMapperTest {

    @Autowired
    private OrganisationsDo2RestMapperV10 do2RestInternalMapper;

    @Test
    public void testToDataProviderSchemes() throws MetamacException {

        String agencyID = WILDCARD;
        String resourceID = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);
        OrganisationSchemeTypeEnum type = OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME;

        List<OrganisationSchemeVersionMetamac> source = new ArrayList<OrganisationSchemeVersionMetamac>();
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_1_CODE, VERSION_1, type));
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_1, type));
        source.add(mockOrganisationScheme(AGENCY_1, ITEM_SCHEME_2_CODE, VERSION_2, type));
        source.add(mockOrganisationScheme(AGENCY_2, ITEM_SCHEME_3_CODE, VERSION_1, type));

        Integer totalRows = source.size() * 5;
        PagedResult<OrganisationSchemeVersionMetamac> sources = new PagedResult<OrganisationSchemeVersionMetamac>(source, offset, source.size(), limit, totalRows, 0);

        // Transform
        DataProviderSchemes target = do2RestInternalMapper.toDataProviderSchemes(sources, agencyID, resourceID, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDER_SCHEMES, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/" + RestInternalConstants.LINK_SUBPATH_DATA_PROVIDER_SCHEMES + "/" + agencyID + "/" + resourceID + "?query="
                + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getDataProviderSchemes().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), RestInternalConstants.KIND_DATA_PROVIDER_SCHEME, RestInternalConstants.LINK_SUBPATH_DATA_PROVIDER_SCHEMES, target.getDataProviderSchemes().get(i));
        }
    }

    @Test
    public void testToDataProviderScheme() throws MetamacException {

        OrganisationSchemeVersionMetamac source = mockOrganisationSchemeWithOrganisations("agencyID1", "DATAPROVIDERS", "01.000", OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);

        // Transform
        DataProviderScheme target = do2RestInternalMapper.toDataProviderScheme(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDER_SCHEME, target.getKind());
        assertEquals("DATAPROVIDERS", target.getId());
        assertEquals("01.000", target.getVersion());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.DataProviderScheme=agencyID1:DATAPROVIDERS(01.000)", target.getUrn());
        String selfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes/agencyID1/DATAPROVIDERS/01.000";
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDER_SCHEME, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals("http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=DATA_PROVIDER_SCHEME;id=agencyID1:DATAPROVIDERS(01.000)",
                target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDER_SCHEMES, target.getParentLink().getKind());
        assertEquals("http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes", target.getParentLink().getHref());
        assertEqualsInternationalString("es", "comment-DATAPROVIDERSv01.000 en Español", "en", "comment-DATAPROVIDERSv01.000 in English", target.getComment());
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
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDERS, target.getChildLinks().getChildLinks().get(0).getKind());
        assertEquals(selfLink + "/dataproviders", target.getChildLinks().getChildLinks().get(0).getHref());

        // DataProviders (SDMX type)
        assertEquals(2, target.getDataProviders().size());
        int i = 0;
        {
            DataProviderType dataProvider = target.getDataProviders().get(i++);
            assertTrue(dataProvider instanceof DataProviderType);
            assertFalse(dataProvider instanceof DataProvider);
            assertEquals("urn:sdmx:org.sdmx.infomodel.base.DataProvider=agencyID1:DATAPROVIDERS(01.000).organisation1", dataProvider.getUrn());
        }
        {
            DataProviderType dataProvider = target.getDataProviders().get(i++);
            assertTrue(dataProvider instanceof DataProviderType);
            assertFalse(dataProvider instanceof DataProvider);
            assertEquals("urn:sdmx:org.sdmx.infomodel.base.DataProvider=agencyID1:DATAPROVIDERS(01.000).organisation2", dataProvider.getUrn());
        }
        assertEquals(i, target.getDataProviders().size());
    }

    @Test
    public void testToDataProviderSchemeImported() throws MetamacException {

        OrganisationSchemeVersionMetamac source = mockOrganisationScheme("agencyID1", "resourceID1", "01.123", OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
        source.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        source.getMaintainableArtefact().setUriProvider("uriProviderDb");

        // Transform
        DataProviderScheme target = do2RestInternalMapper.toDataProviderScheme(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
    }

    @Test
    public void testToDataProviders() throws MetamacException {

        String agencyID = WILDCARD;
        String organisationSchemeID = WILDCARD;
        String version = WILDCARD;
        String query = QUERY_ID_LIKE_1_NAME_LIKE_2;
        String orderBy = ORDER_BY_ID_DESC;
        Integer limit = Integer.valueOf(4);
        Integer offset = Integer.valueOf(4);

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.DATA_PROVIDER;
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
        DataProviders target = do2RestInternalMapper.toDataProviders(sources, agencyID, organisationSchemeID, version, query, orderBy, limit);

        // Validate
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDERS, target.getKind());

        String baseLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes" + "/" + agencyID + "/" + organisationSchemeID + "/" + version + "/dataproviders?query="
                + query + "&orderBy=" + orderBy;

        assertEquals(baseLink + "&limit=" + limit + "&offset=" + offset, target.getSelfLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getFirstLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=0", target.getPreviousLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=8", target.getNextLink());
        assertEquals(baseLink + "&limit=" + limit + "&offset=16", target.getLastLink());

        assertEquals(limit.intValue(), target.getLimit().intValue());
        assertEquals(offset.intValue(), target.getOffset().intValue());
        assertEquals(totalRows.intValue(), target.getTotal().intValue());

        assertEquals(source.size(), target.getDataProviders().size());
        for (int i = 0; i < source.size(); i++) {
            assertEqualsResource(source.get(i), RestInternalConstants.KIND_DATA_PROVIDER, RestInternalConstants.LINK_SUBPATH_DATA_PROVIDER_SCHEMES, RestInternalConstants.LINK_SUBPATH_DATA_PROVIDERS,
                    target.getDataProviders().get(i));
        }
    }

    @Test
    public void testToDataProvider() throws MetamacException {

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.DATA_PROVIDER;

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "DATAPROVIDERS", "01.000", organisationSchemeTypeEnum);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, organisationTypeEnum);

        // Transform
        DataProvider target = do2RestInternalMapper.toDataProvider(source);

        // Validate (only Metamac metadata and some SDMX). Note: check with concrete values (not doing "getter" of source)
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDER, target.getKind());
        assertEquals("organisation2", target.getId());
        assertEquals("urn:sdmx:org.sdmx.infomodel.base.DataProvider=agencyID1:DATAPROVIDERS(01.000).organisation2", target.getUrn());

        String parentLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/dataproviderschemes/agencyID1/DATAPROVIDERS/01.000/dataproviders";
        String selfLink = parentLink + "/organisation2";
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDER, target.getSelfLink().getKind());
        assertEquals(selfLink, target.getSelfLink().getHref());
        assertEquals(
                "http://localhost:8080/metamac-srm-web/#structuralResources/organisationSchemes/organisationScheme;type=DATA_PROVIDER_SCHEME;id=agencyID1:DATAPROVIDERS(01.000)/organisation;id=organisation2",
                target.getManagementAppLink());
        assertEquals(target.getSelfLink().getHref(), target.getUri());
        assertEquals(RestInternalConstants.KIND_DATA_PROVIDERS, target.getParentLink().getKind());
        assertEquals(parentLink, target.getParentLink().getHref());
        assertNull(target.getChildLinks());

        assertEqualsInternationalString("es", "comment-organisation2 en Español", "en", "comment-organisation2 in English", target.getComment());
    }

    @Test
    public void testToDataProviderImported() throws MetamacException {

        OrganisationSchemeTypeEnum organisationSchemeTypeEnum = OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME;
        OrganisationTypeEnum organisationTypeEnum = OrganisationTypeEnum.DATA_PROVIDER;

        OrganisationSchemeVersionMetamac organisationScheme = mockOrganisationScheme("agencyID1", "resourceID1", "01.123", organisationSchemeTypeEnum);
        organisationScheme.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        OrganisationMetamac source = mockOrganisation("organisation2", organisationScheme, null, organisationTypeEnum);
        source.getNameableArtefact().setUriProvider("uriProviderDb");

        // Transform
        DataProvider target = do2RestInternalMapper.toDataProvider(source);

        // Validate
        assertEquals("uriProviderDb", target.getUri());
    }
}