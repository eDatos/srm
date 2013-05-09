package org.siemac.metamac.srm.core.organisation.domain.shared;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

public class OrganisationMetamacVisualisationResult extends ItemVisualisationResult {

    private static final long    serialVersionUID = 1L;

    private OrganisationTypeEnum type;

    public OrganisationMetamacVisualisationResult() {
    }

    public OrganisationTypeEnum getType() {
        return type;
    }

    public void setType(OrganisationTypeEnum type) {
        this.type = type;
    }
}