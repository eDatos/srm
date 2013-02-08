package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import java.math.BigInteger;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.common.v1_0.domain.Item;
import org.siemac.metamac.soap.common.v1_0.domain.Resource;
import org.siemac.metamac.soap.criteria.mapper.SculptorCriteria2SoapCriteria;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.CodelistFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.ReplaceTo;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilyCodes;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.soap.external.v1_0.mapper.base.BaseDo2SoapMapperV10Impl;
import org.springframework.stereotype.Component;

@Component
public class CodesDo2SoapMapperImpl extends BaseDo2SoapMapperV10Impl implements CodesDo2SoapMapper {

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
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> sources, Integer limit) throws MetamacException {
        VariableFamilies targets = new VariableFamilies();
        // Values
        if (!CollectionUtils.isEmpty(sources.getValues())) {
            for (org.siemac.metamac.srm.core.code.domain.VariableFamily source : sources.getValues()) {
                targets.getVariableFamily().add(toVariableFamily(source));
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
    public Variables toVariables(PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> sources, Integer limit) throws MetamacException {
        Variables targets = new Variables();
        // Values
        if (!CollectionUtils.isEmpty(sources.getValues())) {
            for (org.siemac.metamac.srm.core.code.domain.Variable source : sources.getValues()) {
                targets.getVariable().add(toVariable(source));
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
                targets.getCodelistFamily().add(toCodelistFamily(source));
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
                targets.getCodelist().add(toResource(source));
            }
        }
        // Pagination
        SculptorCriteria2SoapCriteria.toPagedResult(sources, targets, limit);
        return targets;
    }

    private ReplaceTo toReplaceTo(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (CollectionUtils.isEmpty(source.getReplaceToVariables())) {
            return null;
        }
        ReplaceTo target = new ReplaceTo();
        target.setTotal(BigInteger.valueOf(source.getReplaceToVariables().size()));
        for (org.siemac.metamac.srm.core.code.domain.Variable replaceToSource : source.getReplaceToVariables()) {
            target.getReplaceTo().add(toItem(replaceToSource));
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
            target.getFamily().add(toItem(variableFamily));
        }
        return target;
    }

    private Item toItem(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
        target.setId(getCode(source.getNameableArtefact()));
        target.setName(toInternationalString(source.getNameableArtefact().getName()));
        return target;
    }

    private Item toItem(org.siemac.metamac.srm.core.code.domain.Variable source) {
        if (source == null) {
            return null;
        }
        Item target = new Item();
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
        target.setUrn(getUrn(source.getMaintainableArtefact()));
        target.setName(toInternationalString(source.getMaintainableArtefact().getName()));
        return target;
    }
}