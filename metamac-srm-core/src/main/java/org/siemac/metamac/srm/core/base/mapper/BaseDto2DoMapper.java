package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.ent.domain.ExternalItem;
import org.siemac.metamac.core.common.exception.MetamacException;

public interface BaseDto2DoMapper {

    public ExternalItem externalItemDtoStatisticalOperationsToExternalItemDo(ExternalItemDto source, ExternalItem target, String metadataName) throws MetamacException;

}
