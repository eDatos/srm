package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.rest.common.v1_0.domain.Children;
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
        return (ConceptScheme) conceptsDo2JaxbSdmxMapper.conceptSchemeDoToJaxb(source, conceptsDo2JaxbCallback);
    }

    @Override
    public void toConceptScheme(ConceptSchemeVersionMetamac source, ConceptScheme target) {
        if (source == null) {
            return;
        }
        target.setKind(RestInternalConstants.KIND_CONCEPT_SCHEME);
        target.setSelfLink(toConceptSchemeLink(source));
        target.setUri(target.getSelfLink());
        target.setType(toConceptSchemeTypeEnum(source.getType()));
        target.setRelatedOperation(toResourceExternalItemStatisticalOperation(source.getRelatedOperation()));
        target.setReplacedBy(source.getMaintainableArtefact().getReplacedBy());
        target.setReplaceTo(source.getMaintainableArtefact().getReplaceTo());
        target.setParent(toConceptSchemeParent());
        target.setChildren(toConceptSchemeChildren(source));
    }

    @Override
    public void toConcept(ConceptMetamac source, Concept target) {
        if (source == null) {
            return;
        }
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
    }

    private ResourceLink toConceptSchemeParent() {
        ResourceLink target = new ResourceLink();
        target.setKind(RestInternalConstants.KIND_CONCEPT_SCHEMES);
        target.setSelfLink(toConceptSchemesLink(null, null, null));
        return target;
    }

    private Children toConceptSchemeChildren(ConceptSchemeVersionMetamac source) {
        Children targets = new Children();

        // Concepts
        ResourceLink conceptsTarget = new ResourceLink();
        conceptsTarget.setKind(RestInternalConstants.KIND_CONCEPTS);
        conceptsTarget.setSelfLink(toConceptsLink(source));
        targets.getChildren().add(conceptsTarget);

        targets.setTotal(BigInteger.valueOf(targets.getChildren().size()));
        return targets;
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
        target.setSelfLink(RestUtils.createLink(apiExternalItem, source.getUri()));
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
        target.setSelfLink(toConceptSchemeLink(source));
        target.setTitle(toInternationalString(source.getMaintainableArtefact().getName()));
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
    private String toConceptSchemeLink(ConceptSchemeVersionMetamac conceptSchemeVersionMetamac) {
        MaintainableArtefact maintainableArtefact = conceptSchemeVersionMetamac.getMaintainableArtefact();
        return toConceptSchemesLink(maintainableArtefact.getMaintainer().getIdAsMaintainer(), maintainableArtefact.getCode(), maintainableArtefact.getVersionLogic());
    }

    // API/conceptschemes/{agencyID}/{resourceID}/{version}/concepts
    private String toConceptsLink(ConceptSchemeVersionMetamac conceptSchemeVersionMetamac) {
        String link = toConceptSchemeLink(conceptSchemeVersionMetamac);
        link = RestUtils.createLink(link, RestInternalConstants.LINK_SUBPATH_CONCEPTS);
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
            target.getUrns().add(source.getNameableArtefact().getUrn()); // TODO urn provider?
        }
        target.setTotal(BigInteger.valueOf(target.getUrns().size()));
        return target;
    }
}