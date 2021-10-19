package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.DataStructureDefinitionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataStructureDefinitionDo2AvroMapper implements Do2AvroMapper<DataStructureDefinitionVersionMetamac, DataStructureDefinitionAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private OrganisationDo2AvroMapper maintainerMapper;

    @Autowired
    private CategorisationDo2AvroMapper categorisationMapper;

    @Autowired
    private ProcStatusEnumDo2AvroMapper procStatusMapper;

    @Override
    public DataStructureDefinitionAvro toAvro(DataStructureDefinitionVersionMetamac source) {
        if (source == null) {
            return null;
        }
        return DataStructureDefinitionAvro.newBuilder()
                .setUuid(source.getUuid())
                .setName(internationalStringMapper.toAvro(source.getMaintainableArtefact().getName()))
                .setUrn(source.getMaintainableArtefact().getUrn())
                .setVersion(source.getVersion())
                .setUriProvider(source.getMaintainableArtefact().getUriProvider())
                .setUrnProvider(source.getMaintainableArtefact().getUrnProvider())
                .setDescription(internationalStringMapper.toAvro(source.getMaintainableArtefact().getDescription()))
                .setFinalLogic(source.getMaintainableArtefact().getFinalLogic())
                .setMaintainer(maintainerMapper.toAvro(source.getMaintainableArtefact().getMaintainer()))
                .setProcStatus(procStatusMapper.toAvro(source.getLifeCycleMetadata().getProcStatus()))
                .setCategorizations(categorisationMapper.toAvros(source.getMaintainableArtefact().getCategorisations())).build();
    }
}
