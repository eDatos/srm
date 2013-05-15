package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public class CodelistOpennessLevelVisualisationWebCriteria extends MetamacWebCriteria {

    private String            dsdDimensionUrn;

    private static final long serialVersionUID = 1L;

    public CodelistOpennessLevelVisualisationWebCriteria() {
    }

    public CodelistOpennessLevelVisualisationWebCriteria(String criteria) {
        super(criteria);
    }

    public String getDsdDimensionUrn() {
        return dsdDimensionUrn;
    }

    public void setDsdDimensionUrn(String dsdDimensionUrn) {
        this.dsdDimensionUrn = dsdDimensionUrn;
    }
}
