package org.siemac.metamac.srm.soap.external.v1_0.mapper.code;

import java.math.BigInteger;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.InternationalString;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.LocalisedString;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.soap.structural_resources.v1_0.domain.VariableFamily;
import org.springframework.stereotype.Component;

@Component
public class CodesDo2SoapMapperImpl implements CodesDo2SoapMapper {

    @Override
    public VariableFamily toVariableFamily(org.siemac.metamac.srm.core.code.domain.VariableFamily source) {
        if (source == null) {
            return null;
        }
        VariableFamily target = new VariableFamily();
        target.setId(source.getNameableArtefact().getCode());
        target.setName(internationalStringToWebService(source.getNameableArtefact().getName()));

        return target;
    }

    @Override
    public VariableFamilies toVariableFamilies(PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> sources, Integer pageSize) throws MetamacException {
        VariableFamilies targets = new VariableFamilies();

        // Values
        if (!CollectionUtils.isEmpty(sources.getValues())) {
            for (org.siemac.metamac.srm.core.code.domain.VariableFamily source : sources.getValues()) {
                targets.getVariableFamily().add(toVariableFamily(source));
            }
        }

        // Pagination TODO librería común?
        targets.setOffset(BigInteger.valueOf(sources.getStartRow()));
        targets.setLimit(BigInteger.valueOf(pageSize)); // when PagingParameter is build as rowAccess pageSize is unknown
        if (sources.isTotalCounted()) {
            targets.setTotal(BigInteger.valueOf(sources.getTotalRows()));
        }
        return targets;
    }

    private InternationalString internationalStringToWebService(org.siemac.metamac.core.common.ent.domain.InternationalString source) {
        if (source == null || source.getTexts() == null || source.getTexts().size() == 0) {
            return null;
        }

        InternationalString internationalString = new InternationalString();
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString item : source.getTexts()) {
            LocalisedString localisedString = localisedStringToLocalisedStringWebService(item);
            internationalString.getText().add(localisedString);
        }

        return internationalString;
    }

    private LocalisedString localisedStringToLocalisedStringWebService(org.siemac.metamac.core.common.ent.domain.LocalisedString source) {
        LocalisedString localisedString = new LocalisedString();
        localisedString.setLang(source.getLocale());
        localisedString.setValue(source.getLabel());
        return localisedString;
    }
}