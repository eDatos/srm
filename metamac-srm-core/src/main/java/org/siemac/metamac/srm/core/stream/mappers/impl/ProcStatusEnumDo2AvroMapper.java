package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.ProcStatusEnumAvro;
import org.springframework.stereotype.Component;

@Component
public class ProcStatusEnumDo2AvroMapper implements Do2AvroMapper<ProcStatusEnum, ProcStatusEnumAvro> {

    @Override
    public ProcStatusEnumAvro toAvro(ProcStatusEnum source) {
        if (source == null) {
            return null;
        }
        return ProcStatusEnumAvro.valueOf(source.name());
    }
}
