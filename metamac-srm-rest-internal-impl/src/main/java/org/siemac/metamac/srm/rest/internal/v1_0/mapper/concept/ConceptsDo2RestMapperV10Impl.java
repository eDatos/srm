package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseDo2RestMapperV10Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2JaxbCallback;

@Component
public class ConceptsDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl implements ConceptsDo2RestMapperV10 {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2JaxbMapper conceptsDo2JaxbSdmxMapper;

    @Autowired
    @Qualifier("conceptsDo2JaxbCallbackMetamac")
    private ConceptsDo2JaxbCallback                                               conceptsDo2JaxbCallback;

    @Override
    public ConceptSchemes toConceptSchemes(PagedResult<ConceptSchemeVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        ConceptSchemes targets = new ConceptSchemes();
        targets.setKind(RestInternalConstants.KIND_CONCEPT_SCHEMES);

        // Pagination
        String baseLink = toConceptSchemesLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (ConceptSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getConceptSchemes().add(target);
        }
        return targets;
    }

    @Override
    public ConceptScheme toConceptScheme(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toConceptScheme(ConceptSchemeVersionMetamac source, ConceptScheme target) method, thank to callback
        return (ConceptScheme) conceptsDo2JaxbSdmxMapper.conceptSchemeDoToJaxb(source, conceptsDo2JaxbCallback);
    }

    @Override
    public void toConceptScheme(ConceptSchemeVersionMetamac source, ConceptScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(RestInternalConstants.KIND_CONCEPT_SCHEME);
        target.setSelfLink(toConceptSchemeSelfLink(source));
        target.setUri(target.getSelfLink().getHref());
        target.setType(toConceptSchemeTypeEnum(source.getType()));
        target.setRelatedOperation(toResourceExternalItemStatisticalOperation(source.getRelatedOperation()));
        target.setReplaceToVersion(source.getMaintainableArtefact().getReplaceToVersion());
        target.setParentLink(toConceptSchemeParentLink(source));
        target.setChildLinks(toConceptSchemeChildLinks(source));
    }

    @Override
    public Concepts toConcepts(PagedResult<ConceptMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        Concepts targets = new Concepts();
        targets.setKind(RestInternalConstants.KIND_CONCEPTS);

        // Pagination
        String baseLink = toConceptsLink(agencyID, resourceID, version);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (ConceptMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
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
        conceptsDo2JaxbSdmxMapper.conceptDoToJaxb(source, target);

        target.setKind(RestInternalConstants.KIND_CONCEPT);
        target.setSelfLink(toConceptSelfLink(source));
        target.setUri(target.getSelfLink().getHref());

        target.setPluralName(toInternationalString(source.getPluralName()));
        target.setAcronym(toInternationalString(source.getAcronym()));
        target.setDescriptionSource(toInternationalString(source.getDescriptionSource()));
        target.setContext(toInternationalString(source.getContext()));
        target.setDocMethod(toInternationalString(source.getDocMethod()));
        target.setType(toItem(source.getType()));
        target.setDerivation(toInternationalString(source.getDerivation()));
        target.setLegalActs(toInternationalString(source.getLegalActs()));
        if (source.getConceptExtends() != null) {
            target.setExtends(source.getConceptExtends().getNameableArtefact().getUrn());
        }
        target.setRoles(itemsToUrns(source.getRoleConcepts()));
        target.setRelatedConcepts(itemsToUrns(source.getRelatedConcepts()));

        target.setParentLink(toConceptParentLink(source));
        target.setChildLinks(toConceptChildLinks(source));

        return target;
    }

    @Override
    public ConceptTypes toConceptTypes(List<ConceptType> sources) {
        ConceptTypes targets = new ConceptTypes();
        targets.setKind(RestInternalConstants.KIND_CONCEPT_TYPES);

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

    private ResourceLink toConceptSchemeSelfLink(ConceptSchemeVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_CONCEPT_SCHEME, toConceptSchemeLink(source));
    }

    private ResourceLink toConceptSchemeParentLink(ConceptSchemeVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_CONCEPT_SCHEMES, toConceptSchemesLink(null, null, null));
    }

    private ChildLinks toConceptSchemeChildLinks(ConceptSchemeVersionMetamac source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(RestInternalConstants.KIND_CONCEPTS, toConceptsLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toConceptSelfLink(ConceptMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_CONCEPT, toConceptLink(source));
    }

    private ResourceLink toConceptParentLink(ConceptMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_CONCEPTS, toConceptsLink(source.getItemSchemeVersion()));
    }

    private ChildLinks toConceptChildLinks(ConceptMetamac source) {
        // nothing
        return null;
    }

    private org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeType toConceptSchemeTypeEnum(ConceptSchemeTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case GLOSSARY:
                return org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeType.GLOSSARY;
            case OPERATION:
                return org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeType.OPERATION;
            case ROLE:
                return org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeType.ROLE;
            case TRANSVERSAL:
                return org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemeType.TRANSVERSAL;
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
        target.setTitle(toInternationalString(source.getDescription()));
        return target;
    }

    private Resource toResource(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), RestInternalConstants.KIND_CONCEPT_SCHEME, toConceptSchemeSelfLink(source));
    }

    private Resource toResource(ConceptMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getNameableArtefact(), RestInternalConstants.KIND_CONCEPT, toConceptSelfLink(source));
    }

    private String toConceptSchemesLink(String agencyID, String resourceID, String version) {
        return toItemSchemesLink(toSubpathItemSchemes(), agencyID, resourceID, version);
    }
    private String toConceptSchemeLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemSchemeLink(toSubpathItemSchemes(), itemSchemeVersion);
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

    private String toSubpathItemSchemes() {
        return RestInternalConstants.LINK_SUBPATH_CONCEPT_SCHEMES;
    }
    private String toSubpathItems() {
        return RestInternalConstants.LINK_SUBPATH_CONCEPTS;
    }
}