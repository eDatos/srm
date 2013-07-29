package org.siemac.metamac.srm.rest.internal.v1_0.mapper.organisation;

import java.math.BigInteger;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
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
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisation;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Organisations;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.ItemSchemeBaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationResultExtensionPoint;
import com.arte.statistic.sdmx.srm.core.organisation.domain.OrganisationSchemeVersion;
import com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2JaxbCallback;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@Component
public class OrganisationsDo2RestMapperV10Impl extends ItemSchemeBaseDo2RestMapperV10Impl implements OrganisationsDo2RestMapperV10 {

    private final boolean                                                                   AS_STUB         = false;
    private final boolean                                                                   IS_INTERNAL_API = true;

    @Autowired
    private SrmConfiguration                                                                srmConfiguration;

    @Autowired
    private com.arte.statistic.sdmx.srm.core.organisation.mapper.OrganisationsDo2JaxbMapper organisationsDo2JaxbSdmxMapper;

    @Autowired
    @Qualifier("organisationsDo2JaxbRestInternalCallbackMetamac")
    private OrganisationsDo2JaxbCallback                                                    organisationsDo2JaxbCallback;

    @Override
    public OrganisationSchemes toOrganisationSchemes(PagedResult<OrganisationSchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        OrganisationSchemeTypeEnum type = null;

        OrganisationSchemes targets = new OrganisationSchemes();
        targets.setKind(toKindItemSchemes(type)); // generic

        // Pagination
        String baseLink = toOrganisationSchemesLink(agencyID, resourceID, null, type);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getOrganisationSchemes().add(target);
        }
        return targets;
    }

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
            ResourceInternal target = toResource(source);
            targets.getAgencySchemes().add(target);
        }
        return targets;
    }

    @Override
    public OrganisationUnitSchemes toOrganisationUnitSchemes(PagedResult<OrganisationSchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy,
            Integer limit) {

        OrganisationSchemeTypeEnum type = OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME;

        OrganisationUnitSchemes targets = new OrganisationUnitSchemes();
        targets.setKind(toKindItemSchemes(type));

        // Pagination
        String baseLink = toOrganisationSchemesLink(agencyID, resourceID, null, type);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getOrganisationUnitSchemes().add(target);
        }
        return targets;
    }

    @Override
    public DataProviderSchemes toDataProviderSchemes(PagedResult<OrganisationSchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        OrganisationSchemeTypeEnum type = OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME;

        DataProviderSchemes targets = new DataProviderSchemes();
        targets.setKind(toKindItemSchemes(type));

        // Pagination
        String baseLink = toOrganisationSchemesLink(agencyID, resourceID, null, type);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getDataProviderSchemes().add(target);
        }
        return targets;
    }

    @Override
    public DataConsumerSchemes toDataConsumerSchemes(PagedResult<OrganisationSchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        OrganisationSchemeTypeEnum type = OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME;

        DataConsumerSchemes targets = new DataConsumerSchemes();
        targets.setKind(toKindItemSchemes(type));

        // Pagination
        String baseLink = toOrganisationSchemesLink(agencyID, resourceID, null, type);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getDataConsumerSchemes().add(target);
        }
        return targets;
    }

    @Override
    public OrganisationScheme toOrganisationScheme(OrganisationSchemeVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }

        OrganisationScheme target = new OrganisationScheme();
        toOrganisationScheme(source, target);
        return target;
    }

    @Override
    public void toOrganisationScheme(OrganisationSchemeVersionMetamac source, OrganisationScheme target) throws MetamacException {
        target.setKind(toKindItemScheme(source.getOrganisationSchemeType()));
        switch (source.getOrganisationSchemeType()) {
            case AGENCY_SCHEME:
                target.setAgencyScheme(toAgencyScheme(source));
                break;
            case ORGANISATION_UNIT_SCHEME:
                target.setOrganisationUnitScheme(toOrganisationUnitScheme(source));
                break;
            case DATA_CONSUMER_SCHEME:
                target.setDataConsumerScheme(toDataConsumerScheme(source));
                break;
            case DATA_PROVIDER_SCHEME:
                target.setDataProviderScheme(toDataProviderScheme(source));
                break;
            default:
                throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + source.getOrganisationSchemeType());
        }
    }

    @Override
    public AgencyScheme toAgencyScheme(OrganisationSchemeVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        // following method will call toAgencyScheme(OrganisationSchemeVersionMetamac source, AgencyScheme target) method, thank to callback
        return (AgencyScheme) organisationsDo2JaxbSdmxMapper.agencySchemeDoToJaxb(source, organisationsDo2JaxbCallback, AS_STUB, IS_INTERNAL_API, srmConfiguration.retrieveMaintainerUrnDefault());
    }

    @Override
    public OrganisationUnitScheme toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toAgencyScheme(OrganisationSchemeVersionMetamac source, OrganisationUnitScheme target) method, thank to callback
        return (OrganisationUnitScheme) organisationsDo2JaxbSdmxMapper.organisationUnitSchemeDoToJaxb(source, organisationsDo2JaxbCallback, AS_STUB);
    }

    @Override
    public DataProviderScheme toDataProviderScheme(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toDataProviderScheme(OrganisationSchemeVersionMetamac source, DataProviderScheme target) method, thank to callback
        return (DataProviderScheme) organisationsDo2JaxbSdmxMapper.dataProviderSchemeDoToJaxb(source, organisationsDo2JaxbCallback, AS_STUB);
    }

    @Override
    public DataConsumerScheme toDataConsumerScheme(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toDataConsumerScheme(OrganisationSchemeVersionMetamac source, DataConsumerScheme target) method, thank to callback
        return (DataConsumerScheme) organisationsDo2JaxbSdmxMapper.dataConsumerSchemeDoToJaxb(source, organisationsDo2JaxbCallback, AS_STUB);
    }

    @Override
    public void toAgencyScheme(OrganisationSchemeVersionMetamac source, AgencyScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(toKindItemScheme(source.getOrganisationSchemeType()));
        target.setUrnInternal(source.getMaintainableArtefact().getUrn());
        target.setSelfLink(toOrganisationSchemeSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toOrganisationSchemeParentLink(source));
        target.setChildLinks(toOrganisationSchemeChildLinks(source));
        target.setManagementAppLink(toOrganisationSchemeManagementApplicationLink(source));

        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));
        target.setCreatedDate(toDate(source.getItemScheme().getResourceCreatedDate()));
    }

    @Override
    public void toOrganisationUnitScheme(OrganisationSchemeVersionMetamac source, OrganisationUnitScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(toKindItemScheme(source.getOrganisationSchemeType()));
        target.setUrnInternal(source.getMaintainableArtefact().getUrn());
        target.setSelfLink(toOrganisationSchemeSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toOrganisationSchemeParentLink(source));
        target.setChildLinks(toOrganisationSchemeChildLinks(source));
        target.setManagementAppLink(toOrganisationSchemeManagementApplicationLink(source));

        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));
        target.setCreatedDate(toDate(source.getItemScheme().getResourceCreatedDate()));
    }

    @Override
    public void toDataProviderScheme(OrganisationSchemeVersionMetamac source, DataProviderScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(toKindItemScheme(source.getOrganisationSchemeType()));
        target.setUrnInternal(source.getMaintainableArtefact().getUrn());
        target.setSelfLink(toOrganisationSchemeSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toOrganisationSchemeParentLink(source));
        target.setChildLinks(toOrganisationSchemeChildLinks(source));
        target.setManagementAppLink(toOrganisationSchemeManagementApplicationLink(source));

        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));
        target.setCreatedDate(toDate(source.getItemScheme().getResourceCreatedDate()));
    }

    @Override
    public void toDataConsumerScheme(OrganisationSchemeVersionMetamac source, DataConsumerScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(toKindItemScheme(source.getOrganisationSchemeType()));
        target.setUrnInternal(source.getMaintainableArtefact().getUrn());
        target.setSelfLink(toOrganisationSchemeSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toOrganisationSchemeParentLink(source));
        target.setChildLinks(toOrganisationSchemeChildLinks(source));
        target.setManagementAppLink(toOrganisationSchemeManagementApplicationLink(source));

        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));
        target.setCreatedDate(toDate(source.getItemScheme().getResourceCreatedDate()));
    }

    @Override
    public Organisations toOrganisations(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        Organisations targets = new Organisations();
        targets.setKind(toKindItemsFromOrganisationType(null)); // generic

        // Pagination
        String baseLink = toOrganisationsLink(agencyID, resourceID, version, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationMetamac source : sourcesPagedResult.getValues()) {
            ItemResourceInternal target = toResource(source);
            targets.getOrganisations().add(target);
        }
        return targets;
    }

    @Override
    public Organisations toOrganisations(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion) {

        Organisations targets = new Organisations();
        targets.setKind(toKindItemsFromOrganisationType(null)); // generic

        // No pagination
        targets.setSelfLink(toOrganisationsLink(organisationSchemeVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            ItemResourceInternal target = toResource(source, organisationSchemeVersion);
            targets.getOrganisations().add(target);
        }
        return targets;
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
            ItemResourceInternal target = toResource(source);
            targets.getAgencies().add(target);
        }
        return targets;
    }

    @Override
    public Agencies toAgencies(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion) {

        Agencies targets = new Agencies();
        targets.setKind(toKindItems(OrganisationSchemeTypeEnum.AGENCY_SCHEME));

        // No pagination
        targets.setSelfLink(toOrganisationsLink(organisationSchemeVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            ItemResourceInternal target = toResource(source, organisationSchemeVersion);
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
            ItemResourceInternal target = toResource(source);
            targets.getOrganisationUnits().add(target);
        }
        return targets;
    }

    @Override
    public OrganisationUnits toOrganisationUnits(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion) {

        OrganisationUnits targets = new OrganisationUnits();
        targets.setKind(toKindItems(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME));

        // No pagination
        targets.setSelfLink(toOrganisationsLink(organisationSchemeVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            ItemResourceInternal target = toResource(source, organisationSchemeVersion);
            targets.getOrganisationUnits().add(target);
        }
        return targets;
    }

    @Override
    public DataProviders toDataProviders(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        DataProviders targets = new DataProviders();
        targets.setKind(toKindItems(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME));

        // Pagination
        String baseLink = toOrganisationsLink(agencyID, resourceID, version, OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationMetamac source : sourcesPagedResult.getValues()) {
            ItemResourceInternal target = toResource(source);
            targets.getDataProviders().add(target);
        }
        return targets;
    }

    @Override
    public DataProviders toDataProviders(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion) {

        DataProviders targets = new DataProviders();
        targets.setKind(toKindItems(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME));

        // No pagination
        targets.setSelfLink(toOrganisationsLink(organisationSchemeVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            ItemResourceInternal target = toResource(source, organisationSchemeVersion);
            targets.getDataProviders().add(target);
        }
        return targets;
    }

    @Override
    public DataConsumers toDataConsumers(PagedResult<OrganisationMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        DataConsumers targets = new DataConsumers();
        targets.setKind(toKindItems(OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME));

        // Pagination
        String baseLink = toOrganisationsLink(agencyID, resourceID, version, OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (OrganisationMetamac source : sourcesPagedResult.getValues()) {
            ItemResourceInternal target = toResource(source);
            targets.getDataConsumers().add(target);
        }
        return targets;
    }

    @Override
    public DataConsumers toDataConsumers(List<ItemResult> sources, OrganisationSchemeVersionMetamac organisationSchemeVersion) {
        DataConsumers targets = new DataConsumers();
        targets.setKind(toKindItems(OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME));

        // No pagination
        targets.setSelfLink(toOrganisationsLink(organisationSchemeVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            ItemResourceInternal target = toResource(source, organisationSchemeVersion);
            targets.getDataConsumers().add(target);
        }
        return targets;
    }

    @Override
    public Organisation toOrganisation(OrganisationMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        Organisation target = new Organisation();
        target.setKind(toKindItem(source.getOrganisationType()));

        switch (source.getOrganisationType()) {
            case AGENCY:
                target.setAgency(toAgency(source));
                break;
            case ORGANISATION_UNIT:
                target.setOrganisationUnit(toOrganisationUnit(source));
                break;
            case DATA_CONSUMER:
                target.setDataConsumer(toDataConsumer(source));
                break;
            case DATA_PROVIDER:
                target.setDataProvider(toDataProvider(source));
                break;
            default:
                throw new IllegalArgumentException("OrganisationTypeEnum unsuported: " + source.getOrganisationType());
        }
        return target;
    }

    @Override
    public Agency toAgency(OrganisationMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        Agency target = new Agency();
        organisationsDo2JaxbSdmxMapper.agencyDoToJaxb(source, target, IS_INTERNAL_API, srmConfiguration.retrieveMaintainerUrnDefault());

        target.setKind(toKindItem(source.getOrganisationType()));
        target.setUrnInternal(source.getNameableArtefact().getUrn());
        target.setSelfLink(toOrganisationSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toOrganisationParentLink(source));
        target.setChildLinks(toOrganisationChildLinks(source));
        target.setManagementAppLink(toOrganisationManagementApplicationLink(source));
        target.setNestedId(source.getIdAsMaintainer());
        target.setComment(toInternationalString(source.getNameableArtefact().getComment()));
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
        target.setUrnInternal(source.getNameableArtefact().getUrn());
        target.setSelfLink(toOrganisationSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toOrganisationParentLink(source));
        target.setChildLinks(toOrganisationChildLinks(source));
        target.setManagementAppLink(toOrganisationManagementApplicationLink(source));

        target.setComment(toInternationalString(source.getNameableArtefact().getComment()));
        return target;
    }

    @Override
    public DataProvider toDataProvider(OrganisationMetamac source) {
        if (source == null) {
            return null;
        }
        DataProvider target = new DataProvider();
        organisationsDo2JaxbSdmxMapper.dataProviderDoToJaxb(source, target);

        target.setKind(toKindItem(source.getOrganisationType()));
        target.setUrnInternal(source.getNameableArtefact().getUrn());
        target.setSelfLink(toOrganisationSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toOrganisationParentLink(source));
        target.setChildLinks(toOrganisationChildLinks(source));
        target.setManagementAppLink(toOrganisationManagementApplicationLink(source));

        target.setComment(toInternationalString(source.getNameableArtefact().getComment()));
        return target;
    }

    @Override
    public DataConsumer toDataConsumer(OrganisationMetamac source) {
        if (source == null) {
            return null;
        }
        DataConsumer target = new DataConsumer();
        organisationsDo2JaxbSdmxMapper.dataConsumerDoToJaxb(source, target);

        target.setKind(toKindItem(source.getOrganisationType()));
        target.setUrnInternal(source.getNameableArtefact().getUrn());
        target.setSelfLink(toOrganisationSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toOrganisationParentLink(source));
        target.setChildLinks(toOrganisationChildLinks(source));
        target.setManagementAppLink(toOrganisationManagementApplicationLink(source));

        target.setComment(toInternationalString(source.getNameableArtefact().getComment()));
        return target;
    }

    @Override
    protected boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source) {
        return true; // no additional conditions
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

    private ResourceLink toOrganisationSelfLink(com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation source) {
        return toResourceLink(toKindItem(source.getOrganisationType()), toOrganisationLink(source));
    }
    private ResourceLink toOrganisationSelfLink(ItemResult source, OrganisationSchemeVersion organisationSchemeVersion) {
        return toResourceLink(toKindItem(getOrganisationTypeEnum(source)), toOrganisationLink(source, organisationSchemeVersion));
    }

    private ResourceLink toOrganisationParentLink(OrganisationMetamac source) {
        return toResourceLink(toKindItemsFromOrganisationType(source.getOrganisationType()), toOrganisationsLink(source));
    }

    private ChildLinks toOrganisationChildLinks(OrganisationMetamac source) {
        // nothing
        return null;
    }

    private ResourceInternal toResource(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        toResource(source.getMaintainableArtefact(), toKindItemScheme(source.getOrganisationSchemeType()), toOrganisationSchemeSelfLink(source),
                toOrganisationSchemeManagementApplicationLink(source), target);
        return target;
    }

    private ItemResourceInternal toResource(OrganisationMetamac source) {
        if (source == null) {
            return null;
        }
        ItemResourceInternal target = new ItemResourceInternal();
        toResource(source, toKindItem(source.getOrganisationType()), toOrganisationSelfLink(source), toOrganisationManagementApplicationLink(source), target);
        if (OrganisationTypeEnum.AGENCY.equals(source.getOrganisationType())) {
            target.setNestedId(source.getIdAsMaintainer());
        }
        return target;
    }

    private ItemResourceInternal toResource(ItemResult source, OrganisationSchemeVersionMetamac organisationSchemeVersion) {
        if (source == null) {
            return null;
        }
        OrganisationTypeEnum organisationType = getOrganisationTypeEnum(source);
        ItemResourceInternal target = new ItemResourceInternal();
        toResource(source, toKindItem(organisationType), toOrganisationSelfLink(source, organisationSchemeVersion),
                toOrganisationManagementApplicationLink(organisationSchemeVersion, source, organisationType), target);
        if (OrganisationTypeEnum.AGENCY.equals(organisationType)) {
            target.setNestedId(getIdAsMaintainer(source));
        }
        return target;
    }

    private String toOrganisationSchemesLink(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) {
        return toMaintainableArtefactLink(toSupathItemSchemes(type), agencyID, resourceID, version);
    }
    private String toOrganisationSchemeLink(OrganisationSchemeVersion itemSchemeVersion) {
        return toItemSchemeLink(toSupathItemSchemes(itemSchemeVersion.getOrganisationSchemeType()), itemSchemeVersion);
    }
    private String toOrganisationsLink(String agencyID, String resourceID, String version, OrganisationSchemeTypeEnum type) {
        return toItemsLink(toSupathItemSchemes(type), toSubpathItems(type), agencyID, resourceID, version);
    }
    private String toOrganisationsLink(OrganisationSchemeVersion itemSchemeVersion) {
        return toItemsLink(toSupathItemSchemes(itemSchemeVersion.getOrganisationSchemeType()), toSubpathItems(itemSchemeVersion.getOrganisationSchemeType()), itemSchemeVersion);
    }
    private String toOrganisationsLink(com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation item) {
        return toItemsLink(toSupathItemSchemes(item.getOrganisationType()), toSubpathItems(item.getOrganisationType()), item.getItemSchemeVersion());
    }
    private String toOrganisationLink(com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation item) {
        return toItemLink(toSupathItemSchemes(item.getOrganisationType()), toSubpathItems(item.getOrganisationType()), item);
    }
    private String toOrganisationLink(ItemResult item, ItemSchemeVersion itemSchemeVersion) {
        OrganisationTypeEnum organisationType = getOrganisationTypeEnum(item);
        return toItemLink(toSupathItemSchemes(organisationType), toSubpathItems(organisationType), item, itemSchemeVersion);
    }

    private String toSupathItemSchemes(OrganisationSchemeTypeEnum type) {
        if (type == null) {
            return RestInternalConstants.LINK_SUBPATH_ORGANISATION_SCHEMES;
        }
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
                throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + type);
        }
    }

    private String toSupathItemSchemes(OrganisationTypeEnum type) {
        return toSupathItemSchemes(SrmRestInternalUtils.toOrganisationSchemeType(type));
    }

    private String toSubpathItems(OrganisationSchemeTypeEnum type) {
        if (type == null) {
            return RestInternalConstants.LINK_SUBPATH_ORGANISATIONS;
        }
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
                throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + type);
        }
    }

    private String toSubpathItems(OrganisationTypeEnum type) {
        return toSubpathItems(SrmRestInternalUtils.toOrganisationSchemeType(type));
    }

    private String toKindItemSchemes(OrganisationSchemeTypeEnum type) {
        if (type == null) {
            return RestInternalConstants.KIND_ORGANISATION_SCHEMES;
        }
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
                throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + type);
        }
    }

    private String toKindItems(OrganisationSchemeTypeEnum type) {
        if (type == null) {
            return RestInternalConstants.KIND_ORGANISATIONS;
        }
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
                throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + type);
        }
    }

    private String toKindItemsFromOrganisationType(OrganisationTypeEnum type) {
        return toKindItems(SrmRestInternalUtils.toOrganisationSchemeType(type));
    }

    private String toKindItemScheme(OrganisationSchemeTypeEnum type) {
        if (type == null) {
            throw new IllegalArgumentException("OrganisationSchemeTypeEnum unsuported: " + type);
        }
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
        if (type == null) {
            throw new IllegalArgumentException("OrganisationTypeEnum unsuported: " + type);
        }
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

    private String toOrganisationSchemeManagementApplicationLink(OrganisationSchemeVersionMetamac source) {
        return getInternalWebApplicationNavigation().buildOrganisationSchemeUrl(source);
    }

    private String toOrganisationManagementApplicationLink(OrganisationMetamac source) {
        return getInternalWebApplicationNavigation().buildOrganisationUrl(source);
    }

    private String toOrganisationManagementApplicationLink(OrganisationSchemeVersion organisationSchemeVersion, ItemResult source, OrganisationTypeEnum organisationType) {
        return getInternalWebApplicationNavigation().buildOrganisationUrl(organisationSchemeVersion.getMaintainableArtefact().getUrn(), source.getCode(), organisationType);
    }

    private OrganisationTypeEnum getOrganisationTypeEnum(ItemResult source) {
        return ((OrganisationResultExtensionPoint) source.getExtensionPoint()).getOrganisationType();
    }

    private String getIdAsMaintainer(ItemResult source) {
        return ((OrganisationResultExtensionPoint) source.getExtensionPoint()).getIdAsMaintainer();
    }
}