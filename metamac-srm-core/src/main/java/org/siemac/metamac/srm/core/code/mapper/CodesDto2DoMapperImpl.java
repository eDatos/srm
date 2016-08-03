package org.siemac.metamac.srm.core.code.mapper;

import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistFamily;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisationRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisationRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementRepository;
import org.siemac.metamac.srm.core.code.domain.VariableFamily;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyRepository;
import org.siemac.metamac.srm.core.code.domain.VariableRepository;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistFamilyDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.code.dto.VariableDto;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.code.dto.VariableFamilyDto;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

@org.springframework.stereotype.Component("codesDto2DoMapper")
public class CodesDto2DoMapperImpl extends BaseDto2DoMapperImpl implements CodesDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.code.mapper.CodesDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private CodelistVersionMetamacRepository                               codelistVersionMetamacRepository;

    @Autowired
    private CodeMetamacRepository                                          codeMetamacRepository;

    @Autowired
    private CodelistFamilyRepository                                       codelistFamilyRepository;

    @Autowired
    private VariableFamilyRepository                                       variableFamilyRepository;

    @Autowired
    private VariableRepository                                             variableRepository;

    @Autowired
    private VariableElementRepository                                      variableElementRepository;

    @Autowired
    private CodelistOrderVisualisationRepository                           codelistOrderVisualisationRepository;

    @Autowired
    private CodelistOpennessVisualisationRepository                        codelistOpennessVisualisationRepository;

    @Override
    public CodelistVersionMetamac codelistDtoToDo(CodelistMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CodelistVersionMetamac target = null;
        if (source.getUrn() == null) {
            target = new CodelistVersionMetamac();
        } else {
            target = retrieveCodelist(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }
        // Modifiable attributes
        target.setShortName(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getShortName(), target.getShortName(), ServiceExceptionParameters.CODELIST_SHORT_NAME));
        target.setDescriptionSource(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getDescriptionSource(), target.getDescriptionSource(),
                ServiceExceptionParameters.CODELIST_DESCRIPTION_SOURCE));
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());

        target.removeAllReplaceToCodelists();

        for (RelatedResourceDto replaceToCodelist : source.getReplaceToCodelists()) {
            // note: do not use 'addReplaceToCodelist' method, to can check in service if any "replaceTo" variable was replaced by another variable
            target.getReplaceToCodelists().add(retrieveCodelist(replaceToCodelist.getUrn()));
        }
        // note: replacedBy metadata is ignored, because it will be updated by replaceTo metadata

        if (source.getFamily() != null) {
            target.setFamily(retrieveCodelistFamily(source.getFamily().getUrn()));
        } else {
            target.setFamily(null);
        }
        target.setIsVariableUpdated(guessIsCodelistVariableUpdated(source, target));
        if (source.getVariable() != null) {
            target.setVariable(retrieveVariable(source.getVariable().getUrn()));
        } else {
            target.setVariable(null);
        }
        if (source.getDefaultOrderVisualisation() != null) {
            target.setDefaultOrderVisualisation(retrieveCodelistOrderVisualisation(source.getDefaultOrderVisualisation().getUrn()));
        } else {
            target.setDefaultOrderVisualisation(null);
        }
        if (source.getDefaultOpennessVisualisation() != null) {
            target.setDefaultOpennessVisualisation(retrieveCodelistOpennessVisualisation(source.getDefaultOpennessVisualisation().getUrn()));
        } else {
            target.setDefaultOpennessVisualisation(null);
        }

        dto2DoMapperSdmxSrm.codelistDtoToDo(source, target);

        return target;
    }

    @Override
    public CodeMetamac codeDtoToDo(CodeMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CodeMetamac target = null;
        if (source.getUrn() == null) {
            target = new CodeMetamac();
        } else {
            target = retrieveCode(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        if (source.getVariableElement() != null) {
            target.setVariableElement(retrieveVariableElement(source.getVariableElement().getUrn()));
        } else {
            target.setVariableElement(null);
        }
        target.setShortName(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getShortName(), target.getShortName(), ServiceExceptionParameters.CODE_SHORT_NAME));

        dto2DoMapperSdmxSrm.codeDtoToDo(source, target);

        return target;
    }

    @Override
    public CodelistFamily codelistFamilyDtoToDo(CodelistFamilyDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CodelistFamily target = null;
        if (source.getUrn() == null) {
            target = new CodelistFamily();
        } else {
            target = retrieveCodelistFamily(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        if (target.getId() == null) {
            target.setNameableArtefact(new NameableArtefact());
        }

        target.setNameableArtefact(dto2DoMapperSdmxSrm.nameableArtefactToEntity(source, target.getNameableArtefact()));

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increment "version" attribute
        target.setUpdateDate(new DateTime());

        return target;
    }

    @Override
    public VariableFamily variableFamilyDtoToDo(VariableFamilyDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        VariableFamily target = null;
        if (source.getUrn() == null) {
            target = new VariableFamily();
        } else {
            target = retrieveVariableFamily(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        if (target.getId() == null) {
            target.setNameableArtefact(new NameableArtefact());
        }

        target.setNameableArtefact(dto2DoMapperSdmxSrm.nameableArtefactToEntity(source, target.getNameableArtefact()));

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increment "version" attribute
        target.setUpdateDate(new DateTime());

        return target;
    }

    @Override
    public Variable variableDtoToDo(VariableDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        Variable target = null;
        if (source.getUrn() == null) {
            target = new Variable();
        } else {
            target = retrieveVariable(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        if (target.getId() == null) {
            target.setNameableArtefact(new NameableArtefact());
        }

        target.setPreviousType(target.getType());
        target.setType(source.getType());
        target.setShortName(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getShortName(), target.getShortName(), ServiceExceptionParameters.VARIABLE_SHORT_NAME));
        target.setValidFrom(CoreCommonUtil.transformDateToDateTime(source.getValidFrom()));
        target.setValidTo(CoreCommonUtil.transformDateToDateTime(source.getValidTo()));
        target.removeAllFamilies();
        for (RelatedResourceDto variableFamilyDto : source.getFamilies()) {
            target.addFamily(retrieveVariableFamily(variableFamilyDto.getUrn()));
        }
        target.removeAllReplaceToVariables();
        for (RelatedResourceDto replaceToVariable : source.getReplaceToVariables()) {
            // note: do not use 'addReplaceToVariable' method, to can check in service if any "replaceTo" variable was replaced by another variable
            target.getReplaceToVariables().add(retrieveVariable(replaceToVariable.getUrn()));
        }
        // note: replacedBy metadata is ignored, because it will be updated by replaceTo metadata

        target.setNameableArtefact(dto2DoMapperSdmxSrm.nameableArtefactToEntity(source, target.getNameableArtefact()));

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increment "version" attribute
        target.setUpdateDate(new DateTime());

        return target;
    }

    @Override
    public VariableElement variableElementDtoToDo(VariableElementDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        VariableElement target = null;
        if (source.getUrn() == null) {
            target = new VariableElement();
        } else {
            target = retrieveVariableElement(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        if (target.getId() == null) {
            target.setIdentifiableArtefact(new IdentifiableArtefact());
        }

        target.setShortName(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getShortName(), target.getShortName(), ServiceExceptionParameters.VARIABLE_ELEMENT_SHORT_NAME));
        target.setComment(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getComment(), target.getComment(), ServiceExceptionParameters.VARIABLE_ELEMENT_COMMENT));
        target.setValidFrom(CoreCommonUtil.transformDateToDateTime(source.getValidFrom()));
        target.setValidTo(CoreCommonUtil.transformDateToDateTime(source.getValidTo()));
        // note: replacedBy metadata is ignored, because it will be updated by replaceTo metadata
        if (source.getVariable() != null) {
            target.setVariable(retrieveVariable(source.getVariable().getUrn()));
        } else {
            target.setVariable(null);
        }
        target.removeAllReplaceToVariableElements();
        for (RelatedResourceDto replaceToVariableElement : source.getReplaceToVariableElements()) {
            // note: do not use 'addReplaceToVariableElement' method, to can check in service if any "replaceTo" variable was replaced by another variable
            target.getReplaceToVariableElements().add(retrieveVariableElement(replaceToVariableElement.getUrn()));
        }
        target.setIdentifiableArtefact(dto2DoMapperSdmxSrm.identifiableArtefactDtoToEntity(source, target.getIdentifiableArtefact()));
        target.setLatitude(source.getLatitude());
        target.setLongitude(source.getLongitude());
        target.setShapeWkt(source.getShapeWkt());
        target.setShapeGeojson(source.getShapeGeojson());
        if (source.getGeographicalGranularity() != null) {
            target.setGeographicalGranularity(retrieveCode(source.getGeographicalGranularity().getUrn()));
        } else {
            target.setGeographicalGranularity(null);
        }

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increment "version" attribute
        target.setUpdateDate(new DateTime());

        return target;
    }

    @Override
    public CodelistOrderVisualisation codelistOrderVisualisationDtoToDo(CodelistVisualisationDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CodelistOrderVisualisation target = null;
        if (source.getUrn() == null) {
            target = new CodelistOrderVisualisation();
        } else {
            target = retrieveCodelistOrderVisualisation(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        if (target.getId() == null) {
            target.setNameableArtefact(new NameableArtefact());
        }

        target.setNameableArtefact(dto2DoMapperSdmxSrm.nameableArtefactToEntity(source, target.getNameableArtefact()));

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increment "version" attribute
        target.setUpdateDate(new DateTime());

        return target;
    }

    @Override
    public CodelistOpennessVisualisation codelistOpennessVisualisationDtoToDo(CodelistVisualisationDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CodelistOpennessVisualisation target = null;
        if (source.getUrn() == null) {
            target = new CodelistOpennessVisualisation();
        } else {
            target = retrieveCodelistOpennessVisualisation(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        if (target.getId() == null) {
            target.setNameableArtefact(new NameableArtefact());
        }

        target.setNameableArtefact(dto2DoMapperSdmxSrm.nameableArtefactToEntity(source, target.getNameableArtefact()));

        // Optimistic locking: Update "update date" attribute to force update to root entity, to increment "version" attribute
        target.setUpdateDate(new DateTime());

        return target;
    }

    private CodelistVersionMetamac retrieveCodelist(String urn) throws MetamacException {
        CodelistVersionMetamac target = codelistVersionMetamacRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private CodeMetamac retrieveCode(String urn) throws MetamacException {
        CodeMetamac target = codeMetamacRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private CodelistFamily retrieveCodelistFamily(String urn) throws MetamacException {
        CodelistFamily target = codelistFamilyRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private VariableFamily retrieveVariableFamily(String urn) throws MetamacException {
        VariableFamily target = variableFamilyRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private Variable retrieveVariable(String urn) throws MetamacException {
        Variable target = variableRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private VariableElement retrieveVariableElement(String urn) throws MetamacException {
        VariableElement target = variableElementRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private CodelistOrderVisualisation retrieveCodelistOrderVisualisation(String urn) throws MetamacException {
        CodelistOrderVisualisation target = codelistOrderVisualisationRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private CodelistOpennessVisualisation retrieveCodelistOpennessVisualisation(String urn) throws MetamacException {
        CodelistOpennessVisualisation target = codelistOpennessVisualisationRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }

    private Boolean guessIsCodelistVariableUpdated(CodelistMetamacDto source, CodelistVersionMetamac target) {
        String sourceVariableUrn = source.getVariable() != null ? source.getVariable().getUrn() : null;
        String targetVariableUrn = target.getVariable() != null ? target.getVariable().getNameableArtefact().getUrn() : null;
        return !ObjectUtils.equals(sourceVariableUrn, targetVariableUrn);
    }

}