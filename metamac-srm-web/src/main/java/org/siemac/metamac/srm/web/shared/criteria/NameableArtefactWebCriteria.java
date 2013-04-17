package org.siemac.metamac.srm.web.shared.criteria;

public class NameableArtefactWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            code;
    private String            codeEQ;               // Use this field when the comparison should be made with EQUALS (not ILIKE!)
    private String            name;
    private String            urn;
    private String            description;

    public NameableArtefactWebCriteria() {
    }

    public NameableArtefactWebCriteria(String criteria) {
        super(criteria);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getUrn() {
        return urn;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeEQ() {
        return codeEQ;
    }

    public void setCodeEQ(String codeEQ) {
        this.codeEQ = codeEQ;
    }
}
