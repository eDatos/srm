package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.base.HasOnlyExternallyPublishedCriteria;
import org.siemac.metamac.web.common.shared.criteria.base.HasOnlyLastVersionCriteria;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class RelatedResourceWebCriteria extends RelatedResourceBaseWebCriteria implements HasOnlyLastVersionCriteria, HasOnlyExternallyPublishedCriteria {

    private static final long serialVersionUID        = 1L;

    private boolean           onlyExternallyPublished = false;
    private boolean           onlyLastVersion         = false;

    public RelatedResourceWebCriteria() {
    }

    public RelatedResourceWebCriteria(RelatedResourceTypeEnum type) {
        super(type);
    }

    @Override
    public boolean isOnlyExternallyPublished() {
        return onlyExternallyPublished;
    }

    @Override
    public void setOnlyExternallyPublished(boolean externallyPublished) {
        onlyExternallyPublished = externallyPublished;
    }

    @Override
    public boolean isOnlyLastVersion() {
        return onlyLastVersion;
    }

    @Override
    public void setOnlyLastVersion(boolean onlyLastVersion) {
        this.onlyLastVersion = onlyLastVersion;
    }

}
