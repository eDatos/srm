package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseDo2RestMapperV10Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

@Component
public class OrganisationsDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl implements OrganisationsDo2RestMapperV10 {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2JaxbMapper organisationsDo2JaxbSdmxMapper;

    @Autowired
    @Qualifier("organisationsDo2JaxbCallbackMetamac")
    private OrganisationsDo2JaxbCallback                                                    organisationsDo2JaxbCallback;

    @Override
    public AgencySchemes toAgencySchemes(PagedResult<OrganisationSchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        OrganisationSchemeTypeEnum type = OrganisationSchemeTypeEnum.AGENCY_SCHEME;

        AgencySchemes targets = new AgencySchemes();
        targets.setKind(getKindItemSchemes(type));

        // Pagination
        String baseLink = toOrganisationSchemesLink(agencyID, resourceID, null, type);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getAgencySchemes().add(target);
        }
        return targets;
    }

    // @Override
    // public CategoryScheme toCategoryScheme(OrganisationSchemeVersionMetamac source) {
    // if (source == null) {
    // return null;
    // }
    // // following method will call toCategoryScheme(OrganisationSchemeVersionMetamac source, CategoryScheme target) method, thank to callback
    // return (CategoryScheme) organisationsDo2JaxbSdmxMapper.categorySchemeDoToJaxb(source, organisationsDo2JaxbCallback);
    // }
    //
    // @Override
    // public void toCategoryScheme(OrganisationSchemeVersionMetamac source, CategoryScheme target) {
    // if (source == null) {
    // return;
    // }
    // target.setKind(RestInternalConstants.KIND_CATEGORY_SCHEME);
    // target.setSelfLink(toCategorySchemeSelfLink(source));
    // target.setUri(target.getSelfLink().getHref());
    // target.setReplaceToVersion(source.getMaintainableArtefact().getReplaceToVersion());
    // target.setParentLink(toCategorySchemeParentLink(source));
    // target.setChildLinks(toCategorySchemeChildLinks(source));
    // }
    //
    // @Override
    // public Categories toCategories(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {
    //
    // Categories targets = new Categories();
    // targets.setKind(RestInternalConstants.KIND_CATEGORIES);
    //
    // // Pagination
    // String baseLink = toCategoriesLink(agencyID, resourceID, version);
    // SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);
    //
    // // Values
    // for (OrganisationMetamac source : sourcesPagedResult.getValues()) {
    // Resource target = toResource(source);
    // targets.getCategories().add(target);
    // }
    // return targets;
    // }
    //
    // @Override
    // public Category toCategory(OrganisationMetamac source) {
    // if (source == null) {
    // return null;
    // }
    // Category target = new Category();
    // organisationsDo2JaxbSdmxMapper.categoryDoToJaxbWithoutChildren(source, target);
    //
    // target.setKind(RestInternalConstants.KIND_CATEGORY);
    // target.setSelfLink(toCategorySelfLink(source));
    // target.setUri(target.getSelfLink().getHref());
    // target.setParentLink(toCategoryParentLink(source));
    // target.setParent(source.getParent() != null ? source.getParent().getNameableArtefact().getUrn() : null);
    // target.setChildLinks(toCategoryChildLinks(source));
    //
    // return target;
    // }

    private ResourceLink toOrganisationSchemeSelfLink(OrganisationSchemeVersion source) {
        return toResourceLink(getKindItemScheme(source.getOrganisationSchemeType()), toOrganisationSchemeLink(source));
    }

    // private ResourceLink toCategorySchemeParentLink(OrganisationSchemeVersionMetamac source) {
    // return toResourceLink(RestInternalConstants.KIND_CATEGORY_SCHEMES, toAgencySchemesLink(null, null, null));
    // }
    //
    // private ChildLinks toCategorySchemeChildLinks(OrganisationSchemeVersionMetamac source) {
    // ChildLinks targets = new ChildLinks();
    // targets.getChildLinks().add(toResourceLink(RestInternalConstants.KIND_CATEGORIES, toCategoriesLink(source)));
    // targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
    // return targets;
    // }
    //
    // private ResourceLink toCategorySelfLink(OrganisationMetamac source) {
    // return toResourceLink(RestInternalConstants.KIND_CATEGORY, toCategoryLink(source));
    // }
    //
    // private ResourceLink toCategoryParentLink(OrganisationMetamac source) {
    // return toResourceLink(RestInternalConstants.KIND_CATEGORIES, toCategoriesLink(source.getItemSchemeVersion()));
    // }
    //
    // private ChildLinks toCategoryChildLinks(OrganisationMetamac source) {
    // // nothing
    // return null;
    // }
    //
    private Resource toResource(OrganisationSchemeVersion source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), getKindItemScheme(source.getOrganisationSchemeType()), toOrganisationSchemeSelfLink(source));
    }

    // private Resource toResource(OrganisationMetamac source) {
    // if (source == null) {
    // return null;
    // }
    // return toResource(source.getNameableArtefact(), RestInternalConstants.KIND_CATEGORY, toCategorySelfLink(source));
    // }

    private String toOrganisationSchemesLink(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) {
        return toItemSchemesLink(getSupathItemSchemes(type), agencyID, resourceID, version);
    }
    private String toOrganisationSchemeLink(OrganisationSchemeVersion itemSchemeVersion) {
        return toItemSchemeLink(getSupathItemSchemes(itemSchemeVersion.getOrganisationSchemeType()), itemSchemeVersion);
    }
    // private String toCategoriesLink(String agencyID, String resourceID, String version) {
    // return toItemsLink(getItemSchemeSubpath(), getItemsSubpath(), agencyID, resourceID, version);
    // }
    // private String toCategoriesLink(ItemSchemeVersion itemSchemeVersion) {
    // return toItemsLink(getItemSchemeSubpath(), getItemsSubpath(), itemSchemeVersion);
    // }
    // private String toCategoryLink(com.arte.statistic.sdmx.srm.core.base.domain.Item item) {
    // return toItemLink(getItemSchemeSubpath(), getItemsSubpath(), item);
    // }

    private String getSupathItemSchemes(OrganisationSchemeTypeEnum type) {
        switch (type) {
            case AGENCY_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_AGENCY_SCHEMES;
            case ORGANISATION_UNIT_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATION_UNIT_SCHEMES;
            case DATA_CONSUMER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_CONSUMER_SCHEMES;
            case DATA_PROVIDER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_PROVIDER_SCHEMES;
            default:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATION_SCHEMES;
        }
    }
    // private String getItemsSubpath() {
    // return RestInternalConstants.LINK_SUBPATH_CATEGORIES;
    // }

    private String getKindItemSchemes(OrganisationSchemeTypeEnum type) {
        switch (type) {
            case AGENCY_SCHEME:
                return RestInternalConstants.KIND_AGENCY_SCHEMES;
            case ORGANISATION_UNIT_SCHEME:
                return RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEMES;
            case DATA_CONSUMER_SCHEME:
                return RestInternalConstants.KIND_DATA_CONSUMER_SCHEMES;
            case DATA_PROVIDER_SCHEME:
                return RestInternalConstants.KIND_DATA_PROVIDER_SCHEMES;
            default:
                // generic list of organisation schemes are supported
                return RestInternalConstants.KIND_ORGANISATION_SCHEMES;
        }
    }

    private String getKindItemScheme(OrganisationSchemeTypeEnum type) {
        switch (type) {
            case AGENCY_SCHEME:
                return RestInternalConstants.KIND_AGENCY_SCHEME;
            case ORGANISATION_UNIT_SCHEME:
                return RestInternalConstants.KIND_ORGANISATION_UNIT_SCHEME;
            case DATA_CONSUMER_SCHEME:
                return RestInternalConstants.KIND_DATA_CONSUMER_SCHEME;
            case DATA_PROVIDER_SCHEME:
                return RestInternalConstants.KIND_DATA_PROVIDER_SCHEME;
            default:
                throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + type);
        }
    }
}