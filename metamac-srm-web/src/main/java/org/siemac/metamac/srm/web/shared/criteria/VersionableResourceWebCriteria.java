package org.siemac.metamac.srm.web.shared.criteria;

import java.util.Date;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;

public class VersionableResourceWebCriteria extends NameableArtefactWebCriteria {

    private static final long serialVersionUID = 1L;

    private ProcStatusEnum    procStatus;
    private Date              internalPublicationDate;
    private String            internalPublicationUser;
    private Date              externalPublicationDate;
    private String            externalPublicationUser;
    private Boolean           isLastVersion;
    private Boolean           isLatestFinal;
    private String            maintainerUrn;

    public VersionableResourceWebCriteria() {
    }

    public VersionableResourceWebCriteria(String criteria) {
        super(criteria);
    }

    public ProcStatusEnum getProcStatus() {
        return procStatus;
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

    public void setProcStatus(ProcStatusEnum procStatus) {
        this.procStatus = procStatus;
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

    public String getMaintainerUrn() {
        return maintainerUrn;
    }

    public void setMaintainerUrn(String maintainerUrn) {
        this.maintainerUrn = maintainerUrn;
    }

    public Boolean getIsLatestFinal() {
        return isLatestFinal;
    }

    public void setIsLatestFinal(Boolean isLatestFinal) {
        this.isLatestFinal = isLatestFinal;
    }
}
