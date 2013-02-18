package org.siemac.metamac.srm.web.shared.criteria;

import java.util.Date;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class VersionableResourceWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    private String            code;
    private String            name;
    private String            urn;
    private String            description;
    private ProcStatusEnum    procStatusEnum;
    private Date              internalPublicationDate;
    private String            internalPublicationUser;
    private Date              externalPublicationDate;
    private String            externalPublicationUser;
    private Boolean           isLastVersion;

    public VersionableResourceWebCriteria() {
    }

    public VersionableResourceWebCriteria(String criteria) {
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

    public ProcStatusEnum getProcStatusEnum() {
        return procStatusEnum;
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

    public Boolean getIsLastVersion() {
        return isLastVersion;
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

    public void setProcStatusEnum(ProcStatusEnum procStatusEnum) {
        this.procStatusEnum = procStatusEnum;
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

    public void setIsLastVersion(Boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }
}
