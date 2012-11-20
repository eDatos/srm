package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.constants.RestEndpointsConstants;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concept;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Concepts;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.Urns;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2JaxbCallback;

@Component
public class ConceptsDo2RestMapperV10Impl implements ConceptsDo2RestMapperV10 {

    @Autowired
    private ConfigurationService                                                  configurationService;

    @Autowired
    private com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2JaxbMapper conceptsDo2JaxbSdmxMapper;

    @Autowired
    @Qualifier("conceptsDo2JaxbCallbackMetamac")
    private ConceptsDo2JaxbCallback                                               conceptsDo2JaxbCallback;

    private String                                                                srmApiInternalEndpointV10;
    private String                                                                statisticalOperationsApiInternalEndpoint;

    @PostConstruct
    public void init() throws Exception {
        // Srm Internal Api V1.0
        String srmApiInternalEndpoint = readProperty(RestEndpointsConstants.SRM_INTERNAL_API);
        srmApiInternalEndpointV10 = RestUtils.createLink(srmApiInternalEndpoint, RestInternalConstants.API_VERSION_1_0);

        // Statistical operations Internal Api
        statisticalOperationsApiInternalEndpoint = readProperty(RestEndpointsConstants.STATISTICAL_OPERATIONS_INTERNAL_API);
    }

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
        target.setRoles(conceptsToUrns(source.getRoleConcepts()));
        target.setRelatedConcepts(conceptsToUrns(source.getRelatedConcepts()));

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

    private Resource toResourceExternalItemStatisticalOperation(ExternalItem source) {
        if (source == null) {
            return null;
        }
        return toResourceExternalItem(source, statisticalOperationsApiInternalEndpoint);
    }

    private Resource toResourceExternalItem(ExternalItem source, String apiExternalItem) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(source.getCode());
        target.setUrn(source.getUrn());
        target.setKind(source.getType().name());
        target.setSelfLink(toResourceLink(target.getKind(), RestUtils.createLink(apiExternalItem, source.getUri())));
        target.setTitle(toInternationalString(source.getTitle()));
        return target;
    }

    private Resource toResource(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(source.getMaintainableArtefact().getCode());
        target.setUrn(source.getMaintainableArtefact().getUrn());
        target.setKind(RestInternalConstants.KIND_CONCEPT_SCHEME);
        target.setSelfLink(toConceptSchemeSelfLink(source));
        target.setTitle(toInternationalString(source.getMaintainableArtefact().getName()));
        return target;
    }

    private Resource toResource(ConceptMetamac source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(source.getNameableArtefact().getCode());
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setKind(RestInternalConstants.KIND_CONCEPT);
        target.setSelfLink(toConceptSelfLink(source));
        target.setTitle(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private InternationalString toInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString sources) {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString source : sources.getTexts()) {
            LocalisedString target = new LocalisedString();
            target.setValue(source.getLabel());
            target.setLang(source.getLocale());
            targets.getTexts().add(target);
        }
        return targets;
    }

    // API/conceptschemes
    // API/conceptschemes/{agencyID}
    // API/conceptschemes/{agencyID}/{resourceID}
    // API/conceptschemes/{agencyID}/{resourceID}/{version}
    private String toConceptSchemesLink(String agencyID, String resourceID, String version) {
        String link = RestUtils.createLink(srmApiInternalEndpointV10, RestInternalConstants.LINK_SUBPATH_CONCEPT_SCHEMES);
        if (agencyID != null) {
            link = RestUtils.createLink(link, agencyID);
            if (resourceID != null) {
                link = RestUtils.createLink(link, resourceID);
                if (version != null) {
                    link = RestUtils.createLink(link, version);
                }
            }
        }
        return link;
    }

    // API/conceptschemes/{agencyID}/{resourceID}/{version}
    private String toConceptSchemeLink(String agencyID, String resourceID, String version) {
        return toConceptSchemesLink(agencyID, resourceID, version);
    }
    private String toConceptSchemeLink(ItemSchemeVersion conceptSchemeVersionMetamac) {
        MaintainableArtefact maintainableArtefact = conceptSchemeVersionMetamac.getMaintainableArtefact();
        return toConceptSchemeLink(maintainableArtefact.getMaintainer().getIdAsMaintainer(), maintainableArtefact.getCode(), maintainableArtefact.getVersionLogic());
    }

    // API/conceptschemes/{agencyID}/{resourceID}/{version}/concepts
    private String toConceptsLink(String agencyID, String resourceID, String version) {
        String link = toConceptSchemeLink(agencyID, resourceID, version);
        link = RestUtils.createLink(link, RestInternalConstants.LINK_SUBPATH_CONCEPTS);
        return link;
    }
    private String toConceptsLink(ItemSchemeVersion conceptSchemeVersionMetamac) {
        MaintainableArtefact maintainableArtefact = conceptSchemeVersionMetamac.getMaintainableArtefact();
        return toConceptsLink(maintainableArtefact.getMaintainer().getIdAsMaintainer(), maintainableArtefact.getCode(), maintainableArtefact.getVersionLogic());
    }

    // API/conceptschemes/{agencyID}/{resourceID}/{version}/concepts
    private String toConceptLink(ConceptMetamac conceptMetamac) {
        String link = toConceptsLink(conceptMetamac.getItemSchemeVersion());
        link = RestUtils.createLink(link, conceptMetamac.getNameableArtefact().getCode());
        return link;
    }

    private String readProperty(String property) {
        String propertyValue = configurationService.getProperty(property);
        if (propertyValue == null) {
            throw new BeanCreationException("Property not found: " + property);
        }
        return propertyValue;
    }

    private Urns conceptsToUrns(List<ConceptMetamac> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        Urns target = new Urns();
        target.setKind("TODO"); // TODO kind
        for (ConceptMetamac source : sources) {
            // TODO utilidad para obtener urn
            if (source.getNameableArtefact().getUrnProvider() != null) {
                target.getUrns().add(source.getNameableArtefact().getUrnProvider());
            } else {
                target.getUrns().add(source.getNameableArtefact().getUrn());
            }
        }
        target.setTotal(BigInteger.valueOf(target.getUrns().size()));
        return target;
    }

    private ResourceLink toResourceLink(String kind, String href) {
        ResourceLink target = new ResourceLink();
        target.setKind(kind);
        target.setHref(href);
        return target;
    }
}