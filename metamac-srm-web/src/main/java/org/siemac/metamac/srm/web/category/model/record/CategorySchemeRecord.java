package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeDS;
import org.siemac.metamac.srm.web.client.model.record.ItemSchemeRecord;

public class CategorySchemeRecord extends ItemSchemeRecord {

    public CategorySchemeRecord() {
    }

    public CategorySchemeRecord(Long id, String code, String name, String status, String versionLogic, String urn, String maintainer, String internalPublicationDate, String internalPublicationUser,
            String externalPublicationDate, String externalPublicationUser, CategorySchemeMetamacDto categorySchemeMetamacDto) {
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
        setCategorySchemeDto(categorySchemeMetamacDto);
    }

    public void setCategorySchemeDto(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        setAttribute(CategorySchemeDS.DTO, categorySchemeMetamacDto);
    }

    public void setCategorySchemeBasicDto(CategorySchemeMetamacBasicDto categorySchemeMetamacDto) {
        setAttribute(CategorySchemeDS.DTO, categorySchemeMetamacDto);
    }

    public ProcStatusEnum getProcStatus() {
        return ((CategorySchemeMetamacDto) getAttributeAsObject(CategorySchemeDS.DTO)).getLifeCycle().getProcStatus();
    }

    public CategorySchemeMetamacDto getCategorySchemeDto() {
        return (CategorySchemeMetamacDto) getAttributeAsObject(CategorySchemeDS.DTO);
    }

    public CategorySchemeMetamacBasicDto getCategorySchemeBasicDto() {
        return (CategorySchemeMetamacBasicDto) getAttributeAsObject(CategorySchemeDS.DTO);
    }
}
