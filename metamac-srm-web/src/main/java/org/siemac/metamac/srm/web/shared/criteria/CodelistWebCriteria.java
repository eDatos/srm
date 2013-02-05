package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class CodelistWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            conceptUrn;
    private ProcStatusEnum    procStatus;
    private String            codelistFamilyUrn;
    private Boolean           isLastVersion;

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

    public ProcStatusEnum getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(ProcStatusEnum procStatus) {
        this.procStatus = procStatus;
    }

    public String getCodelistFamilyUrn() {
        return codelistFamilyUrn;
    }

    public void setCodelistFamilyUrn(String codelistFamilyUrn) {
        this.codelistFamilyUrn = codelistFamilyUrn;
    }

    public Boolean getIsLastVersion() {
        return isLastVersion;
    }

    public void setIsLastVersion(Boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }
}
