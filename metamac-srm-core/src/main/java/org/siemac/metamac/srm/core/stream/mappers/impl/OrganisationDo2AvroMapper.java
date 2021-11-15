package org.siemac.metamac.srm.core.stream.mappers.impl;

import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganisationDo2AvroMapper implements Do2AvroMapper<Organisation, RelatedResourceAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private RelatedResourceTypeEnumDo2AvroMapper relatedResourceTypeEnumMapper;

    @Override
    public RelatedResourceAvro toAvro(Organisation source) {
        if (source == null) {
            return null;
        }
        return RelatedResourceAvro.newBuilder()
                .setUuid(source.getUuid())
                .setUrn(source.getNameableArtefact().getUrn())
                .setCode(source.getNameableArtefact().getCode())
                .setUrnProvider(source.getNameableArtefact().getUrnProvider())
                .setVersion(source.getVersion())
                .setCodeNested(null)
                .setTitle(internationalStringMapper.toAvro(source.getNameableArtefact().getName()))
                .setType(relatedResourceTypeEnumMapper.toAvro(RelatedResourceTypeEnum.AGENCY)).build();
    }
}
