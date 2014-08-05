package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.ExternalResourceWebCriteriaBase;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class RelatedResourceBaseWebCriteria extends ExternalResourceWebCriteriaBase<RelatedResourceTypeEnum> {

    private static final long serialVersionUID = 1L;

    public RelatedResourceBaseWebCriteria() {
    }

    public RelatedResourceBaseWebCriteria(RelatedResourceTypeEnum type) {
        super(type);
    }
}
