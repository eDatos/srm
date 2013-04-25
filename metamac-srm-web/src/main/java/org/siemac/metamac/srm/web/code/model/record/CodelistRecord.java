package org.siemac.metamac.srm.web.code.model.record;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;

public class CodelistRecord extends ItemSchemeRecord {

    public CodelistRecord() {
    }

    public CodelistRecord(Long id, String code, String name, String description, String status, String versionLogic, String urn, String maintainer, String internalPublicationDate,
            String internalPublicationUser, String externalPublicationDate, String externalPublicationUser) {
        super();
        setId(id);
        setCode(code);
        setName(name);
        setProcStatus(status);
        setVersionLogic(versionLogic);
        setUrn(urn);
        setDescription(description);
        setMaintainer(maintainer);
        setInternalPublicationDate(internalPublicationDate);
        setInternalPublicationUser(internalPublicationUser);
        setExternalPublicationDate(externalPublicationDate);
        setExternalPublicationUser(externalPublicationUser);
    }

    public void setDescription(String desc) {
        setAttribute(CodelistDS.DESCRIPTION, desc);
    }

    public void setCodelistBasicDto(CodelistMetamacBasicDto codelistMetamacDto) {
        setAttribute(CodelistDS.DTO, codelistMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((CodelistMetamacBasicDto) getAttributeAsObject(CodelistDS.DTO)).getProcStatus();
    }

    public String getDescription() {
        return getAttribute(CodelistDS.DESCRIPTION);
    }

    public CodelistMetamacBasicDto getCodelistMetamacBasicDto() {
        return (CodelistMetamacBasicDto) getAttributeAsObject(CodelistDS.DTO);
    }
}
