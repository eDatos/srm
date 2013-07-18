package org.siemac.metamac.srm.core.job;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

public class MetamacCronTriggerFactoryBean extends CronTriggerFactoryBean {

    @Autowired
    private SrmConfiguration srmConfiguration;

    public void setCronExpressionProperty(String cronExpressionProperty) throws MetamacException {
        String cronExpression = srmConfiguration.retrieveJobDeleteDeprecatedEntitiesCronExpression(); // Note: parameter is ignored
        if (cronExpression == null) {
            cronExpression = "0 0 3 * * ? 2099"; // never execute
        }
        super.setCronExpression(cronExpression);
    }
}
