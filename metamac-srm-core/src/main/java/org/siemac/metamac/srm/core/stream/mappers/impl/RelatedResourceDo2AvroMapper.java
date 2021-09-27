package org.siemac.metamac.srm.core.stream.mappers.impl;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelatedResourceDo2AvroMapper implements Do2AvroMapper<RelatedResourceDto, RelatedResourceAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private RelatedResourceTypeEnumDo2AvroMapper relatedResourceTypeEnumMapper;

    @Override
    public RelatedResourceAvro toAvro(RelatedResourceDto source) {
        if (source == null) {
            return null;
        }
        return RelatedResourceAvro.newBuilder()
                .setUuid(source.getUuid())
                .setUrn(source.getUrn())
                .setCode(source.getCode())
                .setUrnProvider(source.getUrnProvider())
                .setVersion(source.getVersion())
                .setCodeNested(source.getCodeNested())
                .setTitle(internationalStringMapper.toAvro(source.getTitle()))
                .setType(relatedResourceTypeEnumMapper.toAvro(source.getType())).build();
    }
}
