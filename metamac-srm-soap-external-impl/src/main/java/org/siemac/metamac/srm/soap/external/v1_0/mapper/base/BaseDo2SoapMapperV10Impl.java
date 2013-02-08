package org.siemac.metamac.srm.soap.external.v1_0.mapper.base;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.soap.common.v1_0.domain.InternationalString;
import org.siemac.metamac.soap.common.v1_0.domain.LocalisedString;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;

@Component
public class BaseDo2SoapMapperV10Impl implements BaseDo2SoapMapperV10 {

    @Override
    public InternationalString toInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString source) {
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

    @Override
    public XMLGregorianCalendar toDate(DateTime source) {
        return CoreCommonUtil.transformDateToXmlGregorianCalendar(source);
    }

    protected String getCode(NameableArtefact nameableArtefact) {
        if (nameableArtefact.getCodeFull() != null) {
            return nameableArtefact.getCodeFull();
        } else {
            return nameableArtefact.getCode();
        }
    }

    protected String getUrn(NameableArtefact nameableArtefact) {
        if (nameableArtefact.getUrnProvider() != null) {
            return nameableArtefact.getUrnProvider();
        } else {
            return nameableArtefact.getUrn();
        }
    }

    private LocalisedString localisedStringToLocalisedStringWebService(org.siemac.metamac.core.common.ent.domain.LocalisedString source) {
        LocalisedString localisedString = new LocalisedString();
        localisedString.setLang(source.getLocale());
        localisedString.setValue(source.getLabel());
        return localisedString;
    }
}