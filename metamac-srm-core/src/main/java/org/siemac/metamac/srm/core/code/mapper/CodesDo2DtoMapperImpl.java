package org.siemac.metamac.srm.core.code.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementOperation;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementOperationDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyBasicDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("codesDo2DtoMapper")
public class CodesDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements CodesDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.code.mapper.CodesDo2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public CodelistMetamacDto codelistMetamacDoToDto(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        CodelistMetamacDto target = new CodelistMetamacDto();

        target.setShortName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        target.setDescriptionSource(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getDescriptionSource()));
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());
        target.setReplacedByCodelist(codelistDoToRelatedResourceDto(source.getReplacedByCodelist()));
        for (CodelistVersionMetamac replaceTo : source.getReplaceToCodelists()) {
            target.addReplaceToCodelist(codelistDoToRelatedResourceDto(replaceTo));
        }
        target.setFamily(codelistFamilyDoToRelatedResourceDto(source.getFamily()));
        target.setVariable(variableDoToRelatedResourceDto(source.getVariable()));
        target.setLifeCycle(lifeCycleDoToDto(source.getLifeCycleMetadata()));
        target.setDefaultOrderVisualisation(codelistOrderVisualisationDoToRelatedResourceDto(source.getDefaultOrderVisualisation()));
        target.setDefaultOpennessVisualisation(codelistOpennessVisualisationDoToRelatedResourceDto(source.getDefaultOpennessVisualisation()));
        do2DtoMapperSdmxSrm.codelistDoToDto(source, target);
        return target;
    }

    @Override
    public CodelistMetamacBasicDto codelistMetamacDoToBasicDto(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        CodelistMetamacBasicDto target = new CodelistMetamacBasicDto();
        target.setIsRecommended(source.getIsRecommended());
        target.setVariable(variableDoToRelatedResourceDto(source.getVariable()));
        itemSchemeVersionDoToItemSchemeBasicDto(source, source.getLifeCycleMetadata(), target);
        return target;
    }

    @Override
    public RelatedResourceDto codelistMetamacDoToRelatedResourceDto(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.codelistDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public List<CodelistMetamacBasicDto> codelistMetamacDoListToDtoList(List<CodelistVersionMetamac> codelistVersions) {
        List<CodelistMetamacBasicDto> codelistMetamacDtos = new ArrayList<CodelistMetamacBasicDto>(codelistVersions.size());
        for (CodelistVersionMetamac codelistVersion : codelistVersions) {
            codelistMetamacDtos.add(codelistMetamacDoToBasicDto(codelistVersion));
        }
        return codelistMetamacDtos;
    }

    @Override
    public CodeMetamacDto codeMetamacDoToDto(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        CodeMetamacDto target = new CodeMetamacDto();
        target.setVariableElement(variableElementDoToRelatedResourceDto(source.getVariableElement()));
        target.setShortName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        do2DtoMapperSdmxSrm.codeDoToDto(source, target);

        return target;
    }

    @Override
    public CodeMetamacBasicDto codeMetamacDoToBasicDto(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        CodeMetamacBasicDto target = new CodeMetamacBasicDto();
        itemDoToItemBasicDto(source, target);
        return target;
    }

    @Override
    public RelatedResourceDto codeMetamacDoToRelatedResourceDto(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.codeDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public CodelistFamilyDto codelistFamilyDoToDto(CodelistFamily source) {
        if (source == null) {
            return null;
        }
        CodelistFamilyDto target = new CodelistFamilyDto();
        do2DtoMapperSdmxSrm.nameableArtefactToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getNameableArtefact(), target);

        // Overwrite these values in the final DTO (if not, these values are taken from the AnnotableArtefact entity)
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());

        return target;
    }

    @Override
    public CodelistFamilyBasicDto codelistFamilyDoToBasicDto(CodelistFamily source) {
        if (source == null) {
            return null;
        }
        CodelistFamilyBasicDto target = new CodelistFamilyBasicDto();
        nameableArtefactDoToNameableArtefactBasicDto(source.getNameableArtefact(), target);
        return target;
    }

    @Override
    public VariableFamilyDto variableFamilyDoToDto(VariableFamily source) {
        if (source == null) {
            return null;
        }
        VariableFamilyDto target = new VariableFamilyDto();
        do2DtoMapperSdmxSrm.nameableArtefactToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getNameableArtefact(), target);

        // Overwrite these values in the final DTO (if not, these values are taken from the AnnotableArtefact entity)
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());

        return target;
    }

    @Override
    public VariableFamilyBasicDto variableFamilyDoToBasicDto(VariableFamily source) {
        if (source == null) {
            return null;
        }
        VariableFamilyBasicDto target = new VariableFamilyBasicDto();
        nameableArtefactDoToNameableArtefactBasicDto(source.getNameableArtefact(), target);
        return target;
    }

    @Override
    public VariableDto variableDoToDto(Variable source) {
        if (source == null) {
            return null;
        }
        VariableDto target = new VariableDto();
        target.setShortName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        target.setValidFrom(CoreCommonUtil.transformDateTimeToDate(source.getValidFrom()));
        target.setValidTo(CoreCommonUtil.transformDateTimeToDate(source.getValidTo()));
        do2DtoMapperSdmxSrm.nameableArtefactToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getNameableArtefact(), target);
        for (VariableFamily variableFamily : source.getFamilies()) {
            target.addFamily(variableFamilyDoToRelatedResourceDto(variableFamily));
        }
        target.setReplacedByVariable(variableDoToRelatedResourceDto(source.getReplacedByVariable()));
        for (Variable replaceTo : source.getReplaceToVariables()) {
            target.addReplaceToVariable(variableDoToRelatedResourceDto(replaceTo));
        }

        // Overwrite these values in the final DTO (if not, these values are taken from the AnnotableArtefact entity)
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());

        return target;
    }

    @Override
    public VariableBasicDto variableDoToBasicDto(Variable source) {
        if (source == null) {
            return null;
        }
        VariableBasicDto target = new VariableBasicDto();
        nameableArtefactDoToNameableArtefactBasicDto(source.getNameableArtefact(), target);
        return target;
    }

    @Override
    public RelatedResourceDto variableDoToRelatedResourceDto(Variable source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        do2DtoMapperSdmxSrm.nameableArtefactDoToRelatedResourceDto(source.getNameableArtefact(), target);
        target.setType(null);
        return target;
    }

    @Override
    public VariableElementDto variableElementDoToDto(VariableElement source) {
        if (source == null) {
            return null;
        }
        VariableElementDto target = new VariableElementDto();
        target.setShortName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        target.setValidFrom(CoreCommonUtil.transformDateTimeToDate(source.getValidFrom()));
        target.setValidTo(CoreCommonUtil.transformDateTimeToDate(source.getValidTo()));
        do2DtoMapperSdmxSrm.identifiableArtefactToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getIdentifiableArtefact(), target);
        target.setVariable(variableDoToRelatedResourceDto(source.getVariable()));
        target.setReplacedByVariableElement(variableElementDoToRelatedResourceDto(source.getReplacedByVariableElement()));
        for (VariableElement replaceTo : source.getReplaceToVariableElements()) {
            target.addReplaceToVariableElement(variableElementDoToRelatedResourceDto(replaceTo));
        }

        // Overwrite these values in the final DTO (if not, these values are taken from the AnnotableArtefact entity)
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());

        return target;
    }

    @Override
    public VariableElementBasicDto variableElementDoToBasicDto(VariableElement source) {
        if (source == null) {
            return null;
        }
        VariableElementBasicDto target = new VariableElementBasicDto();
        target.setShortName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        identifiableArtefactDoToIdentifiableArtefactBasicDto(source.getIdentifiableArtefact(), target);
        return target;
    }

    @Override
    public RelatedResourceDto variableElementDoToRelatedResourceDto(VariableElement source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        target.setId(source.getId()); // Database id. Only to efficient save operations
        target.setCode(source.getIdentifiableArtefact().getCode());
        target.setTitle(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        target.setUrn(source.getIdentifiableArtefact().getUrn());
        target.setUrnProvider(source.getIdentifiableArtefact().getUrnProvider());

        target.setType(null);
        return target;
    }

    @Override
    public VariableElementOperationDto variableElementOperationDoToDto(VariableElementOperation source) {
        if (source == null) {
            return null;
        }
        VariableElementOperationDto target = new VariableElementOperationDto();
        target.setCode(source.getCode());
        target.setOperationType(source.getOperationType());
        target.setVariable(variableDoToRelatedResourceDto(source.getVariable()));
        target.getSources().addAll(variableElementsDoToRelatedResourcesDto(source.getSources()));
        target.getTargets().addAll(variableElementsDoToRelatedResourcesDto(source.getTargets()));

        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());

        return target;
    }

    @Override
    public List<VariableElementOperationDto> variableElementOperationsDoToDto(List<VariableElementOperation> sources) {
        if (sources == null) {
            return null;
        }
        List<VariableElementOperationDto> targets = new ArrayList<VariableElementOperationDto>(sources.size());
        for (VariableElementOperation source : sources) {
            targets.add(variableElementOperationDoToDto(source));
        }
        return targets;
    }

    @Override
    public CodelistVisualisationDto codelistOrderVisualisationDoToDto(CodelistOrderVisualisation source) {
        if (source == null) {
            return null;
        }
        CodelistVisualisationDto target = new CodelistVisualisationDto();
        do2DtoMapperSdmxSrm.nameableArtefactToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getNameableArtefact(), target);
        target.setCodelist(codelistDoToRelatedResourceDto(source.getCodelistVersion()));

        // Overwrite these values in the final DTO (if not, these values are taken from the AnnotableArtefact entity)
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());
        return target;
    }

    @Override
    public List<CodelistVisualisationDto> codelistOrderVisualisationsDoToDto(List<CodelistOrderVisualisation> sources) {
        if (sources == null) {
            return null;
        }
        List<CodelistVisualisationDto> targets = new ArrayList<CodelistVisualisationDto>(sources.size());
        for (CodelistOrderVisualisation source : sources) {
            CodelistVisualisationDto target = codelistOrderVisualisationDoToDto(source);
            targets.add(target);
        }
        return targets;
    }

    @Override
    public CodelistVisualisationDto codelistOpennessVisualisationDoToDto(CodelistOpennessVisualisation source) {
        if (source == null) {
            return null;
        }
        CodelistVisualisationDto target = new CodelistVisualisationDto();
        do2DtoMapperSdmxSrm.nameableArtefactToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getNameableArtefact(), target);
        target.setCodelist(codelistDoToRelatedResourceDto(source.getCodelistVersion()));

        // Overwrite these values in the final DTO (if not, these values are taken from the AnnotableArtefact entity)
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());
        return target;
    }

    @Override
    public List<CodelistVisualisationDto> codelistOpennessVisualisationsDoToDto(List<CodelistOpennessVisualisation> sources) {
        if (sources == null) {
            return null;
        }
        List<CodelistVisualisationDto> targets = new ArrayList<CodelistVisualisationDto>(sources.size());
        for (CodelistOpennessVisualisation source : sources) {
            CodelistVisualisationDto target = codelistOpennessVisualisationDoToDto(source);
            targets.add(target);
        }
        return targets;
    }

    @Override
    public RelatedResourceDto codelistOrderVisualisationDoToRelatedResourceDto(CodelistOrderVisualisation source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        do2DtoMapperSdmxSrm.nameableArtefactDoToRelatedResourceDto(source.getNameableArtefact(), target);
        target.setType(null);
        return target;
    }

    @Override
    public RelatedResourceDto codelistOpennessVisualisationDoToRelatedResourceDto(CodelistOpennessVisualisation source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        do2DtoMapperSdmxSrm.nameableArtefactDoToRelatedResourceDto(source.getNameableArtefact(), target);
        target.setType(null);
        return target;
    }

    private RelatedResourceDto codelistDoToRelatedResourceDto(CodelistVersionMetamac source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        do2DtoMapperSdmxSrm.nameableArtefactDoToRelatedResourceDto(source.getMaintainableArtefact(), target);
        target.setType(RelatedResourceTypeEnum.CODELIST);
        return target;
    }

    private RelatedResourceDto codelistFamilyDoToRelatedResourceDto(CodelistFamily source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        do2DtoMapperSdmxSrm.nameableArtefactDoToRelatedResourceDto(source.getNameableArtefact(), target);
        target.setType(null);
        return target;
    }

    private RelatedResourceDto variableFamilyDoToRelatedResourceDto(VariableFamily source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = new RelatedResourceDto();
        do2DtoMapperSdmxSrm.nameableArtefactDoToRelatedResourceDto(source.getNameableArtefact(), target);
        target.setType(null);
        return target;
    }

    private List<RelatedResourceDto> variableElementsDoToRelatedResourcesDto(List<VariableElement> sources) {
        if (sources == null) {
            return null;
        }
        List<RelatedResourceDto> targets = new ArrayList<RelatedResourceDto>(sources.size());
        for (VariableElement source : sources) {
            targets.add(variableElementDoToRelatedResourceDto(source));
        }
        return targets;
    }
}