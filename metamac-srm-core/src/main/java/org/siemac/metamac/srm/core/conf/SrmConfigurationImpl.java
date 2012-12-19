package org.siemac.metamac.srm.core.conf;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("srmConfiguration")
public class SrmConfigurationImpl implements SrmConfiguration {

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public String retrieveMaintainerUrnDefault() {
        return configurationService.getProperty(SrmConstants.METAMAC_ORGANISATION_URN);
    }
}
