package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.AccessTypeEnumAvro;
import org.springframework.stereotype.Component;

@Component
public class AccessTypeEnumDo2AvroMapper implements Do2AvroMapper<AccessTypeEnum, AccessTypeEnumAvro> {

    @Override
    public AccessTypeEnumAvro toAvro(AccessTypeEnum source) {
        if (source == null) {
            return null;
        }
        return AccessTypeEnumAvro.valueOf(source.name());
    }
}