package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class CategorySchemeWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private Boolean           isLastVersion;
    private ProcStatusEnum    procStatus;

    public CategorySchemeWebCriteria() {
    }

    public CategorySchemeWebCriteria(String criteria) {
        super(criteria);
    }

    public Boolean getIsLastVersion() {
        return isLastVersion;
    }

    public void setIsLastVersion(Boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }

    public ProcStatusEnum getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(ProcStatusEnum procStatus) {
        this.procStatus = procStatus;
    }
}
