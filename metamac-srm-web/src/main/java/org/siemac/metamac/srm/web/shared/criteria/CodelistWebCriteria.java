package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;

public class CodelistWebCriteria extends VersionableResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            conceptUrn;
    private String            codelistFamilyUrn;
    private AccessTypeEnum    accessType;
    private String            isNotCodelistUrn;

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

    public String getCodelistFamilyUrn() {
        return codelistFamilyUrn;
    }

    public void setCodelistFamilyUrn(String codelistFamilyUrn) {
        this.codelistFamilyUrn = codelistFamilyUrn;
    }

    public AccessTypeEnum getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessTypeEnum accessType) {
        this.accessType = accessType;
    }

    public String getIsNotCodelistUrn() {
        return isNotCodelistUrn;
    }

    public void setIsNotCodelistUrn(String isNotCodelistUrn) {
        this.isNotCodelistUrn = isNotCodelistUrn;
    }
}
