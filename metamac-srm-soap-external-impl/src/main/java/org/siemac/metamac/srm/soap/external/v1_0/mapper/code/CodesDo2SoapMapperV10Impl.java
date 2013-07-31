package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import java.math.BigInteger;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.common.v1_0.domain.Resource;
import org.siemac.metamac.soap.criteria.mapper.SculptorCriteria2SoapCriteria;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Code;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.ReplaceToCodelist;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.ReplaceToVariable;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilyCodes;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.base.ItemSchemeBaseDo2SoapMapperV10Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@Component
public class CodesDo2SoapMapperV10Impl extends ItemSchemeBaseDo2SoapMapperV10Impl implements CodesDo2SoapMapperV10 {

    private final boolean                                                   AS_STUB = false;

    @Autowired
    private com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbMapper codesDo2JaxbSdmxMapper;

    @Autowired
    @Qualifier("codesDo2JaxbSoapCallbackMetamac")
    private CodesDo2JaxbCallback                                            codesDo2JaxbCallback;

    @Override
    public VariableFamily toVariableFamily(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        if (source == null) {
            return null;
        }
        VariableFamily target = new VariableFamily();
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setUrnProvider(source.getNameableArtefact().getUrnProvider());
        target.setId(getCode(source.getNameableArtefact()));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));

        return target;
    }

    @Override
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> sources, Integer limit) {
        VariableFamilies targets = new VariableFamilies();
        // Values
        if (!CollectionUtils.isEmpty(sources.getValues())) {
            for (org.siemac.metamac.srm.core.code.domain.VariableFamily source : sources.getValues()) {
                targets.getVariableFamilies().add(toVariableFamily(source));
            }
        }
        // Pagination
        SculptorCriteria2SoapCriteria.toPagedResult(sources, targets, limit);
        return targets;
    }

    @Override
    public Variable toVariable(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (source == null) {
            return null;
        }
        Variable target = new Variable();
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setUrnProvider(source.getNameableArtefact().getUrnProvider());
        target.setId(getCode(source.getNameableArtefact()));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        target.setShortName(toInternationalString(source.getShortName()));
        target.setValidFrom(toDate(source.getValidFrom()));
        target.setValidTo(toDate(source.getValidTo()));
        target.setReplacedBy(toResource(source.getReplacedByVariable()));
        target.setReplaceTo(toReplaceTo(source));
        target.setFamilies(toVariableFamilyCodes(source));
        return target;
    }

    @Override
    public Variables toVariables(PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> sources, Integer limit) {
        Variables targets = new Variables();
        // Values
        if (!CollectionUtils.isEmpty(sources.getValues())) {
            for (org.siemac.metamac.srm.core.code.domain.Variable source : sources.getValues()) {
                targets.getVariables().add(toVariable(source));
            }
        }
        // Pagination
        SculptorCriteria2SoapCriteria.toPagedResult(sources, targets, limit);
        return targets;
    }

    @Override
    public CodelistFamily toCodelistFamily(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        if (source == null) {
            return null;
        }
        CodelistFamily target = new CodelistFamily();
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setUrnProvider(source.getNameableArtefact().getUrnProvider());
        target.setId(getCode(source.getNameableArtefact()));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));

        return target;
    }

    @Override
    public CodelistFamilies toCodelistFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> sources, Integer limit) {
        CodelistFamilies targets = new CodelistFamilies();
        // Values
        if (!CollectionUtils.isEmpty(sources.getValues())) {
            for (org.siemac.metamac.srm.core.code.domain.CodelistFamily source : sources.getValues()) {
                targets.getCodelistFamilies().add(toCodelistFamily(source));
            }
        }
        // Pagination
        SculptorCriteria2SoapCriteria.toPagedResult(sources, targets, limit);
        return targets;
    }

    @Override
    public Codelists toCodelists(PagedResult<CodelistVersionMetamac> sources, Integer limit) {
        Codelists targets = new Codelists();
        // Values
        if (!CollectionUtils.isEmpty(sources.getValues())) {
            for (org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac source : sources.getValues()) {
                targets.getCodelists().add(toResource(source));
            }
        }
        // Pagination
        SculptorCriteria2SoapCriteria.toPagedResult(sources, targets, limit);
        return targets;
    }

    @Override
    public Codelist toCodelist(CodelistVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        // following method will call toCodelist(CodelistVersionMetamac source, Codelist target) method, thank to callback
        return (Codelist) codesDo2JaxbSdmxMapper.codelistDoToJaxb(source, codesDo2JaxbCallback, AS_STUB);
    }

    @Override
    public void toCodelist(CodelistVersionMetamac source, Codelist target) {
        if (source == null) {
            return;
        }
        target.setUrn(source.getMaintainableArtefact().getUrn());
        target.setUrnProvider(source.getMaintainableArtefact().getUrnProvider());
        target.setShortName(toInternationalString(source.getShortName()));
        target.setDescriptionSource(toInternationalString(source.getDescriptionSource()));
        target.setIsRecommended(source.getIsRecommended());
        target.setFamily(toResource(source.getFamily()));
        target.setVariable(toResource(source.getVariable()));
        target.setReplaceToVersion(toItemSchemeReplaceToVersion(source));
        target.setReplacedByVersion(toItemSchemeReplacedByVersion(source));
        target.setReplaceTo(toCodelistReplaceTo(source));
        target.setReplacedBy(toCodelistReplacedBy(source));
    }

    @Override
    public void toCode(CodeMetamac source, ItemResult sourceItemResult, Code target) {
        if (sourceItemResult != null) {
            CodeMetamacResultExtensionPoint extensionPoint = (CodeMetamacResultExtensionPoint) sourceItemResult.getExtensionPoint();
            target.setUrn(sourceItemResult.getUrn());
            target.setUrnProvider(sourceItemResult.getUrnProvider());
            target.setShortName(toInternationalString(extensionPoint.getShortName()));
        } else if (source != null) {
            target.setUrn(source.getNameableArtefact().getUrn());
            target.setUrnProvider(source.getNameableArtefact().getUrnProvider());
            if (source.getVariableElement() == null) {
                target.setShortName(toInternationalString(source.getShortName()));
            } else {
                target.setShortName(toInternationalString(source.getVariableElement().getShortName()));
            }
        }
    }

    @Override
    public Code toCode(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toCode(CodeMetamac source, Code target) method, thank to callback
        return (Code) codesDo2JaxbSdmxMapper.codeDoToJaxb(source, null, source.getItemSchemeVersion(), codesDo2JaxbCallback);
    }

    @Override
    protected boolean canItemSchemeVersionBeProvidedByApi(ItemSchemeVersion source) {
        CodelistVersionMetamac codelistVersion = (CodelistVersionMetamac) source;
        return AccessTypeEnum.PUBLIC.equals(codelistVersion.getAccessType());
    }

    private ReplaceToVariable toReplaceTo(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (CollectionUtils.isEmpty(source.getReplaceToVariables())) {
            return null;
        }
        ReplaceToVariable target = new ReplaceToVariable();
        target.setTotal(BigInteger.valueOf(source.getReplaceToVariables().size()));
        for (org.siemac.metamac.srm.core.code.domain.Variable replaceToSource : source.getReplaceToVariables()) {
            target.getReplaceTos().add(toResource(replaceToSource));
        }
        return target;
    }

    private VariableFamilyCodes toVariableFamilyCodes(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (CollectionUtils.isEmpty(source.getFamilies())) {
            return null;
        }
        VariableFamilyCodes target = new VariableFamilyCodes();
        target.setTotal(BigInteger.valueOf(source.getFamilies().size()));
        for (org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamily : source.getFamilies()) {
            target.getFamilies().add(toResource(variableFamily));
        }
        return target;
    }

    private Resource toResource(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setId(getCode(source.getNameableArtefact()));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private Resource toResource(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setId(getCode(source.getNameableArtefact()));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private Resource toResource(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setUrn(source.getNameableArtefact().getUrn());
        target.setId(getCode(source.getNameableArtefact()));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private Resource toResource(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(getCode(source.getMaintainableArtefact()));
        target.setUrn(source.getMaintainableArtefact().getUrn());
        target.setName(toInternationalString(source.getMaintainableArtefact().getName()));
        return target;
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
            target.setTotal(BigInteger.valueOf(target.getReplaceTos().size()));
        }
        return target;
    }
}