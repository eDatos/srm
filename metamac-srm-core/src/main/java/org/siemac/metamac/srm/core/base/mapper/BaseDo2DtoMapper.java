package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.dto.LifeCycleDto;

public interface BaseDo2DtoMapper {

    public LifeCycleDto lifeCycleDoToDto(SrmLifeCycleMetadata source);

}
