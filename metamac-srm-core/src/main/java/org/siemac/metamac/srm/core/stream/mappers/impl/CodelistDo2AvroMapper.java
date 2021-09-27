package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CodelistDo2AvroMapper implements Do2AvroMapper<CodelistMetamacDto, CodelistAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private RelatedResourceDo2AvroMapper relatedResourceMapper;

    @Autowired
    private AccessTypeEnumDo2AvroMapper accessTypeMapper;

    @Override
    public CodelistAvro toAvro(CodelistMetamacDto source) {
        if (source == null) {
            return null;
        }
        return CodelistAvro.newBuilder()
                .setUuid(source.getUuid())
                .setName(internationalStringMapper.toAvro(source.getName()))
                .setShortName(internationalStringMapper.toAvro(source.getShortName()))
                .setUrn(source.getUrn())
                .setVersion(source.getVersion())
                .setUriProvider(source.getUriProvider())
                .setUrnProvider(source.getUrnProvider())
                .setDescription(internationalStringMapper.toAvro(source.getDescription()))
                .setDescriptionSource(internationalStringMapper.toAvro(source.getDescriptionSource()))
                .setIsPartial(source.getIsPartial())
                .setFinalLogic(source.getFinalLogic())
                .setMaintainer(relatedResourceMapper.toAvro(source.getMaintainer()))
                .setIsExternalReference(source.getIsExternalReference())
                .setAccessType(accessTypeMapper.toAvro(source.getAccessType()))
                .setIsRecommended(source.getIsRecommended())
                .setFamily(relatedResourceMapper.toAvro(source.getFamily()))
                .setVariable(relatedResourceMapper.toAvro(source.getVariable()))
                .setCategorizations(new ArrayList<>()).build(); // TODO: EDATOS-3433 fill this attribute
    }
}
