package org.siemac.metamac.srm.core.stream.mappers.impl;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceTypeEnumAvro;
import org.springframework.stereotype.Component;

@Component
public class RelatedResourceTypeEnumDo2AvroMapper implements Do2AvroMapper<RelatedResourceTypeEnum, RelatedResourceTypeEnumAvro> {

    @Override
    public RelatedResourceTypeEnumAvro toAvro(RelatedResourceTypeEnum source) {
        if (source == null) {
            return null;
        }
        return RelatedResourceTypeEnumAvro.valueOf(source.name());
    }
}
