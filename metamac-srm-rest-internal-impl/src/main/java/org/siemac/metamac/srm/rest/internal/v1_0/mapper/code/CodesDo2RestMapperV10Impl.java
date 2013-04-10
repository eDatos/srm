package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import java.math.BigInteger;

import javax.ws.rs.core.Response.Status;

import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AccessType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ReplaceToCodelist;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@Component
public class CodesDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl implements CodesDo2RestMapperV10 {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbMapper codesDo2JaxbSdmxMapper;

    @Autowired
    @Qualifier("codesDo2JaxbRestInternalCallbackMetamac")
    private CodesDo2JaxbCallback                                            codesDo2JaxbCallback;

    @Override
    public Codelists toCodelists(PagedResult<CodelistVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        Codelists targets = new Codelists();
        targets.setKind(RestInternalConstants.KIND_CODELISTS);

        // Pagination
        String baseLink = toCodelistsLink(agencyID, resourceID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CodelistVersionMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getCodelists().add(target);
        }
        return targets;
    }

    @Override
    public Codelist toCodelist(CodelistVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        // following method will call toCodelist(CodelistVersionMetamac source, Codelist target) method, thank to callback
        return (Codelist) codesDo2JaxbSdmxMapper.codelistDoToJaxb(source, codesDo2JaxbCallback);
    }

    @Override
    public void toCodelist(CodelistVersionMetamac source, Codelist target) {
        if (source == null) {
            return;
        }
        target.setKind(RestInternalConstants.KIND_CODELIST);
        target.setSelfLink(toCodelistSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toCodelistParentLink(source));
        target.setChildLinks(toCodelistChildLinks(source));

        target.setShortName(toInternationalString(source.getShortName()));
        target.setDescriptionSource(toInternationalString(source.getDescriptionSource()));
        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setIsRecommended(source.getIsRecommended());
        target.setFamily(toItem(source.getFamily()));
        target.setVariable(toItem(source.getVariable()));
        target.setAccessType(toAccessType(source.getAccessType()));
        target.setDefaultOrderVisualisation(source.getDefaultOrderVisualisation().getNameableArtefact().getCode());
        target.setDefaultOpennessVisualisation(source.getDefaultOpennessVisualisation().getNameableArtefact().getCode());
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));
        target.setReplacedBy(toCodelistReplacedBy(source));
        target.setReplaceTo(toCodelistReplaceTo(source));
    }

    @Override
    public Codes toCodes(PagedResult<CodeMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit) {

        Codes targets = new Codes();
        targets.setKind(RestInternalConstants.KIND_CODES);

        // Pagination
        String baseLink = toCodesLink(agencyID, resourceID, version);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CodeMetamac source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getCodes().add(target);
        }
        return targets;
    }

    @Override
    public Code toCode(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        Code target = new Code();
        codesDo2JaxbSdmxMapper.codeDoToJaxb(source, target);

        target.setKind(RestInternalConstants.KIND_CODE);
        target.setSelfLink(toCodeSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toCodeParentLink(source));
        target.setChildLinks(toCodeChildLinks(source));

        target.setComment(toInternationalString(source.getNameableArtefact().getComment()));
        if (source.getVariableElement() == null) {
            target.setShortName(toInternationalString(source.getShortName()));
        } else {
            target.setShortName(toInternationalString(source.getVariableElement().getShortName()));
        }
        target.setVariableElement(toItem(source.getVariableElement()));

        return target;
    }

    @Override
    public void toCode(com.arte.statistic.sdmx.srm.core.code.domain.Code source, CodeType target) {
        if (source == null) {
            return;
        }
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(toCodeSelfLink(source).getHref());
        }
    }

    @Override
    public void toCode(ItemResult source, ItemSchemeVersion itemSchemeVersion, CodeType target) {
        if (source == null) {
            return;
        }
        if (SrmRestInternalUtils.uriMustBeSelfLink(itemSchemeVersion.getMaintainableArtefact())) {
            target.setUri(toCodeLink(itemSchemeVersion, source));
        }
    }

    @Override
    public Item toItem(Variable source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(source.getNameableArtefact().getCode());
        target.setTitle(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    @Override
    protected boolean canResourceBeProvidedByApiConcreteResource(Object source) {
        CodelistVersionMetamac codelistVersion = (CodelistVersionMetamac) source;
        return AccessTypeEnum.PUBLIC.equals(codelistVersion.getAccessType());
    }

    private Item toItem(VariableElement source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(source.getIdentifiableArtefact().getCode());
        target.setTitle(toInternationalString(source.getShortName()));
        return target;
    }

    private Item toItem(CodelistFamily source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(source.getNameableArtefact().getCode());
        target.setTitle(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private ResourceLink toCodelistSelfLink(CodelistVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_CODELIST, toCodelistLink(source));
    }

    private ResourceLink toCodelistParentLink(CodelistVersionMetamac source) {
        return toResourceLink(RestInternalConstants.KIND_CODELISTS, toCodelistsLink(null, null, null));
    }

    private ChildLinks toCodelistChildLinks(CodelistVersionMetamac source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(RestInternalConstants.KIND_CODES, toCodesLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toCodeSelfLink(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        return toResourceLink(RestInternalConstants.KIND_CODE, toCodeLink(source));
    }

    private ResourceLink toCodeParentLink(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        return toResourceLink(RestInternalConstants.KIND_CODES, toCodesLink(source.getItemSchemeVersion()));
    }

    private ChildLinks toCodeChildLinks(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        // nothing
        return null;
    }

    private Resource toResource(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), RestInternalConstants.KIND_CODELIST, toCodelistSelfLink(source));
    }

    private Resource toResource(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getNameableArtefact(), RestInternalConstants.KIND_CODE, toCodeSelfLink(source));
    }

    private String toCodelistsLink(String agencyID, String resourceID, String version) {
        return toMaintainableArtefactLink(toSubpathItemSchemes(), agencyID, resourceID, version);
    }
    private String toCodelistLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemSchemeLink(toSubpathItemSchemes(), itemSchemeVersion);
    }
    private String toCodesLink(String agencyID, String resourceID, String version) {
        return toItemsLink(toSubpathItemSchemes(), toSubpathItems(), agencyID, resourceID, version);
    }
    private String toCodesLink(ItemSchemeVersion itemSchemeVersion) {
        return toItemsLink(toSubpathItemSchemes(), toSubpathItems(), itemSchemeVersion);
    }
    private String toCodeLink(com.arte.statistic.sdmx.srm.core.base.domain.Item item) {
        return toItemLink(toSubpathItemSchemes(), toSubpathItems(), item);
    }
    private String toCodeLink(ItemSchemeVersion itemSchemeVersion, ItemResult item) {
        String link = toItemsLink(toSubpathItemSchemes(), toSubpathItems(), itemSchemeVersion);
        link = RestUtils.createLink(link, item.getCode());
        return link;
    }

    private String toSubpathItemSchemes() {
        return RestInternalConstants.LINK_SUBPATH_CODELISTS;
    }
    private String toSubpathItems() {
        return RestInternalConstants.LINK_SUBPATH_CODES;
    }

    private AccessType toAccessType(AccessTypeEnum source) {
        switch (source) {
            case PUBLIC:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AccessType.PUBLIC;
            case RESTRICTED:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AccessType.RESTRICTED;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Resource toCodelistReplacedBy(CodelistVersionMetamac source) {
        if (source.getReplacedByCodelist() == null) {
            return null;
        }
        if (!canResourceBeProvidedByApi(source.getReplacedByCodelist())) {
            return null;
        }
        return toResource(source.getReplacedByCodelist());
    }

    private ReplaceToCodelist toCodelistReplaceTo(CodelistVersionMetamac source) {
        ReplaceToCodelist target = null;
        for (CodelistVersionMetamac replaceToCodelist : source.getReplaceToCodelists()) {
            if (canResourceBeProvidedByApi(replaceToCodelist)) { // note: this check is not necessary really, because in core is checked. It is added to avoid future problems
                if (target == null) {
                    target = new ReplaceToCodelist();

                }
                target.getReplaceTos().add(toResource(replaceToCodelist));
            }
        }
        if (target != null) {
            target.setKind(RestInternalConstants.KIND_CODELISTS);
            target.setTotal(BigInteger.valueOf(target.getReplaceTos().size()));
        }
        return target;
    }
}