package org.siemac.metamac.srm.rest.internal.v1_0.mapper.code;

import java.math.BigInteger;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.AccessType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodeResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ItemResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ReplaceToCodelist;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ReplaceToVariable;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableFamilyMetadata;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableType;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Variables;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VisualisationConfiguration;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VisualisationConfigurations;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.rest.internal.RestInternalConstants;
import org.siemac.metamac.srm.rest.internal.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.ItemSchemeBaseDo2RestMapperV10Impl;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@Component
public class CodesDo2RestMapperV10Impl extends ItemSchemeBaseDo2RestMapperV10Impl implements CodesDo2RestMapperV10 {

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
        Codelist target = new Codelist();
        target.setKind(RestInternalConstants.KIND_CODELIST);
        target.setSelfLink(toCodelistSelfLink(source));
        target.setParentLink(toCodelistParentLink(source));
        target.setChildLinks(toCodelistChildLinks(source));
        target.setManagementAppLink(toCodelistManagementApplicationLink(source));

        toItemScheme(source, source.getLifeCycleMetadata(), target);

        target.setShortName(toInternationalString(source.getShortName()));
        target.setDescriptionSource(toInternationalString(source.getDescriptionSource()));
        target.setIsRecommended(source.getIsRecommended());
        target.setFamily(toResource(source.getFamily()));
        target.setVariable(toResource(source.getVariable()));
        target.setAccessType(toAccessType(source.getAccessType()));
        target.setReplacedBy(toCodelistReplacedBy(source));
        target.setReplaceTo(toCodelistReplaceTo(source));
        target.setOrderConfigurations(toOrderConfigurations(source.getOrderVisualisations(), source.getDefaultOrderVisualisation()));
        target.setOpennessConfigurations(toOpennessConfigurations(source.getOpennessVisualisations(), source.getDefaultOpennessVisualisation()));

        return target;
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
            CodeResourceInternal target = toCodeResource(source);
            targets.getCodes().add(target);
        }
        return targets;
    }

    @Override
    public Codes toCodes(List<ItemResult> sources, CodelistVersionMetamac codelistVersion) {

        Codes targets = new Codes();
        targets.setKind(RestInternalConstants.KIND_CODES);

        // No pagination
        targets.setSelfLink(toCodesLink(codelistVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (ItemResult source : sources) {
            CodeResourceInternal target = toCodeResource(source, codelistVersion);
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

        target.setKind(RestInternalConstants.KIND_CODE);
        target.setSelfLink(toCodeSelfLink(source));
        target.setParentLink(toCodeParentLink(source));
        target.setChildLinks(toCodeChildLinks(source));
        target.setManagementAppLink(toCodeManagementApplicationLink(source));

        toItem(source, target);

        if (source.getVariableElement() == null) {
            target.setShortName(toInternationalString(source.getShortName()));
        } else {
            target.setShortName(toInternationalString(source.getVariableElement().getShortName()));
        }
        target.setVariableElement(toItem(source.getVariableElement()));

        return target;
    }

    @Override
    public ItemResourceInternal toResource(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        ItemResourceInternal target = new ItemResourceInternal();
        toResource(source, RestInternalConstants.KIND_CODE, toCodeSelfLink(source), toCodeManagementApplicationLink(source), target);
        return target;
    }

    private CodeResourceInternal toCodeResource(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        CodeResourceInternal target = new CodeResourceInternal();
        toResource(source, RestInternalConstants.KIND_CODE, toCodeSelfLink(source), toCodeManagementApplicationLink(source), target);
        // following information is retrieved only one retrieve efficiently all codes in codelist
        target.setOrder(null);
        target.setOpen(null);
        return target;
    }

    private CodeResourceInternal toCodeResource(ItemResult source, CodelistVersionMetamac codelistVersion) {
        if (source == null) {
            return null;
        }
        CodeResourceInternal target = new CodeResourceInternal();
        toResource(source, RestInternalConstants.KIND_CODE, toCodeSelfLink(source, codelistVersion), toCodeManagementApplicationLink(codelistVersion, source), target);
        target.setOrder(SrmServiceUtils.getCodeItemResultOrder(source) + 1); // add 1 to start in 1, instead of 0
        target.setOpen(SrmServiceUtils.getCodeItemResultOpenness(source));
        return target;
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
        target.setUrnProvider(source.getNameableArtefact().getUrnProvider());
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
        target.setUrnProvider(source.getNameableArtefact().getUrnProvider());
        target.setKind(RestInternalConstants.KIND_VARIABLE);
        target.setSelfLink(toVariableSelfLink(source));
        target.setManagementAppLink(toVariableManagementApplicationLink(source));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        target.setShortName(toInternationalString(source.getShortName()));
        target.setType(toVariableType(source.getType()));
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
        ResourceInternal target = new ResourceInternal();
        toResource(source.getNameableArtefact(), RestInternalConstants.KIND_VARIABLE, toVariableSelfLink(source), toVariableManagementApplicationLink(source), target);
        return target;
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
        target.setUrnProvider(source.getNameableArtefact().getUrnProvider());
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
        target.setName(toInternationalString(source.getShortName()));
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
    private ResourceLink toCodeSelfLink(ItemResult source, CodelistVersion codelistVersion) {
        return toResourceLink(RestInternalConstants.KIND_CODE, toCodeLink(source, codelistVersion));
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
        ResourceInternal target = new ResourceInternal();
        toResource(source.getMaintainableArtefact(), RestInternalConstants.KIND_CODELIST, toCodelistSelfLink(source), toCodelistManagementApplicationLink(source), target);
        return target;
    }

    private ResourceInternal toResource(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        toResource(source.getNameableArtefact(), RestInternalConstants.KIND_VARIABLE_FAMILY, toVariableFamilySelfLink(source), toVariableFamilyManagementApplicationLink(source), target);
        return target;
    }

    private ResourceInternal toResource(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        if (source == null) {
            return null;
        }
        ResourceInternal target = new ResourceInternal();
        toResource(source.getNameableArtefact(), RestInternalConstants.KIND_CODELIST_FAMILY, toCodelistFamilySelfLink(source), toCodelistFamilyManagementApplicationLink(source), target);
        return target;
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
    private String toCodeLink(ItemResult item, ItemSchemeVersion itemSchemeVersion) {
        return toItemLink(toSubpathItemSchemes(), toSubpathItems(), item, itemSchemeVersion);
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
            if (canResourceBeProvidedByApi(replaceToCodelist)) {
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
    private String toCodeManagementApplicationLink(CodelistVersion codelistVersion, ItemResult source) {
        return getInternalWebApplicationNavigation().buildCodeUrl(codelistVersion.getMaintainableArtefact().getUrn(), source.getCode());
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

    private VariableType toVariableType(VariableTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case GEOGRAPHICAL:
                return org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.VariableType.GEOGRAPHICAL;
            default:
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
                throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private VisualisationConfigurations toOrderConfigurations(List<CodelistOrderVisualisation> sources, CodelistOrderVisualisation defaultOrderVisualisation) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        VisualisationConfigurations targets = new VisualisationConfigurations();
        for (org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation source : sources) {
            VisualisationConfiguration target = toVisualisationConfiguration(source.getNameableArtefact());
            if (defaultOrderVisualisation.getNameableArtefact().getCode().equals(source.getNameableArtefact().getCode())) {
                target.setDefault(Boolean.TRUE);
            }
            targets.getVisualisationConfigurations().add(target);
        }
        targets.setTotal(BigInteger.valueOf(sources.size()));
        return targets;
    }

    private VisualisationConfigurations toOpennessConfigurations(List<CodelistOpennessVisualisation> sources, CodelistOpennessVisualisation defaultOpennessVisualisation) {
        if (CollectionUtils.isEmpty(sources)) {
            return null;
        }
        VisualisationConfigurations targets = new VisualisationConfigurations();
        for (org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation source : sources) {
            VisualisationConfiguration target = toVisualisationConfiguration(source.getNameableArtefact());
            if (defaultOpennessVisualisation.getNameableArtefact().getCode().equals(source.getNameableArtefact().getCode())) {
                target.setDefault(Boolean.TRUE);
            }
            targets.getVisualisationConfigurations().add(target);
        }
        targets.setTotal(BigInteger.valueOf(sources.size()));
        return targets;
    }

    private VisualisationConfiguration toVisualisationConfiguration(NameableArtefact source) {
        if (source == null) {
            return null;
        }
        VisualisationConfiguration target = new VisualisationConfiguration();
        target.setId(source.getCode());
        target.setName(toInternationalString(source.getName()));
        return target;
    }
}