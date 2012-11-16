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
import org.siemac.metamac.srm.core.code.exception.CodeMetamacNotFoundException;
import org.siemac.metamac.srm.core.code.exception.CodelistVersionMetamacNotFoundException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.springframework.beans.factory.annotation.Autowired;

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
        if (source.getId() == null) {
            target = new CodelistVersionMetamac();
        } else {
            try {
                target = codelistVersionMetamacRepository.findById(source.getId());
            } catch (CodelistVersionMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SEARCH_BY_ID_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.CODELIST, source.getId()).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
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
        if (source.getId() == null) {
            target = new CodeMetamac();
        } else {
            try {
                target = codeMetamacRepository.findById(source.getId());
            } catch (CodeMetamacNotFoundException e) {
                throw MetamacExceptionBuilder.builder().withCause(e).withExceptionItems(ServiceExceptionType.SEARCH_BY_ID_NOT_FOUND)
                        .withMessageParameters(ServiceExceptionParameters.CODE, source.getId()).withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersion());
        }

        dto2DoMapperSdmxSrm.codeDtoToDo(source, target);

        return target;
    }
}