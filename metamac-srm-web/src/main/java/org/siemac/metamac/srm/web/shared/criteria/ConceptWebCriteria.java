package org.siemac.metamac.srm.web.shared.criteria;

public class ConceptWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            dsdUrn;
    private String            conceptSchemeUrn;

    public ConceptWebCriteria() {
    }

    public ConceptWebCriteria(String criteria) {
        super(criteria);
    }

    public ConceptWebCriteria(String criteria, String conceptSchemeUrn) {
        super(criteria);
        this.conceptSchemeUrn = conceptSchemeUrn;
    }

    public ConceptWebCriteria(String criteria, String dsdUrn, String conceptSchemeUrn) {
        super(criteria);
        this.dsdUrn = dsdUrn;
        this.conceptSchemeUrn = conceptSchemeUrn;
    }

    public String getConceptSchemeUrn() {
        return conceptSchemeUrn;
    }

    public void setConceptSchemeUrn(String conceptSchemeUrn) {
        this.conceptSchemeUrn = conceptSchemeUrn;
    }

    public String getDsdUrn() {
        return dsdUrn;
    }

    public void setDsdUrn(String dsdUrn) {
        this.dsdUrn = dsdUrn;
    }
}
