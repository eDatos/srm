package org.siemac.metamac.srm.web.shared.criteria;

public class CodelistWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            conceptUrn;

    public CodelistWebCriteria() {
    }

    public CodelistWebCriteria(String criteria) {
        super(criteria);
    }

    public CodelistWebCriteria(String criteria, String conceptUrn) {
        super(criteria);
        this.conceptUrn = conceptUrn;
    }

    public String getConceptUrn() {
        return conceptUrn;
    }

    public void setConceptUrn(String conceptUrn) {
        this.conceptUrn = conceptUrn;
    }
}
