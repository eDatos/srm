package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Quantity;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityAmount;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityChangeRate;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityFraction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityIndex;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityMagnitude;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityRate;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityRatio;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RelatedConcepts;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.RoleConcepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.shared.QuantityUtils;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.ItemSchemeBaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.code.CodesDo2RestMapperV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;

@Component
public class ConceptsDo2RestMapperV10Impl extends ItemSchemeBaseDo2RestMapperV10Impl implements ConceptsDo2RestMapperV10 {

    @Autowired
    private CodesDo2RestMapperV10 codesDo2RestMapper;

    @Override
    public ConceptSchemes toConceptSchemes(PagedResult<ConceptSchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        ConceptSchemes targets = new ConceptSchemes();
        targets.setKind(SrmRestConstants.KIND_CONCEPT_SCHEMES);

        // Pagination
        String baseLink = toConceptSchemesLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (ConceptSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getConceptSchemes().add(target);
        }
        return targets;
    }

    @Override
    public ConceptScheme toConceptScheme(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        ConceptScheme target = new ConceptScheme();
        target.setKind(SrmRestConstants.KIND_CONCEPT_SCHEME);
        target.setSelfLink(toConceptSchemeSelfLink(source));
        target.setParentLink(toConceptSchemeParentLink(source));
        target.setChildLinks(toConceptSchemeChildLinks(source));
        target.setManagementAppLink(toConceptSchemeManagementApplicationLink(source));

        toItemScheme(source, source.getLifeCycleMetadata(), target);

        target.setType(toConceptSchemeTypeEnum(source.getType()));
        target.setStatisticalOperation(toResourceExternalItemStatisticalOperation(source.getRelatedOperation()));
        return target;
    }

    @Override
    public ResourceInternal toResource(ConceptSchemeVersion source) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        toResource(source.getMaintainableArtefact(), SrmRestConstants.KIND_CONCEPT_SCHEME, toConceptSchemeSelfLink(source), toConceptSchemeManagementApplicationLink(source), target);
        return target;
    }

    @Override
    public Concepts toConcepts(PagedResult<ConceptMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        Concepts targets = new Concepts();
        targets.setKind(SrmRestConstants.KIND_CONCEPTS);

        // Pagination
        String baseLink = toConceptsLink(agencyID, resourceID, version);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (ConceptMetamac source : sourcesPagedResult.getValues()) {
            ItemResourceInternal target = toResource(source);
            targets.getConcepts().add(target);
        }
        return targets;
    }

    @Override
    public Concepts toConcepts(List<ItemResult> sources, ConceptSchemeVersionMetamac conceptSchemeVersion) {

        Concepts targets = new Concepts();
        targets.setKind(SrmRestConstants.KIND_CONCEPTS);

        // No pagination
        targets.setSelfLink(toConceptsLink(conceptSchemeVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            ItemResourceInternal target = toResource(source, conceptSchemeVersion);
            targets.getConcepts().add(target);
        }
        return targets;
    }

    @Override
    public Concept toConcept(ConceptMetamac source) {
        if (source == null) {
            return null;
        }
        Concept target = new Concept();

        target.setKind(SrmRestConstants.KIND_CONCEPT);
        target.setSelfLink(toConceptSelfLink(source));
        target.setParentLink(toConceptParentLink(source));
        target.setChildLinks(toConceptChildLinks(source));
        target.setManagementAppLink(toConceptManagementApplicationLink(source));

        toItem(source, target);

        target.setCoreRepresentation(commonDo2RestMapper.toRepresentation(source.getCoreRepresentation()));
        target.setPluralName(toInternationalString(source.getPluralName()));
        target.setAcronym(toInternationalString(source.getAcronym()));
        target.setDescriptionSource(toInternationalString(source.getDescriptionSource()));
        target.setContext(toInternationalString(source.getContext()));
        target.setDocMethod(toInternationalString(source.getDocMethod()));
        target.setType(toItem(source.getConceptType()));
        target.setDerivation(toInternationalString(source.getDerivation()));
        target.setLegalActs(toInternationalString(source.getLegalActs()));
        target.setExtends(toResource(source.getConceptExtends()));
        target.setRoles(toRoleConcepts(source.getRoleConcepts()));
        target.setRelatedConcepts(toRelatedConcepts(source));
        target.setComment(toInternationalString(source.getNameableArtefact().getComment()));
        target.setVariable(codesDo2RestMapper.toResource(source.getVariable()));
        target.setQuantity(toQuantity(source.getQuantity()));

        return target;
    }

    @Override
    public ItemResourceInternal toResource(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source) {
        if (source == null) {
            return null;
        }
        ItemResourceInternal target = new ItemResourceInternal();
        toResource(source, SrmRestConstants.KIND_CONCEPT, toConceptSelfLink(source), toConceptManagementApplicationLink(source), target);
        return target;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public RoleConcepts toRoleConcepts(Collection sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        RoleConcepts targets = new RoleConcepts();
        targets.setKind(SrmRestConstants.KIND_CONCEPTS);

        for (Iterator iterator = sources.iterator(); iterator.hasNext();) {
            com.arte.statistic.sdmx.srm.core.concept.domain.Concept source = (com.arte.statistic.sdmx.srm.core.concept.domain.Concept) iterator.next();
            ItemResourceInternal target = toResource(source);
            targets.getRoles().add(target);
        }
        targets.setTotal(BigInteger.valueOf(targets.getRoles().size()));
        return targets;
    }

    @Override
    public ConceptTypes toConceptTypes(List<ConceptType> sources) {
        ConceptTypes targets = new ConceptTypes();
        targets.setKind(SrmRestConstants.KIND_CONCEPT_TYPES);

        if (sources == null) {
            targets.setTotal(BigInteger.ZERO);
        } else {
            for (ConceptType source : sources) {
                Item target = toItem(source);
                targets.getConceptTypes().add(target);
            }
            targets.setTotal(BigInteger.valueOf(sources.size()));
        }
        return targets;
    }

    @Override
    protected boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source) {
        return true; // no additional conditions
    }

    private ResourceLink toConceptSchemeSelfLink(ConceptSchemeVersion source) {
        return toResourceLink(SrmRestConstants.KIND_CONCEPT_SCHEME, toConceptSchemeLink(source));
    }

    @Override
    public ResourceLink toConceptSchemeSelfLink(String agencyID, String resourceID, String version) {
        return toResourceLink(SrmRestConstants.KIND_CONCEPT_SCHEME, toConceptSchemeLink(agencyID, resourceID, version));
    }

    private ResourceLink toConceptSchemeParentLink(ConceptSchemeVersionMetamac source) {
        return toResourceLink(SrmRestConstants.KIND_CONCEPT_SCHEMES, toConceptSchemesLink(null, null, null));
    }

    private ChildLinks toConceptSchemeChildLinks(ConceptSchemeVersionMetamac source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(SrmRestConstants.KIND_CONCEPTS, toConceptsLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toConceptSelfLink(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source) {
        return toResourceLink(SrmRestConstants.KIND_CONCEPT, toConceptLink(source));
    }

    private ResourceLink toConceptSelfLink(ItemResult source, ConceptSchemeVersion conceptSchemeVersion) {
        return toResourceLink(SrmRestConstants.KIND_CONCEPT, toConceptLink(source, conceptSchemeVersion));
    }

    private ResourceLink toConceptParentLink(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source) {
        return toResourceLink(SrmRestConstants.KIND_CONCEPTS, toConceptsLink(source.getItemSchemeVersion()));
    }

    private ChildLinks toConceptChildLinks(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source) {
        // nothing
        return null;
    }

    private org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeType toConceptSchemeTypeEnum(ConceptSchemeTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case GLOSSARY:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeType.GLOSSARY;
            case OPERATION:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeType.OPERATION;
            case ROLE:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeType.ROLE;
            case TRANSVERSAL:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeType.TRANSVERSAL;
            case MEASURE:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ConceptSchemeType.MEASURE;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Item toItem(ConceptType source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(source.getIdentifier());
        target.setName(toInternationalString(source.getDescription()));
        return target;
    }

    private ItemResourceInternal toResource(ItemResult source, ConceptSchemeVersionMetamac conceptSchemeVersion) {
        if (source == null) {
            return null;
        }
        ItemResourceInternal target = new ItemResourceInternal();
        toResource(source, SrmRestConstants.KIND_CONCEPT, toConceptSelfLink(source, conceptSchemeVersion), toConceptManagementApplicationLink(conceptSchemeVersion, source), target,
                conceptSchemeVersion.getMaintainableArtefact().getIsImported());
        return target;
    }

    private RelatedConcepts toRelatedConcepts(ConceptMetamac concept) {
        if (CollectionUtils.isEmpty(concept.getRelatedConcepts())) {
            return null;
        }
        RelatedConcepts targets = new RelatedConcepts();
        targets.setKind(SrmRestConstants.KIND_CONCEPTS);

        for (ConceptMetamac source : concept.getRelatedConcepts()) {
            ItemResourceInternal target = toResource(source);
            targets.getRelatedConcepts().add(target);
        }
        targets.setTotal(BigInteger.valueOf(targets.getRelatedConcepts().size()));
        return targets;
    }

    private Quantity toQuantity(org.siemac.metamac.srm.core.concept.domain.Quantity source) {
        if (source == null) {
            return null;
        }
        Quantity target = null;
        switch (source.getQuantityType()) {
            case QUANTITY:
                target = new Quantity();
                break;
            case AMOUNT:
                target = new QuantityAmount();
                break;
            case MAGNITUDE:
                target = new QuantityMagnitude();
                break;
            case FRACTION:
                target = new QuantityFraction();
                break;
            case RATIO:
                target = new QuantityRatio();
                break;
            case RATE:
                target = new QuantityRate();
                break;
            case INDEX:
                target = new QuantityIndex();
                break;
            case CHANGE_RATE:
                target = new QuantityChangeRate();
                break;
        }
        if (QuantityUtils.isQuantityOrExtension(source.getQuantityType())) {
            target.setUnitCode(codesDo2RestMapper.toResource(source.getUnitCode()));
            target.setUnitSymbolPosition(toQuantityUnitSymbolPosition(source.getUnitSymbolPosition()));
            target.setSignificantDigits(source.getSignificantDigits());
            target.setDecimalPlaces(source.getDecimalPlaces());
            target.setUnitMultiplier(source.getUnitMultiplier());
        }
        if (QuantityUtils.isAmountOrExtension(source.getQuantityType())) {
            // nothing
        }
        if (QuantityUtils.isMagnitudeOrExtension(source.getQuantityType())) {
            ((QuantityMagnitude) target).setMin(source.getMinimum());
            ((QuantityMagnitude) target).setMax(source.getMaximum());
        }
        if (QuantityUtils.isFractionOrExtension(source.getQuantityType())) {
            ((QuantityFraction) target).setNumerator(toResource(source.getNumerator()));
            ((QuantityFraction) target).setDenominator(toResource(source.getDenominator()));
        }
        if (QuantityUtils.isRatioOrExtension(source.getQuantityType())) {
            ((QuantityRatio) target).setIsPercentage(source.getIsPercentage());
            ((QuantityRatio) target).setPercentageOf(toInternationalString(source.getPercentageOf()));
        }
        if (QuantityUtils.isRateOrExtension(source.getQuantityType())) {
            // nothing
        }
        if (QuantityUtils.isIndexOrExtension(source.getQuantityType())) {
            ((QuantityIndex) target).setBaseValue(source.getBaseValue());
            ((QuantityIndex) target).setBaseTime(source.getBaseTime());
            ((QuantityIndex) target).setBaseLocation(codesDo2RestMapper.toResource(source.getBaseLocation()));
        }
        if (QuantityUtils.isChangeRateOrExtension(source.getQuantityType())) {
            ((QuantityChangeRate) target).setBaseQuantity(toResource(source.getBaseQuantity()));
        }
        return target;
    }

    private String toConceptSchemesLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathItemSchemes(), agencyID, resourceID, version);
    }

    private String toConceptSchemeLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemSchemeLink(toSubpathItemSchemes(), itemSchemeVersion);
    }

    private String toConceptSchemeLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathItemSchemes(), agencyID, resourceID, version);
    }

    private String toConceptsLink(String agencyID, String resourceID, String version) {
        return toItemsLink(toSubpathItemSchemes(), toSubpathItems(), agencyID, resourceID, version);
    }

    private String toConceptsLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemsLink(toSubpathItemSchemes(), toSubpathItems(), itemSchemeVersion);
    }

    private String toConceptLink(com.arte.statistic.sdmx.srm.core.base.domain.Item item) {
        return toItemLink(toSubpathItemSchemes(), toSubpathItems(), item);
    }

    private String toConceptLink(ItemResult item, ItemSchemeVersion itemSchemeVersion) {
        return toItemLink(toSubpathItemSchemes(), toSubpathItems(), item, itemSchemeVersion);
    }

    private String toSubpathItemSchemes() {
        return SrmRestConstants.LINK_SUBPATH_CONCEPT_SCHEMES;
    }

    private String toSubpathItems() {
        return SrmRestConstants.LINK_SUBPATH_CONCEPTS;
    }

    private String toConceptSchemeManagementApplicationLink(ConceptSchemeVersion source) {
        return getInternalWebApplicationNavigation().buildConceptSchemeUrl(source);
    }

    @Override
    public String toConceptSchemeManagementApplicationLink(String conceptSchemeUrn) {
        return getInternalWebApplicationNavigation().buildConceptSchemeUrl(conceptSchemeUrn);
    }

    private String toConceptManagementApplicationLink(com.arte.statistic.sdmx.srm.core.concept.domain.Concept source) {
        return getInternalWebApplicationNavigation().buildConceptUrl(source);
    }

    private String toConceptManagementApplicationLink(ConceptSchemeVersion conceptSchemeVersion, ItemResult source) {
        return getInternalWebApplicationNavigation().buildConceptUrl(conceptSchemeVersion.getMaintainableArtefact().getUrn(), source.getCode());
    }

    private org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityUnitSymbolPosition toQuantityUnitSymbolPosition(QuantityUnitSymbolPositionEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case START:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityUnitSymbolPosition.START;
            case END:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityUnitSymbolPosition.END;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }
}