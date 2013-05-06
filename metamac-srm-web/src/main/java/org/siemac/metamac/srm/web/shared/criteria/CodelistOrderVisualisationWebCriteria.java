package org.siemac.metamac.srm.web.shared.criteria;

public class CodelistOrderVisualisationWebCriteria extends MetamacWebCriteria {

    private String            dsdDimensionUrn;

    private static final long serialVersionUID = 1L;

    public CodelistOrderVisualisationWebCriteria() {
    }

    public CodelistOrderVisualisationWebCriteria(String criteria) {
        super(criteria);
    }

    public String getDsdDimensionUrn() {
        return dsdDimensionUrn;
    }

    public void setDsdDimensionUrn(String dsdDimensionUrn) {
        this.dsdDimensionUrn = dsdDimensionUrn;
    }
}
