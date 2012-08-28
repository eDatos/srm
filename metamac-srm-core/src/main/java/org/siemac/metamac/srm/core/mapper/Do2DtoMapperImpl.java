package org.siemac.metamac.srm.core.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.ComponentList;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersion;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ComponentListDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DataStructureDefinitionExtendDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("do2DtoMapper")
public class Do2DtoMapperImpl implements Do2DtoMapper {

    // ------------------------------------------------------------
    // DSDs
    // ------------------------------------------------------------

    @Autowired
    @Qualifier("do2DtoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.mapper.Do2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public <U extends ComponentDto> U componentToComponentDto(TypeDozerCopyMode typeDozerCopyMode, Component component) {
        return (U) do2DtoMapperSdmxSrm.componentToComponentDto(typeDozerCopyMode, component);
    }

    @Override
    public <U extends ComponentListDto> U componentListToComponentListDto(TypeDozerCopyMode typeDozerCopyMode, ComponentList componentList) {
        return (U) do2DtoMapperSdmxSrm.componentListToComponentListDto(typeDozerCopyMode, componentList);
    }

    @Override
    public DataStructureDefinitionDto dataStructureDefinitionToDataStructureDefinitionDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersion dataStructureDefinitionVersion) {
        return do2DtoMapperSdmxSrm.dataStructureDefinitionToDataStructureDefinitionDto(typeDozerCopyMode, dataStructureDefinitionVersion);
    }

    @Override
    public DataStructureDefinitionExtendDto dataStructureDefinitionToDataStructureDefinitionExtendDto(TypeDozerCopyMode typeDozerCopyMode, DataStructureDefinitionVersion dataStructureDefinitionVersion) {
        return do2DtoMapperSdmxSrm.dataStructureDefinitionToDataStructureDefinitionExtendDto(typeDozerCopyMode, dataStructureDefinitionVersion);
    }

    // ------------------------------------------------------------
    // CONCEPTS
    // ------------------------------------------------------------

    @Override
    public ConceptSchemeMetamacDto conceptSchemeMetamacDoToDto(ConceptSchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        ConceptSchemeMetamacDto target = new ConceptSchemeMetamacDto();
        target.setProcStatus(source.getProcStatus());
        target.setType(source.getType());
        target.setRelatedOperation(do2DtoMapperSdmxSrm.externalItemToExternalItemDto(TypeDozerCopyMode.UPDATE, source.getRelatedOperation()));
        target.setProductionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getProductionValidationDate()));
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setInternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getInternalPublicationDate()));
        target.setInternalPublicationUser(source.getInternalPublicationUser());
        target.setExternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getExternalPublicationDate()));
        target.setExternalPublicationUser(source.getExternalPublicationUser());

        do2DtoMapperSdmxSrm.conceptSchemeDoToDto(source, target);
        return target;
    }

    @Override
    public List<ConceptSchemeMetamacDto> conceptSchemeMetamacDoListToDtoList(List<ConceptSchemeVersionMetamac> conceptSchemeVersions) {
        List<ConceptSchemeMetamacDto> conceptSchemeMetamacDtos = new ArrayList<ConceptSchemeMetamacDto>();
        for (ConceptSchemeVersionMetamac conceptSchemeVersion : conceptSchemeVersions) {
            conceptSchemeMetamacDtos.add(conceptSchemeMetamacDoToDto(conceptSchemeVersion));
        }
        return conceptSchemeMetamacDtos;
    }

    @Override
    public ConceptMetamacDto conceptMetamacDoToDto(ConceptMetamac source) {
        if (source == null) {
            return null;
        }
        ConceptMetamacDto target = new ConceptMetamacDto();

        // TODO
//        target.setPluralName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.UPDATE, source.getPluralName()));
//        target.setAcronym(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.UPDATE, source.getAcronym()));
//        target.setDescriptionSource(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.UPDATE, source.getDescriptionSource()));
//        target.setContext(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.UPDATE, source.getContext()));
//        target.setDocMethod(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.UPDATE, source.getDocMethod()));
//        target.setDerivation(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.UPDATE, source.getDerivation()));
//        target.setLegalActs(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.UPDATE, source.getLegalActs()));
//
//        do2DtoMapperSdmxSrm.conceptDoToDto(source, target);
        return target;
    }

    @Override
    public List<ConceptMetamacDto> conceptMetamacDoListToDtoList(List<ConceptMetamac> conceptMetamacs) {
        List<ConceptMetamacDto> conceptMetamacDtos = new ArrayList<ConceptMetamacDto>();
        for (ConceptMetamac conceptMetamac : conceptMetamacs) {
            conceptMetamacDtos.add(conceptMetamacDoToDto(conceptMetamac));
        }
        return conceptMetamacDtos;
    }

}
