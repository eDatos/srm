package org.siemac.metamac.srm.soap.external.v1_0.mapper.base;

import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.soap.common.v1_0.domain.InternationalString;
import org.siemac.metamac.soap.common.v1_0.domain.LocalisedString;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;

public abstract class BaseDo2SoapMapperV10Impl {

    protected InternationalString toInternationalString(com.arte.statistic.sdmx.srm.core.common.domain.InternationalString source) {
        if (source == null || source.getTexts() == null || source.getTexts().size() == 0) {
            return null;
        }

        InternationalString internationalString = new InternationalString();
        for (com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString item : source.getTexts()) {
            LocalisedString localisedString = localisedStringToLocalisedStringWebService(item);
            internationalString.getTexts().add(localisedString);
        }

        return internationalString;
    }

    protected InternationalString toInternationalString(Map<String, String> sources) {
        if (sources == null || sources.size() == 0) {
            return null;
        }
        InternationalString internationalString = new InternationalString();
        for (String locale : sources.keySet()) {
            LocalisedString localisedString = new LocalisedString();
            localisedString.setLang(locale);
            localisedString.setValue(sources.get(locale));
            internationalString.getTexts().add(localisedString);
        }
        return internationalString;
    }

    protected Date toDate(DateTime source) {
        return CoreCommonUtil.transformDateTimeToDate(source);
    }

    protected String getCode(NameableArtefact nameableArtefact) {
        if (nameableArtefact.getCodeFull() != null) {
            return nameableArtefact.getCodeFull();
        } else {
            return nameableArtefact.getCode();
        }
    }

    protected boolean canResourceBeProvidedByApi(MaintainableArtefact source) {
        return source.getPublicLogic();
    }

    private LocalisedString localisedStringToLocalisedStringWebService(com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString source) {
        LocalisedString localisedString = new LocalisedString();
        localisedString.setLang(source.getLocale());
        localisedString.setValue(source.getLabel());
        return localisedString;
    }

}