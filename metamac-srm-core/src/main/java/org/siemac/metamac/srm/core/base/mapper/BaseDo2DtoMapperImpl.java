package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;

public class BaseDo2DtoMapperImpl implements org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapper {

    @Override
    public LifeCycleDto lifeCycleDoToDto(SrmLifeCycleMetadata source) {
        if (source == null) {
            return null;
        }
        LifeCycleDto target = new LifeCycleDto();
        target.setProcStatus(source.getProcStatus());
        target.setProductionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getProductionValidationDate()));
        target.setProductionValidationUser(source.getProductionValidationUser());
        target.setDiffusionValidationDate(CoreCommonUtil.transformDateTimeToDate(source.getDiffusionValidationDate()));
        target.setDiffusionValidationUser(source.getDiffusionValidationUser());
        target.setInternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getInternalPublicationDate()));
        target.setInternalPublicationUser(source.getInternalPublicationUser());
        target.setExternalPublicationDate(CoreCommonUtil.transformDateTimeToDate(source.getExternalPublicationDate()));
        target.setExternalPublicationUser(source.getExternalPublicationUser());
        // TODO isExternalPublicationFailed
        // TODO externalPublicationFailedDate
        return target;
    }

}
