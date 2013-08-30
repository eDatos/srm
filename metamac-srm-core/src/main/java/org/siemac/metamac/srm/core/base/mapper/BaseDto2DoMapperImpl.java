package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;

@org.springframework.stereotype.Component("baseDto2DoMapper")
public class BaseDto2DoMapperImpl implements org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapper {

    @Autowired
    @Qualifier("baseDto2DoMapperSdmxSrm")
    private com.arte.statistic.sdmx.srm.core.base.mapper.BaseDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private SrmConfiguration                                              srmConfiguration;

    @Override
    public ExternalItem externalItemDtoStatisticalOperationsToExternalItemDo(ExternalItemDto source, ExternalItem target, String metadataName) throws MetamacException {
        target = dto2DoMapperSdmxSrm.externalItemDtoToExternalItem(source, target, metadataName);
        if (target != null) {
            target.setUri(CoreCommonUtil.externalItemUrlDtoToUrlDo(getStatisticalOperationsInternalApiUrlBase(), target.getUri()));
            target.setManagementAppUrl(CoreCommonUtil.externalItemUrlDtoToUrlDo(getStatisticalOperationsInternalWebApplicationUrlBase(), target.getManagementAppUrl()));
        }
        return target;
    }

    private String getStatisticalOperationsInternalWebApplicationUrlBase() throws MetamacException {
        return srmConfiguration.retrieveStatisticalOperationsInternalWebApplicationUrlBase();
    }

    private String getStatisticalOperationsInternalApiUrlBase() throws MetamacException {
        return srmConfiguration.retrieveStatisticalOperationsInternalApiUrlBase();
    }
}
