package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;

public interface BaseDto2DoMapper {

    public ExternalItem externalItemDtoStatisticalOperationsToExternalItemDo(ExternalItemDto source, ExternalItem target, String metadataName) throws MetamacException;

}
