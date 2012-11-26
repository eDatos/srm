package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import java.math.BigInteger;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agencies;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Agency;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

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
        targets.setKind(toKindItemSchemes(type));

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
    
    @Override
    public OrganisationUnitSchemes toOrganisationUnitSchemes(PagedResult<OrganisationSchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        OrganisationSchemeTypeEnum type = OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME;

        OrganisationUnitSchemes targets = new OrganisationUnitSchemes();
        targets.setKind(toKindItemSchemes(type));

        // Pagination
        String baseLink = toOrganisationSchemesLink(agencyID, resourceID, null, type);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getOrganisationUnitSchemes().add(target);
        }
        return targets;
    }

    @Override
    public AgencyScheme toAgencyScheme(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toAgencyScheme(OrganisationSchemeVersionMetamac source, AgencyScheme target) method, thank to callback
        return (AgencyScheme) organisationsDo2JaxbSdmxMapper.agencySchemeDoToJaxb(source, organisationsDo2JaxbCallback);
    }
    
    @Override
    public OrganisationUnitScheme toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toAgencyScheme(OrganisationSchemeVersionMetamac source, AgencyScheme target) method, thank to callback
        return (OrganisationUnitScheme) organisationsDo2JaxbSdmxMapper.organisationUnitSchemeDoToJaxb(source, organisationsDo2JaxbCallback);
    }

    @Override
    public void toAgencyScheme(OrganisationSchemeVersionMetamac source, AgencyScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(toKindItemScheme(source.getOrganisationSchemeType()));
        target.setSelfLink(toOrganisationSchemeSelfLink(source));
        target.setUri(target.getSelfLink().getHref());
        target.setReplaceToVersion(source.getMaintainableArtefact().getReplaceToVersion());
        target.setParentLink(toOrganisationSchemeParentLink(source));
        target.setChildLinks(toOrganisationSchemeChildLinks(source));
    }
    
    @Override
    public void toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source, OrganisationUnitScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(toKindItemScheme(source.getOrganisationSchemeType()));
        target.setSelfLink(toOrganisationSchemeSelfLink(source));
        target.setUri(target.getSelfLink().getHref());
        target.setReplaceToVersion(source.getMaintainableArtefact().getReplaceToVersion());
        target.setParentLink(toOrganisationSchemeParentLink(source));
        target.setChildLinks(toOrganisationSchemeChildLinks(source));
    }

    @Override
    public Agencies toAgencies(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        Agencies targets = new Agencies();
        targets.setKind(toKindItems(OrganisationSchemeTypeEnum.AGENCY_SCHEME));

        // Pagination
        String baseLink = toOrganisationsLink(agencyID, resourceID, version, OrganisationSchemeTypeEnum.AGENCY_SCHEME);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getAgencies().add(target);
        }
        return targets;
    }
    
    @Override
    public OrganisationUnits toOrganisationUnits(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        OrganisationUnits targets = new OrganisationUnits();
        targets.setKind(toKindItems(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME));

        // Pagination
        String baseLink = toOrganisationsLink(agencyID, resourceID, version, OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getOrganisationUnits().add(target);
        }
        return targets;
    }

    @Override
    public Agency toAgency(OrganisationMetamac source) {
        if (source == null) {
            return null;
        }
        Agency target = new Agency();
        organisationsDo2JaxbSdmxMapper.agencyDoToJaxb(source, target);

        target.setKind(toKindItem(source.getOrganisationType()));
        target.setSelfLink(toOrganisationSelfLink(source));
        target.setUri(target.getSelfLink().getHref());
        target.setParentLink(toOrganisationParentLink(source));
        target.setChildLinks(toOrganisationChildLinks(source));

        return target;
    }
    
    @Override
    public OrganisationUnit toOrganisationUnit(OrganisationMetamac source) {
        if (source == null) {
            return null;
        }
        OrganisationUnit target = new OrganisationUnit();
        organisationsDo2JaxbSdmxMapper.organisationUnitDoToJaxb(source, target);

        target.setKind(toKindItem(source.getOrganisationType()));
        target.setSelfLink(toOrganisationSelfLink(source));
        target.setUri(target.getSelfLink().getHref());
        target.setParentLink(toOrganisationParentLink(source));
        target.setChildLinks(toOrganisationChildLinks(source));

        return target;
    }
    
    private ResourceLink toOrganisationSchemeSelfLink(OrganisationSchemeVersion source) {
        return toResourceLink(toKindItemScheme(source.getOrganisationSchemeType()), toOrganisationSchemeLink(source));
    }

    private ResourceLink toOrganisationSchemeParentLink(OrganisationSchemeVersionMetamac source) {
        return toResourceLink(toKindItemSchemes(source.getOrganisationSchemeType()), toOrganisationSchemesLink(null, null, null, source.getOrganisationSchemeType()));
    }

    private ChildLinks toOrganisationSchemeChildLinks(OrganisationSchemeVersionMetamac source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(toKindItems(source.getOrganisationSchemeType()), toOrganisationsLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toOrganisationSelfLink(OrganisationMetamac source) {
        return toResourceLink(toKindItem(source.getOrganisationType()), toOrganisationLink(source));
    }

    private ResourceLink toOrganisationParentLink(OrganisationMetamac source) {
        return toResourceLink(toKindItems(source.getOrganisationType()), toOrganisationsLink(source));
    }

    private ChildLinks toOrganisationChildLinks(OrganisationMetamac source) {
        // nothing
        return null;
    }

    private Resource toResource(OrganisationSchemeVersion source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), toKindItemScheme(source.getOrganisationSchemeType()), toOrganisationSchemeSelfLink(source));
    }

    private Resource toResource(OrganisationMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getNameableArtefact(), toKindItem(source.getOrganisationType()), toOrganisationSelfLink(source));
    }

    private String toOrganisationSchemesLink(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) {
        return toItemSchemesLink(toSupathItemSchemes(type), agencyID, resourceID, version);
    }
    private String toOrganisationSchemeLink(OrganisationSchemeVersion itemSchemeVersion) {
        return toItemSchemeLink(toSupathItemSchemes(itemSchemeVersion.getOrganisationSchemeType()), itemSchemeVersion);
    }
    private String toOrganisationsLink(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) {
        return toItemsLink(toSupathItemSchemes(type), toSubpathItems(type), agencyID, resourceID, version);
    }
    private String toOrganisationsLink(OrganisationSchemeVersionMetamac itemSchemeVersion) {
        return toItemsLink(toSupathItemSchemes(itemSchemeVersion.getOrganisationSchemeType()), toSubpathItems(itemSchemeVersion.getOrganisationSchemeType()), itemSchemeVersion);
    }
    private String toOrganisationsLink(OrganisationMetamac item) {
        return toItemsLink(toSupathItemSchemes(item.getOrganisationType()), toSubpathItems(item.getOrganisationType()), item.getItemSchemeVersion());
    }
    private String toOrganisationLink(OrganisationMetamac item) {
        return toItemLink(toSupathItemSchemes(item.getOrganisationType()), toSubpathItems(item.getOrganisationType()), item);
    }

    private String toSupathItemSchemes(OrganisationSchemeTypeEnum type) {
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

    private String toSupathItemSchemes(OrganisationTypeEnum type) {
        return toSupathItemSchemes(SrmRestInternalUtils.toOrganisationSchemeType(type));
    }

    private String toSubpathItems(OrganisationSchemeTypeEnum type) {
        switch (type) {
            case AGENCY_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_AGENCIES;
            case ORGANISATION_UNIT_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATION_UNITS;
            case DATA_CONSUMER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_CONSUMERS;
            case DATA_PROVIDER_SCHEME:
                return RestInternalConstants.LINK_SUBPATH_DATA_PROVIDERS;
            default:
                return RestInternalConstants.LINK_SUBPATH_ORGANISATIONS;
        }
    }

    private String toSubpathItems(OrganisationTypeEnum type) {
        return toSubpathItems(SrmRestInternalUtils.toOrganisationSchemeType(type));
    }

    private String toKindItemSchemes(OrganisationSchemeTypeEnum type) {
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

    private String toKindItems(OrganisationSchemeTypeEnum type) {
        switch (type) {
            case AGENCY_SCHEME:
                return RestInternalConstants.KIND_AGENCIES;
            case ORGANISATION_UNIT_SCHEME:
                return RestInternalConstants.KIND_ORGANISATION_UNITS;
            case DATA_CONSUMER_SCHEME:
                return RestInternalConstants.KIND_DATA_CONSUMERS;
            case DATA_PROVIDER_SCHEME:
                return RestInternalConstants.KIND_DATA_PROVIDERS;
            default:
                // generic list of organisation schemes are supported
                return RestInternalConstants.KIND_ORGANISATIONS;
        }
    }

    private String toKindItems(OrganisationTypeEnum type) {
        return toKindItems(SrmRestInternalUtils.toOrganisationSchemeType(type));
    }

    private String toKindItemScheme(OrganisationSchemeTypeEnum type) {
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

    private String toKindItem(OrganisationTypeEnum type) {
        switch (type) {
            case AGENCY:
                return RestInternalConstants.KIND_AGENCY;
            case ORGANISATION_UNIT:
                return RestInternalConstants.KIND_ORGANISATION_UNIT;
            case DATA_CONSUMER:
                return RestInternalConstants.KIND_DATA_CONSUMER;
            case DATA_PROVIDER:
                return RestInternalConstants.KIND_DATA_PROVIDER;
            default:
                throw new IllegalArgumentException("OrganisationTypeEnum unsuported: " + type);
        }
    }
}