package org.siemac.metamac.srm.core.stream.mappers.impl;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategorisationDo2AvroMapper implements Do2AvroMapper<Categorisation, RelatedResourceAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private RelatedResourceTypeEnumDo2AvroMapper relatedResourceTypeEnumMapper;

    @Override
    public RelatedResourceAvro toAvro(Categorisation source) {
        if (source == null) {
            return null;
        }
        return RelatedResourceAvro.newBuilder()
                .setUuid(source.getUuid())
                .setUrn(source.getMaintainableArtefact().getUrn())
                .setCode(source.getMaintainableArtefact().getCode())
                .setUrnProvider(source.getMaintainableArtefact().getUrnProvider())
                .setVersion(source.getVersion())
                .setCodeNested(null)
                .setTitle(internationalStringMapper.toAvro(source.getMaintainableArtefact().getName()))
                .setType(relatedResourceTypeEnumMapper.toAvro(RelatedResourceTypeEnum.CATEGORISATION)).build();
    }
}
