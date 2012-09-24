package org.siemac.metamac.srm.web.server.mock;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;

public class MockService {

    public static InternationalStringDto createInternationalStringDto(String text_es, String text_en) {
        InternationalStringDto internationalStringDto = new InternationalStringDto();
        LocalisedStringDto locale_es = new LocalisedStringDto();
        locale_es.setLocale("es");
        locale_es.setLabel(text_es);
        LocalisedStringDto locale_en = new LocalisedStringDto();
        locale_en.setLocale("en");
        locale_en.setLabel(text_en);
        internationalStringDto.addText(locale_es);
        internationalStringDto.addText(locale_en);
        return internationalStringDto;
    }

}
