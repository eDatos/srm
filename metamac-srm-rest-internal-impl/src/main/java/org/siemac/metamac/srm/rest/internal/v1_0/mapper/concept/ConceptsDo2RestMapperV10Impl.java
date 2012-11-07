package org.siemac.metamac.srm.rest.internal.v1_0.mapper.concept;

import javax.annotation.PostConstruct;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.constants.RestEndpointsConstants;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.srm_internal.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;

public class ConceptsDo2RestMapperV10Impl implements ConceptsDo2RestMapperV10 {

    @Autowired
    private ConfigurationService configurationService;

    private String               srmApiInternalEndpointV10;

    @PostConstruct
    public void init() throws Exception {
        // Statistical operations Internal Api
        String srmApiInternalEndpoint = configurationService.getProperty(RestEndpointsConstants.SRM_INTERNAL_API);
        if (srmApiInternalEndpoint == null) {
            throw new BeanCreationException("Property not found: " + RestEndpointsConstants.SRM_INTERNAL_API);
        }
        srmApiInternalEndpointV10 = RestUtils.createLink(srmApiInternalEndpoint, RestInternalConstants.API_VERSION_1_0);
    }

    @Override
    public ConceptSchemes toConceptSchemes(PagedResult<ConceptSchemeVersionMetamac> sourcesPagedResult, String query, String orderBy, Integer limit) {

        ConceptSchemes targets = new ConceptSchemes();
        targets.setKind(RestInternalConstants.KIND_CONCEPT_SCHEMES);

        // Pagination
        String baseLink = toConceptSchemesLink();
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (ConceptSchemeVersionMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getConceptSchemes().add(target);
        }
        return targets;
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
    private String toConceptSchemesLink() {
        return RestUtils.createLink(srmApiInternalEndpointV10, RestInternalConstants.LINK_SUBPATH_CONCEPT_SCHEMES);
    }

    // API/conceptschemes/{agencyID}/{resourceID}/{version}
    private String toConceptSchemeLink(ConceptSchemeVersionMetamac conceptSchemeVersionMetamac) {
        String linkConceptScheme = toConceptSchemesLink();
        linkConceptScheme = RestUtils.createLink(linkConceptScheme, conceptSchemeVersionMetamac.getMaintainableArtefact().getMaintainer().getIdAsMaintainer());
        linkConceptScheme = RestUtils.createLink(linkConceptScheme, conceptSchemeVersionMetamac.getMaintainableArtefact().getCode());
        linkConceptScheme = RestUtils.createLink(linkConceptScheme, conceptSchemeVersionMetamac.getMaintainableArtefact().getVersionLogic());
        return linkConceptScheme;
    }
}