package org.siemac.metamac.srm.core.code.mapper;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.code.dto.CodeMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

@org.springframework.stereotype.Component("codesDto2DoMapper")
public class CodesDto2DoMapperImpl implements CodesDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.code.mapper.CodesDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private CodelistVersionMetamacRepository                               codelistVersionMetamacRepository;

    @Autowired
    private CodeMetamacRepository                                          codeMetamacRepository;

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
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }
        // Modifiable attributes
        target.setShortName(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getShortName(), target.getShortName(), ServiceExceptionParameters.CODELIST_SHORT_NAME));
        target.setIsRecommended(source.getIsRecommended());
        target.setAccessType(source.getAccessType());

        target.removeAllReplaceToCodelists();
        for (RelatedResourceDto replaceToCodelist : source.getReplaceToCodelists()) {
            target.addReplaceToCodelist(retrieveCodelist(replaceToCodelist.getUrn()));
        }
        // note: replacedBy metadata is ignored, because it is updated by replaceTo metadata

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
            target = codeMetamacRepository.findByUrn(source.getUrn());
            if (target == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        dto2DoMapperSdmxSrm.codeDtoToDo(source, target);

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
}