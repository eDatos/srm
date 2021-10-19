package org.siemac.metamac.srm.core.stream.mappers;

import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.stream.mappers.impl.*;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganisationSchemeDo2AvroMapper implements Do2AvroMapper<OrganisationSchemeVersionMetamac, OrganisationSchemeAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private OrganisationDo2AvroMapper maintainerMapper;

    @Autowired
    private CategorisationDo2AvroMapper categorisationMapper;

    @Autowired
    private ProcStatusEnumDo2AvroMapper procStatusMapper;

    @Override
    public OrganisationSchemeAvro toAvro(OrganisationSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        return OrganisationSchemeAvro.newBuilder()
                .setUuid(source.getUuid())
                .setName(internationalStringMapper.toAvro(source.getMaintainableArtefact().getName()))
                .setUrn(source.getMaintainableArtefact().getUrn())
                .setVersion(source.getVersion())
                .setUriProvider(source.getMaintainableArtefact().getUriProvider())
                .setUrnProvider(source.getMaintainableArtefact().getUrnProvider())
                .setDescription(internationalStringMapper.toAvro(source.getMaintainableArtefact().getDescription()))
                .setIsPartial(source.getIsPartial())
                .setFinalLogic(source.getMaintainableArtefact().getFinalLogic())
                .setMaintainer(maintainerMapper.toAvro(source.getMaintainableArtefact().getMaintainer()))
                .setProcStatus(procStatusMapper.toAvro(source.getLifeCycleMetadata().getProcStatus()))
                .setCategorizations(categorisationMapper.toAvros(source.getMaintainableArtefact().getCategorisations())).build();
    }
}
