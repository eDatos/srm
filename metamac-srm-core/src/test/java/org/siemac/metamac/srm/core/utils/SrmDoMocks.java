package org.siemac.metamac.srm.core.utils;

import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.core.common.bt.domain.ExternalItemBt;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;


public class SrmDoMocks {

    public static InternationalString mockInternationalString() {
        InternationalString internationalString = new InternationalString();
        LocalisedString es = new LocalisedString();
        es.setLabel(MetamacMocks.mockString(10) + " en Español");
        es.setLocale("es");
        LocalisedString en = new LocalisedString();
        en.setLabel(MetamacMocks.mockString(10) + " in English");
        en.setLocale("en");
        internationalString.addText(es);
        internationalString.addText(en);
        return internationalString;
    }

    public static ExternalItemBt mockAgencyExternalItemBt() {
        String code = MetamacMocks.mockString(10);
        return new ExternalItemBt("http://sdmx/v2.1/agency/" + code, code, TypeExternalArtefactsEnum.AGENCY);
    }
}
