package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import java.math.BigInteger;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.common.v1_0.domain.Item;
import org.siemac.metamac.soap.common.v1_0.domain.Resource;
import org.siemac.metamac.soap.criteria.mapper.SculptorCriteria2SoapCriteria;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Code;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.ReplaceTo;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilyCodes;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.base.BaseDo2SoapMapperV10Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2JaxbCallback;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

@Component
public class CodesDo2SoapMapperV10Impl extends BaseDo2SoapMapperV10Impl implements CodesDo2SoapMapperV10 {

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
        target.setId(getCode(source.getNameableArtefact()));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        target.setShortName(toInternationalString(source.getShortName()));
        target.setValidFrom(toDate(source.getValidFrom()));
        target.setValidTo(toDate(source.getValidTo()));
        target.setReplacedBy(toItem(source));
        target.setReplaceTo(toReplaceTo(source));
        target.setFamily(toVariableFamilyCodes(source));
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
        return (Codelist) codesDo2JaxbSdmxMapper.codelistDoToJaxb(source, codesDo2JaxbCallback);
    }

    @Override
    public void toCodelist(CodelistVersionMetamac source, Codelist target) {
        if (source == null) {
            return;
        }
        target.setShortName(toInternationalString(source.getShortName()));
        target.setDescriptionSource(toInternationalString(source.getDescriptionSource()));
        target.setIsRecommended(source.getIsRecommended());
        target.setFamily(toItem(source.getFamily()));
        target.setVariable(toItem(source.getVariable()));
    }

    @Override
    public void toCode(ItemResult source, ItemSchemeVersion itemSchemeVersion, Code target) {
        CodeMetamacResultExtensionPoint extensionPoint = (CodeMetamacResultExtensionPoint) source.getExtensionPoint();
        target.setShortName(toInternationalString(extensionPoint.getShortName()));
    }

    @Override
    public Code toCode(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        // following method will call toCode(CodeMetamac source, Code target) method, thank to callback
        return (Code) codesDo2JaxbSdmxMapper.codeDoToJaxb(source, codesDo2JaxbCallback);
    }

    @Override
    public void toCode(CodeMetamac source, Code target) {
        if (source == null) {
            return;
        }
        if (source.getVariableElement() == null) {
            target.setShortName(toInternationalString(source.getShortName()));
        } else {
            target.setShortName(toInternationalString(source.getVariableElement().getShortName()));
        }
    }

    private ReplaceTo toReplaceTo(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (CollectionUtils.isEmpty(source.getReplaceToVariables())) {
            return null;
        }
        ReplaceTo target = new ReplaceTo();
        target.setTotal(BigInteger.valueOf(source.getReplaceToVariables().size()));
        for (org.siemac.metamac.srm.core.code.domain.Variable replaceToSource : source.getReplaceToVariables()) {
            target.getReplaceTos().add(toItem(replaceToSource));
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
            target.getFamilies().add(toItem(variableFamily));
        }
        return target;
    }

    private Item toItem(org.siemac.metamac.srm.core.code.domain.CodelistFamily source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(getCode(source.getNameableArtefact()));
        target.setTitle(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private Item toItem(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(getCode(source.getNameableArtefact()));
        target.setTitle(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private Item toItem(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(getCode(source.getNameableArtefact()));
        target.setTitle(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private Resource toResource(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        Resource target = new Resource();
        target.setId(getCode(source.getMaintainableArtefact()));
        target.setUrn(getUrn(source.getMaintainableArtefact()));
        target.setTitle(toInternationalString(source.getMaintainableArtefact().getName()));
        return target;
    }
}