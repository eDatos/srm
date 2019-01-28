package org.siemac.metamac.srm.rest.external.v1_0.mapper.code;

import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_FEATURES;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_FEATURE_COLLECTION_VALUE;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_FEATURE_VALUE;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_GEOMETRY;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_ID;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_LAST_UPDATED_DATE;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_LATITUDE;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_LONGITUDE;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_PROPERTIES;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_TYPE;
import static org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants.VARIABLE_ELEMENT_URN;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ChildLinks;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.constants.RestConstants;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.mapper.SculptorCriteria2RestCriteria;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AccessType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodeResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ItemResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ReplaceToResources;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElement;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElements;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementsGeoInfo;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementsGeoInfoFeature;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementsGeoInfoFeatureProperties;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementsGeoInfoFeatures;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamiliesMetadata;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableType;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VisualisationConfiguration;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VisualisationConfigurations;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.base.ItemSchemeBaseDo2RestMapperV10Impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.code.domain.CodelistVersion;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@Component
public class CodesDo2RestMapperV10Impl extends ItemSchemeBaseDo2RestMapperV10Impl implements CodesDo2RestMapperV10 {

    private final Logger logger = LoggerFactory.getLogger(CodesDo2RestMapperV10Impl.class);

    @Override
    public Codelists toCodelists(PagedResult<CodelistVersionMetamac> sourcesPagedResult, String agencyID, String resourceID, String query, String orderBy, Integer limit) {

        Codelists targets = new Codelists();
        targets.setKind(SrmRestConstants.KIND_CODELISTS);

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
        Codelist target = new Codelist();
        target.setKind(SrmRestConstants.KIND_CODELIST);
        target.setSelfLink(toCodelistSelfLink(source));
        target.setParentLink(toCodelistParentLink(source));
        target.setChildLinks(toCodelistChildLinks(source));

        toItemScheme(source, source.getLifeCycleMetadata(), target);

        target.setShortName(toInternationalString(source.getShortName()));
        target.setDescriptionSource(toInternationalString(source.getDescriptionSource()));
        target.setIsRecommended(source.getIsRecommended());
        target.setVariable(toResource(source.getVariable()));
        target.setReplaceTo(toCodelistReplaceTo(source));
        target.setOrderConfigurations(toOrderConfigurations(source.getOrderVisualisations(), source.getDefaultOrderVisualisation()));
        target.setOpennessConfigurations(toOpennessConfigurations(source.getOpennessVisualisations(), source.getDefaultOpennessVisualisation()));

        return target;
    }

    @Override
    public Resource toResource(CodelistVersion source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getMaintainableArtefact(), SrmRestConstants.KIND_CODELIST, toCodelistSelfLink(source), target);
        return target;
    }

    @Override
    public Codes toCodes(PagedResult<CodeMetamac> sourcesPagedResult, String agencyID, String resourceID, String version, String query, String orderBy, Integer limit, Set<String> fields) {

        Codes targets = new Codes();
        targets.setKind(SrmRestConstants.KIND_CODES);

        // Pagination
        String baseLink = toCodesLink(agencyID, resourceID, version);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (CodeMetamac source : sourcesPagedResult.getValues()) {
            CodeResource target = toCodeResource(source, fields);
            targets.getCodes().add(target);
        }
        return targets;
    }

    @Override
    public Codes toCodes(List<ItemResult> sources, CodelistVersionMetamac codelistVersion, Set<String> fields) {

        Codes targets = new Codes();
        targets.setKind(SrmRestConstants.KIND_CODES);

        // No pagination
        targets.setSelfLink(toCodesLink(codelistVersion));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        String variableID = codelistVersion.getVariable() != null ? codelistVersion.getVariable().getNameableArtefact().getCode() : null;
        for (ItemResult source : sources) {
            CodeResource target = toCodeResource(source, codelistVersion, variableID, fields);
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

        target.setKind(SrmRestConstants.KIND_CODE);
        target.setSelfLink(toCodeSelfLink(source));
        target.setParentLink(toCodeParentLink(source));
        target.setChildLinks(toCodeChildLinks(source));

        toItem(source, target);

        if (source.getVariableElement() == null) {
            target.setShortName(toInternationalString(source.getShortName()));
        } else {
            target.setShortName(toInternationalString(source.getVariableElement().getShortName()));
        }

        return target;
    }

    @Override
    public ItemResource toResource(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        ItemResource target = new ItemResource();
        toResource(source, SrmRestConstants.KIND_CODE, toCodeSelfLink(source), target, null);
        return target;
    }

    private CodeResource toCodeResource(CodeMetamac source, Set<String> fields) {
        if (source == null) {
            return null;
        }
        CodeResource target = new CodeResource();
        toResource(source, SrmRestConstants.KIND_CODE, toCodeSelfLink(source), target, fields);
        
        setVisibleFields(source, target, fields);
        
        return target;
    }
    
    
    private void setVisibleFields(CodeMetamac source, CodeResource target, Set<String> fields) {
        if (containsField(fields, SrmRestConstants.FIELD_INCLUDE_VARIABLE_ELEMENT)) {
            target.setVariableElement(toResource(source.getVariableElement()));
        }
        
        target.setOpen(null);
        target.setOrder(null);
    }
    

    private CodeResource toCodeResource(ItemResult source, CodelistVersionMetamac codelistVersion, String variableID, Set<String> fields) {
        if (source == null) {
            return null;
        }
        CodeResource target = new CodeResource();
        toResource(source, SrmRestConstants.KIND_CODE, toCodeSelfLink(source, codelistVersion), target, codelistVersion.getMaintainableArtefact().getIsImported(), fields);

        setVisibleFields(source, target, variableID, fields);

        return target;
    }

    private void setVisibleFields(ItemResult source, CodeResource target, String variableID, Set<String> fields) {
        if (containsField(fields, SrmRestConstants.FIELD_INCLUDE_VARIABLE_ELEMENT)) {
            target.setVariableElement(toResource(variableID, ((CodeMetamacResultExtensionPoint) source.getExtensionPoint()).getVariableElement()));
        }

        if (containsField(fields, SrmRestConstants.FIELD_INCLUDE_ORDER)) {
            target.setOrder(SrmServiceUtils.getCodeItemResultOrder(source) + 1); // add 1 to start in 1, instead of 0
        }

        if (containsField(fields, SrmRestConstants.FIELD_INCLUDE_OPENNES)) {
            target.setOpen(SrmServiceUtils.getCodeItemResultOpenness(source));
        }
    }

    @Override
    protected boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source) {
        CodelistVersionMetamac codelistVersion = (CodelistVersionMetamac) source;
        return ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(codelistVersion.getLifeCycleMetadata().getProcStatus()) && AccessTypeEnum.PUBLIC.equals(codelistVersion.getAccessType());
    }

    @Override
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> sourcesPagedResult, String query, String orderBy, Integer limit) {

        VariableFamilies targets = new VariableFamilies();
        targets.setKind(SrmRestConstants.KIND_VARIABLE_FAMILIES);

        // Pagination
        String baseLink = toVariableFamiliesLink();
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.VariableFamily source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
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
        toNameableArtefact(source.getNameableArtefact(), target, false);

        target.setKind(SrmRestConstants.KIND_VARIABLE_FAMILY);
        target.setSelfLink(toVariableFamilySelfLink(source));
        target.setParentLink(toVariableFamilyParentLink(source));
        target.setChildLinks(toVariableFamilyChildLinks(source));

        return target;
    }

    @Override
    public Variables toVariablesByFamily(String familyID, PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> sourcesPagedResult, String query, String orderBy, Integer limit) {

        Variables targets = new Variables();
        targets.setKind(SrmRestConstants.KIND_VARIABLES);

        // Pagination
        String baseLink = toVariablesByFamilyLink(familyID);
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.Variable source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
            targets.getVariables().add(target);
        }
        return targets;
    }

    @Override
    public Variables toVariables(PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> sourcesPagedResult, String query, String orderBy, Integer limit) {

        Variables targets = new Variables();
        targets.setKind(SrmRestConstants.KIND_VARIABLES);

        // Pagination
        String baseLink = toVariablesLink();
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.Variable source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
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
        toNameableArtefact(source.getNameableArtefact(), target, false);

        target.setKind(SrmRestConstants.KIND_VARIABLE);
        target.setSelfLink(toVariableSelfLink(source));
        target.setParentLink(toVariableParentLink(source));
        target.setChildLinks(toVariableChildLinks(source));

        target.setShortName(toInternationalString(source.getShortName()));
        target.setType(toVariableType(source.getType()));
        target.setReplacedBy(toResource(source.getReplacedByVariable()));
        target.setReplaceTo(toVariableReplaceTo(source));
        target.setFamilies(toVariableFamiliesMetadata(source));
        return target;
    }

    @Override
    public VariableElements toVariableElements(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> sources, String variableID, String query, String orderBy, Integer limit) {

        VariableElements targets = new VariableElements();
        targets.setKind(SrmRestConstants.KIND_VARIABLE_ELEMENTS);

        // Pagination
        String baseLink = toVariableElementsLink(variableID, null);
        SculptorCriteria2RestCriteria.toPagedResult(sources, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.VariableElement source : sources.getValues()) {
            VariableElementResource target = toResource(source);
            targets.getVariableElements().add(target);
        }
        return targets;
    }

    @Override
    public VariableElements toVariableElements(List<VariableElementResult> sources, String variableID, String query) {
        VariableElements targets = new VariableElements();
        targets.setKind(SrmRestConstants.KIND_VARIABLE_ELEMENTS);

        // No pagination
        targets.setSelfLink(toVariableElementsLink(variableID, query));
        targets.setTotal(BigInteger.valueOf(sources.size()));

        // Values
        for (org.siemac.metamac.srm.core.code.domain.VariableElementResult source : sources) {
            VariableElementResource target = toResource(variableID, source);
            targets.getVariableElements().add(target);
        }
        return targets;
    }

    @Override
    public String toVariableElementsGeoJson(List<VariableElementResult> sources, VariableElementResultSelection selection, DateTime globalLastUpdatedDate) {
        return toVariableElementsGeoJsonCommon(sources, selection, globalLastUpdatedDate);
    }

    @Override
    public String toVariableElementsGeoJson(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> sources, VariableElementResultSelection selection, DateTime globalLastUpdatedDate) {
        return toVariableElementsGeoJsonCommon(sources.getValues(), selection, globalLastUpdatedDate);
    }

    @Override
    public VariableElementsGeoInfo toVariableElementsGeoXml(List<VariableElementResult> sources, VariableElementResultSelection selection, DateTime globalLastUpdatedDate) {
        return toVariableElementsGeoXmlCommon(sources, selection, globalLastUpdatedDate);
    }

    @Override
    public VariableElementsGeoInfo toVariableElementsGeoXml(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> sources, VariableElementResultSelection selection,
            DateTime globalLastUpdatedDate) {
        return toVariableElementsGeoXmlCommon(sources.getValues(), selection, globalLastUpdatedDate);
    }

    @Override
    public VariableElement toVariableElement(org.siemac.metamac.srm.core.code.domain.VariableElement source) {
        if (source == null) {
            return null;
        }
        VariableElement target = new VariableElement();
        toIdentifiableArtefact(source.getIdentifiableArtefact(), target, false);

        target.setKind(SrmRestConstants.KIND_VARIABLE_ELEMENT);
        target.setSelfLink(toVariableElementSelfLink(source));
        target.setParentLink(toVariableElementParentLink(source));
        target.setChildLinks(toVariableElementChildLinks(source));

        target.setName(toInternationalString(source.getShortName()));
        target.setReplacedBy(toResource(source.getReplacedByVariableElement()));
        target.setReplaceTo(toVariableElementReplaceTo(source));
        target.setVariable(toResource(source.getVariable()));
        return target;
    }

    @Override
    public Resource toResource(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getNameableArtefact(), SrmRestConstants.KIND_VARIABLE, toVariableSelfLink(source), target, false, null);
        return target;
    }

    @Override
    public CodelistFamilies toCodelistFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> sourcesPagedResult, String query, String orderBy, Integer limit) {

        CodelistFamilies targets = new CodelistFamilies();
        targets.setKind(SrmRestConstants.KIND_CODELIST_FAMILIES);

        // Pagination
        String baseLink = toCodelistFamiliesLink();
        SculptorCriteria2RestCriteria.toPagedResult(sourcesPagedResult, targets, query, orderBy, limit, baseLink);

        // Values
        for (org.siemac.metamac.srm.core.code.domain.CodelistFamily source : sourcesPagedResult.getValues()) {
            Resource target = toResource(source);
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
        toNameableArtefact(source.getNameableArtefact(), target, false);

        target.setKind(SrmRestConstants.KIND_CODELIST_FAMILY);
        target.setSelfLink(toCodelistFamilySelfLink(source));
        target.setParentLink(toCodelistFamilyParentLink(source));
        target.setChildLinks(toCodelistFamilyChildLinks(source));

        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private VariableElementResource toResource(org.siemac.metamac.srm.core.code.domain.VariableElement source) {
        if (source == null) {
            return null;
        }
        VariableElementResource target = new VariableElementResource();
        toResource(source.getIdentifiableArtefact(), SrmRestConstants.KIND_VARIABLE_ELEMENT, toVariableElementSelfLink(source), target, false);
        target.setName(toInternationalString(source.getShortName()));

        if (source.getGeographicalGranularity() != null) {
            target.setGeographicalGranularity(toResource(source.getGeographicalGranularity()));
        }
        return target;
    }

    private VariableElementResource toResource(String variableID, org.siemac.metamac.srm.core.code.domain.VariableElementResult source) {
        if (source == null) {
            return null;
        }
        VariableElementResource target = new VariableElementResource();
        toResource(source, SrmRestConstants.KIND_VARIABLE_ELEMENT, toVariableElementSelfLink(variableID, source), target, false);
        target.setName(toInternationalString(source.getShortName()));

        if (source.getGeographicalGranularity() != null) {
            ItemResource resource = toResource(source.getGeographicalGranularity());
            target.setGeographicalGranularity(resource);
        }
        return target;
    }

    private ResourceLink toCodelistSelfLink(CodelistVersion source) {
        return toResourceLink(SrmRestConstants.KIND_CODELIST, toCodelistLink(source));
    }

    private ResourceLink toCodelistParentLink(CodelistVersionMetamac source) {
        return toResourceLink(SrmRestConstants.KIND_CODELISTS, toCodelistsLink(null, null, null));
    }

    private ChildLinks toCodelistChildLinks(CodelistVersionMetamac source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(SrmRestConstants.KIND_CODES, toCodesLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toCodeSelfLink(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        return toResourceLink(SrmRestConstants.KIND_CODE, toCodeLink(source));
    }
    private ResourceLink toCodeSelfLink(ItemResult source, CodelistVersion codelistVersion) {
        return toResourceLink(SrmRestConstants.KIND_CODE, toCodeLink(source, codelistVersion));
    }
    private ResourceLink toCodeSelfLink(ItemResult source) {
        String[] codeUrnSplited = UrnUtils.splitUrnItem(source.getUrn());
        String codesLink = toItemsLink(toSubpathItemSchemes(), toSubpathItems(), codeUrnSplited[0], codeUrnSplited[1], codeUrnSplited[2]);
        String codeLink = RestUtils.createLink(codesLink, getCode(source));
        return toResourceLink(SrmRestConstants.KIND_CODE, codeLink);
    }

    private ResourceLink toCodeParentLink(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        return toResourceLink(SrmRestConstants.KIND_CODES, toCodesLink(source.getItemSchemeVersion()));
    }

    private ChildLinks toCodeChildLinks(com.arte.statistic.sdmx.srm.core.code.domain.Code source) {
        // nothing
        return null;
    }

    private ResourceLink toVariableFamilyParentLink(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        return null;
    }

    private ChildLinks toVariableFamilyChildLinks(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(SrmRestConstants.KIND_VARIABLES, toVariablesByFamilyLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toVariableParentLink(org.siemac.metamac.srm.core.code.domain.Variable source) {
        return null;
    }

    private ChildLinks toVariableChildLinks(org.siemac.metamac.srm.core.code.domain.Variable source) {
        ChildLinks targets = new ChildLinks();
        targets.getChildLinks().add(toResourceLink(SrmRestConstants.KIND_VARIABLE_ELEMENTS, toVariableElementsLink(source)));
        targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        return targets;
    }

    private ResourceLink toVariableElementParentLink(org.siemac.metamac.srm.core.code.domain.VariableElement source) {
        return toResourceLink(SrmRestConstants.KIND_VARIABLE_ELEMENTS, toVariableElementsLink(source.getVariable()));
    }

    private ChildLinks toVariableElementChildLinks(org.siemac.metamac.srm.core.code.domain.VariableElement source) {
        ChildLinks targets = null;
        if (VariableTypeEnum.GEOGRAPHICAL.equals(source.getVariable().getType())) {
            targets = new ChildLinks();
            targets.getChildLinks().add(toResourceLink(null, toVariableElementGeoLink(source)));
            targets.setTotal(BigInteger.valueOf(targets.getChildLinks().size()));
        }
        return targets;
    }

    private ResourceLink toCodelistFamilyParentLink(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        return null;
    }

    private ChildLinks toCodelistFamilyChildLinks(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        return null;
    }

    private Resource toResource(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getNameableArtefact(), SrmRestConstants.KIND_VARIABLE_FAMILY, toVariableFamilySelfLink(source), target, false, null);
        return target;
    }

    private Resource toResource(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        toResource(source.getNameableArtefact(), SrmRestConstants.KIND_CODELIST_FAMILY, toCodelistFamilySelfLink(source), target, false, null);
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
        return SrmRestConstants.LINK_SUBPATH_CODELISTS;
    }
    private String toSubpathItems() {
        return SrmRestConstants.LINK_SUBPATH_CODES;
    }

    // API/variablefamilies
    private String toVariableFamiliesLink() {
        return RestUtils.createLink(getSrmApiExternalEndpointV10(), SrmRestConstants.LINK_SUBPATH_VARIABLE_FAMILIES);
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
        target.setKind(SrmRestConstants.KIND_VARIABLE_FAMILY);
        target.setHref(toVariableFamilyLink(source));
        return target;
    }

    // API/variablefamilies/VARIABLE_FAMILY_ID/variables
    private String toVariablesByFamilyLink(String variableFamilyID) {
        String linkVariableFamily = toVariableFamilyLink(variableFamilyID);
        return RestUtils.createLink(linkVariableFamily, SrmRestConstants.LINK_SUBPATH_VARIABLES_BY_FAMILY);
    }
    private String toVariablesByFamilyLink(org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamily) {
        return toVariablesByFamilyLink(variableFamily.getNameableArtefact().getCode());
    }

    // API/variables
    private String toVariablesLink() {
        return RestUtils.createLink(getSrmApiExternalEndpointV10(), SrmRestConstants.LINK_SUBPATH_VARIABLES);
    }

    // API/variables/VARIABLE_ID
    private String toVariableLink(String variableID) {
        String linkVariables = toVariablesLink();
        return RestUtils.createLink(linkVariables, variableID);
    }
    private String toVariableLink(org.siemac.metamac.srm.core.code.domain.Variable variable) {
        return toVariableLink(variable.getNameableArtefact().getCode());
    }
    private ResourceLink toVariableSelfLink(org.siemac.metamac.srm.core.code.domain.Variable source) {
        ResourceLink target = new ResourceLink();
        target.setKind(SrmRestConstants.KIND_VARIABLE);
        target.setHref(toVariableLink(source));
        return target;
    }

    // API/variables/VARIABLE_ID/variableelements
    private String toVariableElementsLink(String variableID, String query) {
        String linkVariable = toVariableLink(variableID);
        String link = RestUtils.createLink(linkVariable, SrmRestConstants.LINK_SUBPATH_VARIABLE_ELEMENTS);
        if (query != null) {
            link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_QUERY, query);
        }
        return link;
    }

    private String toVariableElementsLink(org.siemac.metamac.srm.core.code.domain.Variable variable) {
        return toVariableElementsLink(variable.getNameableArtefact().getCode(), null);
    }

    // API/variables/VARIABLE_ID/variableelements/VARIABLE_ELEMENT_ID
    private String toVariableElementLink(String variableID, String variableElementID) {
        String linkVariableElements = toVariableElementsLink(variableID, null);
        return RestUtils.createLink(linkVariableElements, variableElementID);
    }
    private String toVariableElementLink(org.siemac.metamac.srm.core.code.domain.VariableElement variableElement) {
        return toVariableElementLink(variableElement.getVariable().getNameableArtefact().getCode(), variableElement.getIdentifiableArtefact().getCode());
    }
    private ResourceLink toVariableElementSelfLink(org.siemac.metamac.srm.core.code.domain.VariableElement source) {
        ResourceLink target = new ResourceLink();
        target.setKind(SrmRestConstants.KIND_VARIABLE_ELEMENT);
        target.setHref(toVariableElementLink(source));
        return target;
    }
    private ResourceLink toVariableElementSelfLink(String variableID, org.siemac.metamac.srm.core.code.domain.VariableElementResult source) {
        ResourceLink target = new ResourceLink();
        target.setKind(SrmRestConstants.KIND_VARIABLE_ELEMENT);
        target.setHref(toVariableElementLink(variableID, source.getCode()));
        return target;
    }
    private String toVariableElementGeoLink(org.siemac.metamac.srm.core.code.domain.VariableElement variableElement) {
        String linkVariableElement = toVariableElementLink(variableElement);
        return RestUtils.createLink(linkVariableElement, SrmRestConstants.LINK_SUBPATH_VARIABLE_ELEMENT_GEOGRAPHICAL_INFO);
    }

    // API/codelistfamilies
    private String toCodelistFamiliesLink() {
        return RestUtils.createLink(getSrmApiExternalEndpointV10(), SrmRestConstants.LINK_SUBPATH_CODELIST_FAMILIES);
    }

    // API/codelistfamilies/CODELIST_FAMILY_ID
    private String toCodelistFamilyLink(org.siemac.metamac.srm.core.code.domain.CodelistFamily codelistFamily) {
        String linkCodelistFamilies = toCodelistFamiliesLink();
        return RestUtils.createLink(linkCodelistFamilies, codelistFamily.getNameableArtefact().getCode());
    }
    private ResourceLink toCodelistFamilySelfLink(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        ResourceLink target = new ResourceLink();
        target.setKind(SrmRestConstants.KIND_CODELIST_FAMILY);
        target.setHref(toCodelistFamilyLink(source));
        return target;
    }

    private AccessType toAccessType(AccessTypeEnum source) {
        switch (source) {
            case PUBLIC:
                return org.siemac.metamac.rest.structural_resources.v1_0.domain.AccessType.PUBLIC;
            case RESTRICTED:
                return org.siemac.metamac.rest.structural_resources.v1_0.domain.AccessType.RESTRICTED;
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

    private ReplaceToResources toCodelistReplaceTo(CodelistVersionMetamac source) {
        ReplaceToResources target = null;
        for (CodelistVersionMetamac replaceToCodelist : source.getReplaceToCodelists()) {
            if (canResourceBeProvidedByApi(replaceToCodelist)) {
                if (target == null) {
                    target = new ReplaceToResources();

                }
                target.getReplaceTos().add(toResource(replaceToCodelist));
            }
        }
        if (target != null) {
            target.setKind(SrmRestConstants.KIND_CODELISTS);
            target.setTotal(BigInteger.valueOf(target.getReplaceTos().size()));
        }
        return target;
    }

    private ReplaceToResources toVariableReplaceTo(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (CollectionUtils.isEmpty(source.getReplaceToVariables())) {
            return null;
        }
        ReplaceToResources target = new ReplaceToResources();
        target.setKind(SrmRestConstants.KIND_VARIABLES);
        target.setTotal(BigInteger.valueOf(source.getReplaceToVariables().size()));
        for (org.siemac.metamac.srm.core.code.domain.Variable replaceToSource : source.getReplaceToVariables()) {
            target.getReplaceTos().add(toResource(replaceToSource));
        }
        return target;
    }

    private ReplaceToResources toVariableElementReplaceTo(org.siemac.metamac.srm.core.code.domain.VariableElement source) {
        if (CollectionUtils.isEmpty(source.getReplaceToVariableElements())) {
            return null;
        }
        ReplaceToResources target = new ReplaceToResources();
        target.setKind(SrmRestConstants.KIND_VARIABLE_ELEMENTS);
        target.setTotal(BigInteger.valueOf(source.getReplaceToVariableElements().size()));
        for (org.siemac.metamac.srm.core.code.domain.VariableElement replaceToSource : source.getReplaceToVariableElements()) {
            target.getReplaceTos().add(toResource(replaceToSource));
        }
        return target;
    }

    private VariableFamiliesMetadata toVariableFamiliesMetadata(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (CollectionUtils.isEmpty(source.getFamilies())) {
            return null;
        }
        VariableFamiliesMetadata target = new VariableFamiliesMetadata();
        target.setKind(SrmRestConstants.KIND_VARIABLE_FAMILIES);
        target.setTotal(BigInteger.valueOf(source.getFamilies().size()));
        for (org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamily : source.getFamilies()) {
            target.getFamilies().add(toResource(variableFamily));
        }
        return target;
    }

    private VariableType toVariableType(VariableTypeEnum source) {
        if (source == null) {
            return null;
        }
        switch (source) {
            case GEOGRAPHICAL:
                return org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableType.GEOGRAPHICAL;
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
            if (target != null) {
                if (defaultOpennessVisualisation.getNameableArtefact().getCode().equals(source.getNameableArtefact().getCode())) {
                    target.setDefault(Boolean.TRUE);
                }
                targets.getVisualisationConfigurations().add(target);
            }
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

    @SuppressWarnings("rawtypes")
    private String toVariableElementsGeoJsonCommon(List sources, VariableElementResultSelection selection, DateTime globalLastUpdatedDate) {
        StringBuilder target = new StringBuilder();
        target.append("{");
        target.append("\"" + VARIABLE_ELEMENT_TYPE + "\":\"" + VARIABLE_ELEMENT_FEATURE_COLLECTION_VALUE + "\"");
        if (!CollectionUtils.isEmpty(sources)) {
            target.append(",");
            target.append("\"" + VARIABLE_ELEMENT_FEATURES + "\":[");
            for (int i = 0; i < sources.size(); i++) {
                Object source = sources.get(i);
                VariableElementMetadataExtraction variableElementMetadataExtraction = buildVariableElementMetadataExtraction(source);
                toVariableElementGeoJson(variableElementMetadataExtraction, selection, globalLastUpdatedDate, target);
                if (i != sources.size() - 1) {
                    target.append(",");
                }
            }
            target.append("]");
        }
        target.append("}");
        return target.toString();
    }

    @SuppressWarnings("rawtypes")
    private VariableElementsGeoInfo toVariableElementsGeoXmlCommon(List sources, VariableElementResultSelection selection, DateTime globalLastUpdatedDate) {
        VariableElementsGeoInfo target = new VariableElementsGeoInfo();
        target.setType(VARIABLE_ELEMENT_FEATURE_COLLECTION_VALUE);
        if (!CollectionUtils.isEmpty(sources)) {
            VariableElementsGeoInfoFeatures features = new VariableElementsGeoInfoFeatures();
            target.setFeatures(features);
            for (int i = 0; i < sources.size(); i++) {
                Object source = sources.get(i);
                VariableElementMetadataExtraction variableElementMetadataExtraction = buildVariableElementMetadataExtraction(source);
                VariableElementsGeoInfoFeature feature = toVariableElementGeoXml(variableElementMetadataExtraction, selection, globalLastUpdatedDate);
                target.getFeatures().getFeatures().add(feature);
            }
            target.getFeatures().setTotal(BigInteger.valueOf(target.getFeatures().getFeatures().size()));
        }
        return target;
    }

    private void toVariableElementGeoJson(VariableElementMetadataExtraction variableElementMetadataExtraction, VariableElementResultSelection selection, DateTime globalLastUpdatedDate,
            StringBuilder target) {
        target.append("{");
        target.append("\"" + VARIABLE_ELEMENT_TYPE + "\":\"" + VARIABLE_ELEMENT_FEATURE_VALUE + "\"");
        target.append(",");
        target.append("\"" + VARIABLE_ELEMENT_ID + "\":\"" + variableElementMetadataExtraction.getCode() + "\"");
        if (selection.isShapeGeojson()) {
            Object shapeGeojson = variableElementMetadataExtraction.getShapeGeoJson();
            if (shapeGeojson != null) {
                target.append(",");
                target.append("\"" + VARIABLE_ELEMENT_GEOMETRY + "\":" + shapeGeojson);
            }
        }
        target.append(",");
        target.append("\"" + VARIABLE_ELEMENT_PROPERTIES + "\": ");
        target.append("{");
        target.append("\"" + VARIABLE_ELEMENT_URN + "\": \"" + variableElementMetadataExtraction.getUrn() + "\"");
        if (globalLastUpdatedDate != null) {
            target.append(",");
            target.append("\"" + VARIABLE_ELEMENT_LAST_UPDATED_DATE + "\": \"" + globalLastUpdatedDate.toString() + "\"");
        }
        if (selection.isLongitudeLatitude()) {
            Double longitude = variableElementMetadataExtraction.getLongitude();
            if (longitude != null) {
                target.append(",");
                target.append("\"" + VARIABLE_ELEMENT_LONGITUDE + "\": " + longitude);
            }
            Double latitude = variableElementMetadataExtraction.getLatitude();
            if (latitude != null) {
                target.append(",");
                target.append("\"" + VARIABLE_ELEMENT_LATITUDE + "\": " + latitude);
            }
        }
        if (selection.isGeographicalGranularity()) {
            String geographicalGranularityUrn = variableElementMetadataExtraction.getGeographicalGranularityUrn();
            if (geographicalGranularityUrn != null) {
                target.append(",");
                target.append("\"" + VARIABLE_ELEMENT_GEOGRAPHICAL_GRANULARITY + "\": \"" + geographicalGranularityUrn + "\"");
            }
        }
        target.append("}");
        target.append("}");
    }
    private VariableElementMetadataExtraction buildVariableElementMetadataExtraction(Object source) {
        if (source instanceof VariableElementResult) {
            return new VariableElementMetadataExtractionResult((VariableElementResult) source);
        } else if (source instanceof org.siemac.metamac.srm.core.code.domain.VariableElement) {
            return new VariableElementMetadataExtractionEntity((org.siemac.metamac.srm.core.code.domain.VariableElement) source);
        } else {
            logger.error("VariableElement object unsupported: " + source.getClass().getCanonicalName());
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.UNKNOWN);
            throw new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    private VariableElementsGeoInfoFeature toVariableElementGeoXml(VariableElementMetadataExtraction variableElementMetadataExtraction, VariableElementResultSelection selection,
            DateTime globalLastUpdatedDate) {
        VariableElementsGeoInfoFeature target = new VariableElementsGeoInfoFeature();
        target.setType(VARIABLE_ELEMENT_FEATURE_VALUE);
        target.setId(variableElementMetadataExtraction.getCode());
        if (selection.isShapeWkt()) {
            target.setGeometryWKT(variableElementMetadataExtraction.getShapeWkt());
        }
        target.setProperties(new VariableElementsGeoInfoFeatureProperties());
        target.getProperties().setUrn(variableElementMetadataExtraction.getUrn());
        target.getProperties().setLastUpdatedDate(toDate(globalLastUpdatedDate));
        if (selection.isLongitudeLatitude()) {
            target.getProperties().setLongitude(variableElementMetadataExtraction.getLongitude());
            target.getProperties().setLatitude(variableElementMetadataExtraction.getLatitude());
        }
        if (selection.isGeographicalGranularity()) {
            target.getProperties().setGeographicalGranularity(variableElementMetadataExtraction.getGeographicalGranularityAsResource());
        }
        return target;
    }

    private ItemResource toResource(ItemResult source) {
        if (source == null) {
            return null;
        }
        ItemResource target = new ItemResource();
        toResource(source, SrmRestConstants.KIND_CODE, toCodeSelfLink(source), target, false, null);
        return target;
    }

    private interface VariableElementMetadataExtraction {

        public String getCode();
        public String getUrn();
        public String getShapeWkt();
        public String getShapeGeoJson();
        public Double getLongitude();
        public Double getLatitude();
        public Object getGeographicalGranularity();
        public String getGeographicalGranularityUrn();
        public ItemResource getGeographicalGranularityAsResource();
    }

    private class VariableElementMetadataExtractionEntity implements VariableElementMetadataExtraction {

        private final org.siemac.metamac.srm.core.code.domain.VariableElement variableElement;

        public VariableElementMetadataExtractionEntity(org.siemac.metamac.srm.core.code.domain.VariableElement variableElement) {
            this.variableElement = variableElement;
        }

        @Override
        public String getCode() {
            return variableElement.getIdentifiableArtefact().getCode();
        }

        @Override
        public String getUrn() {
            return variableElement.getIdentifiableArtefact().getUrn();
        }

        @Override
        public String getShapeWkt() {
            return variableElement.getShapeWkt();
        }

        @Override
        public String getShapeGeoJson() {
            return variableElement.getShapeGeojson();
        }

        @Override
        public Double getLongitude() {
            return variableElement.getLongitude();
        }

        @Override
        public Double getLatitude() {
            return variableElement.getLatitude();
        }

        @Override
        public Object getGeographicalGranularity() {
            return variableElement.getGeographicalGranularity();
        }

        @Override
        public String getGeographicalGranularityUrn() {
            return variableElement.getGeographicalGranularity() != null ? variableElement.getGeographicalGranularity().getNameableArtefact().getUrn() : null;
        }

        @Override
        public ItemResource getGeographicalGranularityAsResource() {
            return toResource(variableElement.getGeographicalGranularity());
        }
    }

    private class VariableElementMetadataExtractionResult implements VariableElementMetadataExtraction {

        private final org.siemac.metamac.srm.core.code.domain.VariableElementResult variableElement;

        public VariableElementMetadataExtractionResult(VariableElementResult variableElement) {
            this.variableElement = variableElement;
        }

        @Override
        public String getCode() {
            return variableElement.getCode();
        }

        @Override
        public String getUrn() {
            return variableElement.getUrn();
        }

        @Override
        public String getShapeWkt() {
            return variableElement.getShapeWkt();
        }

        @Override
        public String getShapeGeoJson() {
            return variableElement.getShapeGeojson();
        }

        @Override
        public Double getLongitude() {
            return variableElement.getLongitude();
        }

        @Override
        public Double getLatitude() {
            return variableElement.getLatitude();
        }

        @Override
        public Object getGeographicalGranularity() {
            return variableElement.getGeographicalGranularity();
        }

        @Override
        public String getGeographicalGranularityUrn() {
            return variableElement.getGeographicalGranularity() != null ? variableElement.getGeographicalGranularity().getUrn() : null;
        }

        @Override
        public ItemResource getGeographicalGranularityAsResource() {
            return toResource(variableElement.getGeographicalGranularity());
        }
    }

}