package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;

public class CategorySchemeRecord extends ItemSchemeRecord {

    public CategorySchemeRecord() {
    }

    public CategorySchemeRecord(Long id, String code, String name, String status, String versionLogic, String urn, String maintainer, String internalPublicationDate, String internalPublicationUser,
            String externalPublicationDate, String externalPublicationUser) {
        super();
        setId(id);
        setCode(code);
        setName(name);
        setProcStatus(status);
        setVersionLogic(versionLogic);
        setUrn(urn);
        setMaintainer(maintainer);
        setInternalPublicationDate(internalPublicationDate);
        setInternalPublicationUser(internalPublicationUser);
        setExternalPublicationDate(externalPublicationDate);
        setExternalPublicationUser(externalPublicationUser);
    }

    public void setCategorySchemeBasicDto(CategorySchemeMetamacBasicDto categorySchemeMetamacDto) {
        setAttribute(CategorySchemeDS.DTO, categorySchemeMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((CategorySchemeMetamacBasicDto) getAttributeAsObject(CategorySchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public CategorySchemeMetamacBasicDto getCategorySchemeBasicDto() {
        return (CategorySchemeMetamacBasicDto) getAttributeAsObject(CategorySchemeDS.DTO);
    }
}
