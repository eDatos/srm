package org.siemac.metamac.srm.web.shared.criteria;

import java.util.Date;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class ConceptSchemeWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            dsdUrn;
    private Boolean           isLastVersion;
    private String            code;
    private String            name;
    private String            urn;
    private String            description;
    private ProcStatusEnum    procStatusEnum;
    private Date              internalPublicationDate;
    private String            internalPublicationUser;
    private Date              externalPublicationDate;
    private String            externalPublicationUser;

    public ConceptSchemeWebCriteria() {
    }

    public ConceptSchemeWebCriteria(String criteria) {
        super(criteria);
    }

    public ConceptSchemeWebCriteria(String criteria, String dsdUrn) {
        super(criteria);
        this.dsdUrn = dsdUrn;
    }

    public String getDsdUrn() {
        return dsdUrn;
    }

    public void setDsdUrn(String dsdUrn) {
        this.dsdUrn = dsdUrn;
    }

    public Boolean getIsLastVersion() {
        return isLastVersion;
    }

    public void setIsLastVersion(Boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProcStatusEnum getProcStatusEnum() {
        return procStatusEnum;
    }

    public void setProcStatusEnum(ProcStatusEnum procStatusEnum) {
        this.procStatusEnum = procStatusEnum;
    }

    public Date getInternalPublicationDate() {
        return internalPublicationDate;
    }

    public String getInternalPublicationUser() {
        return internalPublicationUser;
    }

    public Date getExternalPublicationDate() {
        return externalPublicationDate;
    }

    public String getExternalPublicationUser() {
        return externalPublicationUser;
    }

    public void setInternalPublicationDate(Date internalPublicationDate) {
        this.internalPublicationDate = internalPublicationDate;
    }

    public void setInternalPublicationUser(String internalPublicationUser) {
        this.internalPublicationUser = internalPublicationUser;
    }

    public void setExternalPublicationDate(Date externalPublicationDate) {
        this.externalPublicationDate = externalPublicationDate;
    }

    public void setExternalPublicationUser(String externalPublicationUser) {
        this.externalPublicationUser = externalPublicationUser;
    }
}
