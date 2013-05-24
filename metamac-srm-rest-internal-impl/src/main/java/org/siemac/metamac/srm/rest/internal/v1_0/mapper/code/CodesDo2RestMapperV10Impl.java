package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import java.math.BigInteger;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.sdmx.resources.sdmxml.schemas.v2_1.structure.CodeType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AccessType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ReplaceToCodelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ReplaceToVariable;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilyMetadata;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variables;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.ItemSchemeBaseDo2RestMapperV10Impl;
import org.siemac.metamac.srm.rest.internal.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@Component
public class CodesDo2RestMapperV10Impl extends ItemSchemeBaseDo2RestMapperV10Impl implements CodesDo2RestMapperV10 {

    private final boolean                                                   AS_STUB               = false;
    private final boolean                                                   WITH_LOCAL_REFERENCES = false;

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
            ResourceInternal target = toResource(source);
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
        return (Codelist) codesDo2JaxbSdmxMapper.codelistDoToJaxb(source, codesDo2JaxbCallback, AS_STUB, WITH_LOCAL_REFERENCES);
    }

    @Override
    public void toCodelist(CodelistVersionMetamac source, Codelist target) {
        if (source == null) {
            return;
        }
        target.setKind(RestInternalConstants.KIND_CODELIST);
        target.setSelfLink(toCodelistSelfLink(source));
        target.setManagementAppLink(toCodelistManagementApplicationLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toCodelistParentLink(source));
        target.setChildLinks(toCodelistChildLinks(source));

        target.setShortName(toInternationalString(source.getShortName()));
        target.setDescriptionSource(toInternationalString(source.getDescriptionSource()));
        target.setComment(toInternationalString(source.getMaintainableArtefact().getComment()));
        target.setIsRecommended(source.getIsRecommended());
        target.setFamily(toResource(source.getFamily()));
        target.setVariable(toResource(source.getVariable()));
        target.setAccessType(toAccessType(source.getAccessType()));
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setLifeCycle(toLifeCycle(source.getLifeCycleMetadata()));
        target.setReplacedBy(toCodelistReplacedBy(source));
        target.setReplaceTo(toCodelistReplaceTo(source));
        target.setCreatedDate(toDate(source.getItemScheme().getResourceCreatedDate()));
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
            ResourceInternal target = toResource(source);
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
        codesDo2JaxbSdmxMapper.codeDoToJaxb(source, target, WITH_LOCAL_REFERENCES);

        target.setKind(RestInternalConstants.KIND_CODE);
        target.setSelfLink(toCodeSelfLink(source));
        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getItemSchemeVersion().getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
        target.setParentLink(toCodeParentLink(source));
        target.setChildLinks(toCodeChildLinks(source));
        target.setManagementAppLink(toCodeManagementApplicationLink(source));

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
    protected boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source) {
        CodelistVersionMetamac codelistVersion = (CodelistVersionMetamac) source;
        return AccessTypeEnum.PUBLIC.equals(codelistVersion.getAccessType());
    }

    @Override
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> sourcesPagedResult, String query, String orderBy, Integer limit) {

        VariableFamilies targets = new VariableFamilies();
        targets.setKind(RestInternalConstants.KIND_VARIABLE_FAMILIES);

        // Pagination
        String baseLink = toVariableFamiliesLink();
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.VariableFamily source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getVariableFamilies().add(target);
        }
        return targets;
    }

    @Override
    public VariableFamily toVariableFamily(org.siemac.metamac.srm.core.code.domain.VariableFamily source) throws MetamacException {
        if (source == null) {
            return null;
        }
        VariableFamily target = new VariableFamily();
        target.setId(source.getNameableArtefact().getCode());
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setKind(RestInternalConstants.KIND_VARIABLE_FAMILY);
        target.setSelfLink(toVariableFamilySelfLink(source));
        target.setChildLinks(toVariableFamilyChildLinks(source));
        target.setManagementAppLink(toVariableFamilyManagementApplicationLink(source));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }
    @Override
    public Variables toVariablesByFamily(String familyID, PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> sourcesPagedResult, String query, String orderBy, Integer limit) {

        Variables targets = new Variables();
        targets.setKind(RestInternalConstants.KIND_VARIABLES);

        // Pagination
        String baseLink = toVariablesByFamilyLink(familyID);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.Variable source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getVariables().add(target);
        }
        return targets;
    }

    @Override
    public Variables toVariables(PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> sourcesPagedResult, String query, String orderBy, Integer limit) {

        Variables targets = new Variables();
        targets.setKind(RestInternalConstants.KIND_VARIABLES);

        // Pagination
        String baseLink = toVariablesLink();
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.Variable source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getVariables().add(target);
        }
        return targets;
    }

    @Override
    public Variable toVariable(org.siemac.metamac.srm.core.code.domain.Variable source) throws MetamacException {
        if (source == null) {
            return null;
        }
        Variable target = new Variable();
        target.setId(source.getNameableArtefact().getCode());
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setKind(RestInternalConstants.KIND_VARIABLE);
        target.setSelfLink(toVariableSelfLink(source));
        target.setManagementAppLink(toVariableManagementApplicationLink(source));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        target.setShortName(toInternationalString(source.getShortName()));
        target.setValidFrom(toDate(source.getValidFrom()));
        target.setValidTo(toDate(source.getValidTo()));
        target.setReplacedBy(toResource(source.getReplacedByVariable()));
        target.setReplaceTo(toVariableReplaceTo(source));
        target.setFamily(toVariableFamilyMetadata(source));
        return target;
    }

    @Override
    public ResourceInternal toResource(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getNameableArtefact(), RestInternalConstants.KIND_VARIABLE, toVariableSelfLink(source), toVariableManagementApplicationLink(source));
    }

    @Override
    public CodelistFamilies toCodelistFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> sourcesPagedResult, String query, String orderBy, Integer limit) {

        CodelistFamilies targets = new CodelistFamilies();
        targets.setKind(RestInternalConstants.KIND_CODELIST_FAMILIES);

        // Pagination
        String baseLink = toCodelistFamiliesLink();
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.CodelistFamily source : sourcesPagedResult.getValues()) {
            ResourceInternal target = toResource(source);
            targets.getCodelistFamilies().add(target);
        }
        return targets;
    }

    @Override
    public CodelistFamily toCodelistFamily(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) throws MetamacException {
        if (source == null) {
            return null;
        }
        CodelistFamily target = new CodelistFamily();
        target.setId(source.getNameableArtefact().getCode());
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setKind(RestInternalConstants.KIND_CODELIST_FAMILY);
        target.setSelfLink(toCodelistFamilySelfLink(source));
        target.setManagementAppLink(toCodelistFamilyManagementApplicationLink(source));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        return target;
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

    private ChildLinks toVariableFamilyChildLinks(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(RestInternalConstants.KIND_VARIABLES, toVariablesByFamilyLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceInternal toResource(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getMaintainableArtefact(), RestInternalConstants.KIND_CODELIST, toCodelistSelfLink(source), toCodelistManagementApplicationLink(source));
    }

    private ResourceInternal toResource(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getNameableArtefact(), RestInternalConstants.KIND_CODE, toCodeSelfLink(source), toCodeManagementApplicationLink(source));
    }

    private ResourceInternal toResource(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getNameableArtefact(), RestInternalConstants.KIND_VARIABLE_FAMILY, toVariableFamilySelfLink(source), toVariableFamilyManagementApplicationLink(source));
    }

    private ResourceInternal toResource(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        if (source == null) {
            return null;
        }
        return toResource(source.getNameableArtefact(), RestInternalConstants.KIND_CODELIST_FAMILY, toCodelistFamilySelfLink(source), toCodelistFamilyManagementApplicationLink(source));
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

    // API/variablefamilies
    private String toVariableFamiliesLink() {
        return RestUtils.createLink(getSrmApiInternalEndpointV10(), RestInternalConstants.LINK_SUBPATH_VARIABLE_FAMILIES);
    }

    // API/variablefamilies/VARIABLE_FAMILY_ID
    private String toVariableFamilyLink(String variableFamilyID) {
        String linkVariableFamilies = toVariableFamiliesLink();
        return RestUtils.createLink(linkVariableFamilies, variableFamilyID);
    }
    private String toVariableFamilyLink(org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamily) {
        return toVariableFamilyLink(variableFamily.getNameableArtefact().getCode());
    }
    private ResourceLink toVariableFamilySelfLink(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        ResourceLink target = new ResourceLink();
        target.setKind(RestInternalConstants.KIND_VARIABLE_FAMILY);
        target.setHref(toVariableFamilyLink(source));
        return target;
    }

    // API/variablefamilies/VARIABLE_FAMILY_ID/variables
    private String toVariablesByFamilyLink(String variableFamilyID) {
        String linkVariableFamily = toVariableFamilyLink(variableFamilyID);
        return RestUtils.createLink(linkVariableFamily, RestInternalConstants.LINK_SUBPATH_VARIABLES_BY_FAMILY);
    }
    private String toVariablesByFamilyLink(org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamily) {
        return toVariablesByFamilyLink(variableFamily.getNameableArtefact().getCode());
    }

    // API/variables
    private String toVariablesLink() {
        return RestUtils.createLink(getSrmApiInternalEndpointV10(), RestInternalConstants.LINK_SUBPATH_VARIABLES);
    }

    // API/variables/VARIABLE_ID
    private String toVariableLink(org.siemac.metamac.srm.core.code.domain.Variable variable) {
        String linkVariables = toVariablesLink();
        return RestUtils.createLink(linkVariables, variable.getNameableArtefact().getCode());
    }
    private ResourceLink toVariableSelfLink(org.siemac.metamac.srm.core.code.domain.Variable source) {
        ResourceLink target = new ResourceLink();
        target.setKind(RestInternalConstants.KIND_VARIABLE);
        target.setHref(toVariableLink(source));
        return target;
    }

    // API/codelistfamilies
    private String toCodelistFamiliesLink() {
        return RestUtils.createLink(getSrmApiInternalEndpointV10(), RestInternalConstants.LINK_SUBPATH_CODELIST_FAMILIES);
    }

    // API/codelistfamilies/CODELIST_FAMILY_ID
    private String toCodelistFamilyLink(org.siemac.metamac.srm.core.code.domain.CodelistFamily codelistFamily) {
        String linkCodelistFamilies = toCodelistFamiliesLink();
        return RestUtils.createLink(linkCodelistFamilies, codelistFamily.getNameableArtefact().getCode());
    }
    private ResourceLink toCodelistFamilySelfLink(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        ResourceLink target = new ResourceLink();
        target.setKind(RestInternalConstants.KIND_CODELIST_FAMILY);
        target.setHref(toCodelistFamilyLink(source));
        return target;
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

    private ResourceInternal toCodelistReplacedBy(CodelistVersionMetamac source) {
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

    private ReplaceToVariable toVariableReplaceTo(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (CollectionUtils.isEmpty(source.getReplaceToVariables())) {
            return null;
        }
        ReplaceToVariable target = new ReplaceToVariable();
        target.setKind(RestInternalConstants.KIND_VARIABLES);
        target.setTotal(BigInteger.valueOf(source.getReplaceToVariables().size()));
        for (org.siemac.metamac.srm.core.code.domain.Variable replaceToSource : source.getReplaceToVariables()) {
            target.getReplaceTos().add(toResource(replaceToSource));
        }
        return target;
    }

    private VariableFamilyMetadata toVariableFamilyMetadata(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (CollectionUtils.isEmpty(source.getFamilies())) {
            return null;
        }
        VariableFamilyMetadata target = new VariableFamilyMetadata();
        target.setKind(RestInternalConstants.KIND_VARIABLE_FAMILIES);
        target.setTotal(BigInteger.valueOf(source.getFamilies().size()));
        for (org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamily : source.getFamilies()) {
            target.getFamilies().add(toResource(variableFamily));
        }
        return target;
    }

    private String toCodelistManagementApplicationLink(CodelistVersionMetamac source) {
        return getInternalWebApplicationNavigation().buildCodelistUrl(source);
    }

    private String toCodeManagementApplicationLink(CodeMetamac source) {
        return getInternalWebApplicationNavigation().buildCodeUrl(source);
    }

    private String toCodelistFamilyManagementApplicationLink(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        return getInternalWebApplicationNavigation().buildCodelistFamilyUrl(source);
    }

    private String toVariableFamilyManagementApplicationLink(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        return getInternalWebApplicationNavigation().buildVariableFamilyUrl(source);
    }

    private String toVariableManagementApplicationLink(org.siemac.metamac.srm.core.code.domain.Variable source) {
        return getInternalWebApplicationNavigation().buildVariableUrl(source);
    }

}