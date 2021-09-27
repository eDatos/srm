package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.InternationalStringAvro;
import org.siemac.metamac.srm.core.stream.message.LocalisedStringAvro;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InternationalStringDo2AvroMapper implements Do2AvroMapper<InternationalStringDto, InternationalStringAvro> {

    @Override
    public InternationalStringAvro toAvro(InternationalStringDto source) {
        InternationalStringAvro target = null;
        if (source != null && source.getTexts() != null) {
            List<LocalisedStringAvro> avroLocalisedStrings = new ArrayList<>();
            for (LocalisedStringDto localisedString : source.getTexts()) {
                LocalisedStringAvro avroLocalisedString = LocalisedStringAvro.newBuilder()
                        .setLocale(localisedString.getLocale())
                        .setLabel(localisedString.getLabel())
                        .build();
                avroLocalisedStrings.add(avroLocalisedString);
            }
            target = InternationalStringAvro.newBuilder().setLocalisedStrings(avroLocalisedStrings).build();
        }
        return target;

    }
}
